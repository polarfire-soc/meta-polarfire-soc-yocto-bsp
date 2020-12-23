#include <sys/stat.h>
#include <sys/mman.h>
#include <sys/types.h>

#include <fcntl.h>
#include <errno.h>
#include <string.h>
#include <stdint.h>
#include <unistd.h>
#include <stdio.h>
#include <poll.h>

uint8_t dma_complete = 0;

int uioFd_0,d=0;
int uioFd_1;
volatile uint32_t *ddr_mem,*dma_mem,*lsram_mem;

#define UIO_0_DEVICE    "/dev/uio0"
#define UIO_1_DEVICE    "/dev/uio2"

#define MMAP_SIZE     0x10000

int main(int argc, char* argvp[])
{
	int retCode = 0;
	char d1;	
	uint32_t memread[5]={0xAA,0x77,0xbb,0xcc,0x05};
	uint32_t val = 0;
	volatile uint32_t i=0 ;
	uint32_t var1 =0;
	int32_t intInfo;
	ssize_t readSize;
	uint32_t reenable=1,pending=0,temp1;
 
	// Open uio_0 device
	uioFd_0 = open(UIO_0_DEVICE, O_RDWR);
	if(uioFd_0 < 0)
	{
		fprintf(stderr, "Cannot open %s: %s\n", UIO_0_DEVICE, strerror(errno));
		return -1;
	}

	uioFd_1 = open(UIO_1_DEVICE, O_RDWR);
	if(uioFd_0 < 0)
	{
		fprintf(stderr, "Cannot open %s: %s\n", UIO_0_DEVICE, strerror(errno));
		return -1;
	}

	dma_mem = mmap(NULL, 0x1000, PROT_READ|PROT_WRITE, MAP_SHARED, uioFd_1, 0*getpagesize());
			if(dma_mem == MAP_FAILED){
				fprintf(stderr, "Cannot mmap: %s\n", strerror(errno));
				close(uioFd_0);
				return -1;
			}
	
	while(1){
		printf("\n\t # Choose one of  the following options: \n\t Enter 1 to perform memory test on LSRAM \n\t Enter 2 to write data from LSRAM to LPDD4 via DMA access  \n\t Enter 3 to Exit\n");
		scanf("%c%*c",&d1);
		if(d1=='3'){
			break;
		}else if(d1=='1')
			{
				lsram_mem = mmap(NULL, MMAP_SIZE, PROT_READ|PROT_WRITE, MAP_SHARED, uioFd_0, 0*getpagesize());
				if(lsram_mem == MAP_FAILED){
					fprintf(stderr, "Cannot mmap: %s\n", strerror(errno));
					close(uioFd_0);
					return -1;
				}
				printf("\nWriting incremental pattern starting from address 0x61000000\n");
				printf("\nReading data starting from address 0x61000000 \n");
				printf("\nComparing data \n");

				for(i=0;i<(MMAP_SIZE/4);i++)
				{
					*(lsram_mem+i)=i;
				 	val=*(lsram_mem+i);
                 			if(val !=i) {
						printf("\n\n\r ***** LSRAM memory test Failed***** \n\r");
                        			break;
                    			}
                   			else if(i%600 == 0)
                    			{
                    				printf(".");
                    			}
				}
				if(i == (MMAP_SIZE/4))
					printf("\n\n\n**** LSRAM memory test Passed with incremental pattern *****\n\n");
		}
		else if(d1=='2') 
		{
			lsram_mem = mmap(NULL, MMAP_SIZE, PROT_READ|PROT_WRITE, MAP_SHARED, uioFd_0, 0*getpagesize());
			if(lsram_mem == MAP_FAILED){
				fprintf(stderr, "Cannot mmap: %s\n", strerror(errno));
				close(uioFd_0);
				return -1;
			}
			
			ddr_mem = mmap(NULL, MMAP_SIZE, PROT_READ|PROT_WRITE, MAP_SHARED, uioFd_0, 1*getpagesize());
			if(ddr_mem == MAP_FAILED){
				fprintf(stderr, "Cannot mmap: %s\n", strerror(errno));
				close(uioFd_0);
				return -1;
			}
			printf("\nInitialized LSRAM (64KB) with incremental pattern.\n");
        	/* Initialize LSRAM */
			for(i=0;i<64;i=i+1) {
				*(lsram_mem +i) = 0x12345678+i;
                		var1 = *(lsram_mem+i);
				if(var1 == 0x12345678+i){
					printf(".");
				}else {
                    			printf("\n\n\r *****LSRAM memory test Failed***** \n\r");
                        		break;
                    }
               }
	    printf("\nFabric DMA controller configured for LSRAM to LPDDR4 data transfer.\n");
            temp1 = *(dma_mem);   //version control register

	    *(dma_mem + (0x14/4))  = 0x00000001;  //Interrupt mask

            *(dma_mem + (0x68/4))  = 0x61000000;  // Source address
            printf("\n\r\t Source Address (LSRAM) - 0x61000000 \n\r");

            *(dma_mem + (0x6C/4))  = 0xC0000000;  //destination address
            printf("\n\r\tDestination Address (LPDDR4) - 0xC000000 \n\r");

//            *(dma_mem + (0x64/4))  = 0x00000020;  //byte count
            *(dma_mem + (0x64/4))  = 0x00010000;  //byte count
	    printf("\n\r\tByte count - 0x10000 \n\r");

	    *(dma_mem + (0x60/4))  = 0x0000F005;  //data ready,descriptor valid,sop,dop,chain

	    printf("\n\rDMA Transfer Initiated... \n\r");
            * (dma_mem + (0x04/4))  = 0x00000001;  //start operation register

	    readSize = read(uioFd_1, &pending, sizeof(intInfo));
            if(readSize < 0){
                   fprintf(stderr, "Cannot wait for uio device interrupt: %s\n",
                   strerror(errno));
                   retCode = -1;
                   break;
               }
			   
	    printf("\n****DMA Transfer completed and interrupt generated.*****\n");
	    printf("\nCleared DMA interrupt. \n");
	    printf("\nComparing LSRAM data with LPDDR4 data... \n");

	    while(1){
                 for(i=0;i<8 ;i++){
                     val = *(ddr_mem+i);
                     var1 = *(lsram_mem+i);
                     if(val==var1){
                          printf(".");
                     }else{
                          printf("\nLPDDR4 data verification failed\n");
                          break;
                     }
                 }

		if(i == 8)
                 printf("\n***** Data Verification Passed *****\n");

		break;
         	}
		printf("\n Displaying interrupt count by executing \"cat /proc/interrupts \":\n");
		{	
			char command[50];
			strcpy(command, "cat /proc/interrupts" );
			system(command);
			printf("\n\n");
   		}
	}else {
			printf("Enter either 1, 2, and 3\n");
		}
	munmap((void*)lsram_mem, MMAP_SIZE);
	munmap((void*)ddr_mem, MMAP_SIZE);

	}	
	munmap((void*)dma_mem, 0x1000);
	close(uioFd_0);
	close(uioFd_1);
	return 0;
}

