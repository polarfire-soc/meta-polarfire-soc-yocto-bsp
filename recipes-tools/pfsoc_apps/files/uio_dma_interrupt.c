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
#include <stdlib.h>

#define SYSFS_PATH_LEN		(128)
#define ID_STR_LEN		(32)
#define UIO_DEVICE_PATH_LEN	(32)
#define NUM_UIO_DEVICES		(32)

char uio_id_str_lpddr4_lsram[] = "uio_lpddr4";
char uio_id_str_dma_dev[] = "mss_dma0";
char sysfs_template[] = "/sys/class/uio/uio%d/%s";

/*****************************************/
/* function get_uio_device will return */
/* the uio device number */
/*************************************** */

int get_uio_device(char * id)
{
	FILE *fp;
	int i;
	int len;
	char file_id[ID_STR_LEN];
	char sysfs_path[SYSFS_PATH_LEN];

	for (i = 0; i < NUM_UIO_DEVICES; i++) {
		snprintf(sysfs_path, SYSFS_PATH_LEN, sysfs_template, i, "/name");
		fp = fopen(sysfs_path, "r");
		if (fp == NULL)
			break;

		fscanf(fp, "%32s", file_id);
		len = strlen(id);
		if (len > ID_STR_LEN-1)
			len = ID_STR_LEN-1;
		if (strncmp(file_id, id, len) == 0) {
			return i;
		}
	}

	return -1;
}

/*****************************************/
/* function get_memory_size will return */
/* the uio device size */
/*************************************** */

uint32_t get_memory_size(char *sysfs_path, char *uio_device)
{
	FILE *fp;
	uint32_t sz;

	/* 
	 * open the file the describes the memory range size.
	 * this is set up by the reg property of the node in the device tree
	 */
	fp = fopen(sysfs_path, "r");
	if (fp == NULL) {
		fprintf(stderr, "unable to determine size for %s\n", uio_device);
		exit(0);
	}

	fscanf(fp, "0x%016X", &sz);
	fclose(fp);
	return sz;
}

uint8_t dma_complete = 0;


