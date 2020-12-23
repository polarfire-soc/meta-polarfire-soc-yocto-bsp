#include <sys/stat.h>
#include <sys/mman.h>

#include <fcntl.h>
#include <errno.h>
#include <string.h>
#include <stdint.h>
#include <unistd.h>
#include <stdio.h>

#define UIO_0_DEVICE    "/dev/uio0"

#define MMAP_SIZE     0x10000

int main(int argc, char* argvp[])
{
	int retCode = 0;
	int uioFd_0,uioFd_1,d=0;
	char d1;
	volatile uint32_t* mem_ptr0,*mem_ptr1,*mem_ptr2,*mem_ptr3;
	uint32_t memread[5]={0xAA,0x77,0xbb,0xcc,0x05};
	uint32_t val = 0;
	volatile uint32_t i =0 ;

 
	// Open uio_0 device
	uioFd_0 = open(UIO_0_DEVICE, O_RDWR);
	if(uioFd_0 < 0)
	{
		fprintf(stderr, "Cannot open %s: %s\n", UIO_0_DEVICE, strerror(errno));
		return -1;
	}
	while(1){
		printf("\n\t # Choose one of  the following options: \n\t Enter 1 to perform memory test on LSRAM \n\t Enter 2 to Exit\n");
		scanf("%c%*c",&d1);
		if(d1=='2'){
			break;
		}else if(d1=='1'){
			mem_ptr0 = mmap(NULL, MMAP_SIZE, PROT_READ|PROT_WRITE, MAP_SHARED, uioFd_0, 0*getpagesize());
			if(mem_ptr0 == MAP_FAILED){
				fprintf(stderr, "Cannot mmap: %s\n", strerror(errno));
				close(uioFd_0);
				return -1;
			}
			printf("\nWriting incremental pattern starting from address 0x61000000\n");
			printf("\nReading data starting from address 0x61000000 \n");
			printf("\nComparing data \n");

			for(i=0;i<(MMAP_SIZE/4);i++) {
				*(mem_ptr0+i)=i;
				val=*(mem_ptr0+i);
                                if(val !=i){
                                        printf("\n\n\r ***** LSRAM memory test Failed***** \n\r");
                                        break;
                                }
                                else if(i%100 == 0)
                                {
                                        printf(".");
                                }

			}
			if(i == (MMAP_SIZE/4))
				printf("\n\n\n**** LSRAM memory test passed successfully *****\n");
		}else {
			printf("Enter either 1 or 2\n");
		}
		munmap((void*)mem_ptr0, MMAP_SIZE);
	}

	close(uioFd_0);
	return retCode;
}