int main(int argc, char* argvp[])
{
	char d1;	
	uint32_t val = 0;
	char uio_device_mem[UIO_DEVICE_PATH_LEN];
	char uio_device_dma_dev[UIO_DEVICE_PATH_LEN];
	char sysfs_path[SYSFS_PATH_LEN];
	uint32_t mmap_size_lpddr4, mmap_size_lsram, mmap_size_dma_dev;
	volatile uint32_t *lpddr4_mem,*dma_dev,*lsram_mem;
	volatile uint32_t i=0 ;
	int uioFd_0,uioFd_1, index=0;
	uint32_t var1 =0;
	int32_t intInfo;
	ssize_t readSize;
	uint32_t pending=0;
	//uint32_t temp1;

	printf("locating device for %s\n", uio_id_str_lpddr4_lsram);
        index = get_uio_device(uio_id_str_lpddr4_lsram);
	if (index < 0) {
		fprintf(stderr, "can't locate uio device for %s\n", uio_id_str_lpddr4_lsram);
		return -1;
	}

	snprintf(uio_device_mem, UIO_DEVICE_PATH_LEN, "/dev/uio%d", index);
	printf("located %s\n", uio_device_mem);

	uioFd_0 = open(uio_device_mem, O_RDWR);
	if(uioFd_0 < 0) {
		fprintf(stderr, "cannot open %s: %s\n", uio_device_mem, strerror(errno));
		return -1;
	} else {
		printf("opened %s (r,w)\n", uio_device_mem);
	}

	snprintf(sysfs_path, SYSFS_PATH_LEN, sysfs_template, index, "maps/map0/size");
	mmap_size_lsram = get_memory_size(sysfs_path, uio_device_mem);
	if (mmap_size_lsram == 0) {
		fprintf(stderr, "bad memory size for %s\n", uio_device_mem);
		return -1;
	}

	snprintf(sysfs_path, SYSFS_PATH_LEN, sysfs_template, index, "maps/map1/size");
	mmap_size_lpddr4 = get_memory_size(sysfs_path, uio_device_mem);
	if (mmap_size_lpddr4 == 0) {
		fprintf(stderr, "bad memory size for %s\n", uio_device_mem);
		return -1;
	}
 
	printf("locating device for %s\n", uio_id_str_dma_dev);
        index = get_uio_device(uio_id_str_dma_dev);
	if (index < 0) {
		fprintf(stderr, "can't locate uio device for %s\n", uio_id_str_dma_dev);
		return -1;
	}

	snprintf(uio_device_dma_dev, UIO_DEVICE_PATH_LEN, "/dev/uio%d", index);
	printf("located %s\n", uio_device_dma_dev);

	uioFd_1 = open(uio_device_dma_dev, O_RDWR);
	if(uioFd_0 < 0) {
		fprintf(stderr, "cannot open %s: %s\n", uio_device_dma_dev, strerror(errno));
		return -1;
	} else {
		printf("opened %s (r,w)\n", uio_device_dma_dev);
	}

	snprintf(sysfs_path, SYSFS_PATH_LEN, sysfs_template, index, "maps/map0/size");
	mmap_size_dma_dev = get_memory_size(sysfs_path, uio_device_dma_dev);
	if (mmap_size_dma_dev == 0) {
		fprintf(stderr, "bad memory size for %s\n", uio_device_dma_dev);
		return -1;
	}

	dma_dev = mmap(NULL, mmap_size_dma_dev, PROT_READ|PROT_WRITE, MAP_SHARED, uioFd_1, 0*getpagesize());
		if(dma_dev == MAP_FAILED){
			fprintf(stderr, "Cannot mmap: %s\n", strerror(errno));
			close(uioFd_1);
			return -1;
		}
	
	while(1){
		printf("\n\t # Choose one of  the following options: \n\t Enter 1 to perform memory test on LSRAM \n\t Enter 2 to write data from LSRAM to LPDD4 via DMA access  \n\t Enter 3 to Exit\n");
		scanf("%c%*c",&d1);
		if(d1=='3'){
			break;
		}else if(d1=='1')
			{
				lsram_mem = mmap(NULL, mmap_size_lsram, PROT_READ|PROT_WRITE, MAP_SHARED, uioFd_0, 0*getpagesize());
				if(lsram_mem == MAP_FAILED){
					fprintf(stderr, "Cannot mmap: %s\n", strerror(errno));
					close(uioFd_0);
					return -1;
				}
				printf("\nWriting incremental pattern starting from address 0x61000000\n");
				printf("\nReading data starting from address 0x61000000 \n");
				printf("\nComparing data \n");

				for(i=0;i<(mmap_size_lsram/4);i++)
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
				if(i == (mmap_size_lsram/4))
					printf("\n\n\n**** LSRAM memory test Passed with incremental pattern *****\n\n");
		}
		else if(d1=='2') 
		{
			lsram_mem = mmap(NULL, mmap_size_lsram, PROT_READ|PROT_WRITE, MAP_SHARED, uioFd_0, 0*getpagesize());
			if(lsram_mem == MAP_FAILED){
				fprintf(stderr, "Cannot mmap: %s\n", strerror(errno));
				close(uioFd_0);
				return -1;
			}
			
			lpddr4_mem = mmap(NULL, mmap_size_lpddr4, PROT_READ|PROT_WRITE, MAP_SHARED, uioFd_0, 1*getpagesize());
			if(lpddr4_mem == MAP_FAILED){
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
            //temp1 = *(dma_dev);   //version control register

	    *(dma_dev + (0x14/4))  = 0x00000001;  //Interrupt mask

            *(dma_dev + (0x68/4))  = 0x61000000;  // Source address
            printf("\n\r\t Source Address (LSRAM) - 0x61000000 \n\r");

            *(dma_dev + (0x6C/4))  = 0xC0000000;  //destination address
            printf("\n\r\tDestination Address (LPDDR4) - 0xC000000 \n\r");

//            *(dma_mem + (0x64/4))  = 0x00000020;  //byte count
            *(dma_dev + (0x64/4))  = 0x00010000;  //byte count
	    printf("\n\r\tByte count - 0x10000 \n\r");

	    *(dma_dev + (0x60/4))  = 0x0000F005;  //data ready,descriptor valid,sop,dop,chain

	    printf("\n\rDMA Transfer Initiated... \n\r");
            * (dma_dev + (0x04/4))  = 0x00000001;  //start operation register

	    readSize = read(uioFd_1, &pending, sizeof(intInfo));
            if(readSize < 0){
                   fprintf(stderr, "Cannot wait for uio device interrupt: %s\n",
                   strerror(errno));
                   break;
               }
			   
	    printf("\n****DMA Transfer completed and interrupt generated.*****\n");
	    printf("\nCleared DMA interrupt. \n");
	    printf("\nComparing LSRAM data with LPDDR4 data... \n");

	    while(1){
                 for(i=0;i<8 ;i++){
                     val = *(lpddr4_mem+i);
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
	munmap((void*)lsram_mem, mmap_size_lsram);
	munmap((void*)lpddr4_mem, mmap_size_lpddr4);

	}
	munmap((void*)dma_dev, mmap_size_dma_dev);
	close(uioFd_0);
	printf("closed %s\n", uio_device_mem);
	close(uioFd_1);
	printf("closed %s\n", uio_device_dma_dev);
	return 0;
}
