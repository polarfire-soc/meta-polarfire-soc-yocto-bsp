#include <errno.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

int main()
{
	char option;
	int j=0;
	// Export the desired pin by writing to /sys/class/gpio/export

	int led1 = open("/sys/class/gpio/export", O_WRONLY);
	if (led1 == -1) {
		perror("Unable to open /sys/class/gpio/export");
		exit(1);
	}
	int led2 = open("/sys/class/gpio/export", O_WRONLY);
	if (led2 ==-1) {
		perror("Unable to open /sys/class/gpio/export");
		exit(1);
	}
	
	int led3 = open("/sys/class/gpio/export", O_WRONLY);
	if (led3 == -1) {
		perror("Unable to open /sys/class/gpio/export");
		exit(1);
	}
	int led4 = open("/sys/class/gpio/export", O_WRONLY);
	if (led4 == -1) {
		perror("Unable to open /sys/class/gpio/export");
		exit(1);
	}

	//Export led1
	write(led1, "16", 2);

    
	//Export led2
	write(led2, "17", 2);
	//Export led3
	write(led3, "18", 2);
	//Export led4
	write(led4, "19", 2);



	close(led1);
	close(led2);
	close(led3);
	close(led4);
    
	// Set the pin to be an output by writing "out" to /sys/class/gpio/gpio24/direction
	//set led1 direction as out
	led1 = open("/sys/class/gpio/gpio16/direction", O_WRONLY);
	if (led1 == -1) {
		perror("Unable to open /sys/class/gpio/gpio16/direction");
		exit(1);
	}

	if (write(led1, "out", 3) != 3) {
		perror("Error writing to /sys/class/gpio/gpio16/direction");
		exit(1);
	}

	//set led5 direction as out
	led2 = open("/sys/class/gpio/gpio17/direction", O_WRONLY);
	if (led2 == -1) {
		perror("Unable to open /sys/class/gpio/gpio17/direction");
		exit(1);
	}
	if (write(led2, "out", 3) != 3) {
		perror("Error writing to /sys/class/gpio/gpio17/direction");
		exit(1);
	}

	led3 = open("/sys/class/gpio/gpio18/direction", O_WRONLY);
	if (led3 == -1) {
		perror("Unable to open /sys/class/gpio/gpio18/direction");
		exit(1);
	}

	if (write(led3, "out", 3) != 3) {
		perror("Error writing to /sys/class/gpio/gpio18/direction");
		exit(1);
	}

	led4 = open("/sys/class/gpio/gpio19/direction", O_WRONLY);
	if (led4 == -1) {
		perror("Unable to open /sys/class/gpio/gpio19/direction");
		exit(1);
	}

	if (write(led4, "out", 3) != 3) {
		perror("Error writing to /sys/class/gpio/gpio19/direction");
		exit(1);
	}
	close(led1);
	close(led2);
	close(led3);
	close(led4);
	while(1){
		printf("\n\t# Choose one of  the following options:\n\tEnter 1 to blink LEDs 1, 2, 3 and 4\n\tPress 'Ctrl+c' to Exit\n");
		scanf("%c%*c",&option);
		if(option=='1'){
			led1 = open("/sys/class/gpio/gpio16/value", O_WRONLY);
			if (led1 == -1) {
				perror("Unable to open /sys/class/gpio/gpio16/value");
				exit(1);
			}
			led2 = open("/sys/class/gpio/gpio17/value", O_WRONLY);
			if (led2 == -1) {
				perror("Unable to open /sys/class/gpio/gpio17/value");
				exit(1);
			}
		led3 = open("/sys/class/gpio/gpio18/value", O_WRONLY);
			if (led3 == -1) {
				perror("Unable to open /sys/class/gpio/gpio18/value");
				exit(1);
			}
			led4 = open("/sys/class/gpio/gpio19/value", O_WRONLY);
			if (led2 == -1) {
				perror("Unable to open /sys/class/gpio/gpio19/value");
				exit(1);
			}
		printf("\n\t LEDs are blinking press 'Ctrl+c' to Exit\n");
		while(1){
			if (write(led1, "1", 1) != 1) {
				perror("Error writing to /sys/class/gpio/gpio16/value");
				exit(1);
			}

			if (write(led2, "1", 1) != 1) {
				perror("Error writing to /sys/class/gpio/gpio17/value");
				exit(1);
			}
			if (write(led3, "1", 1) != 1) {
				perror("Error writing to /sys/class/gpio/gpio18/value");
				exit(1);
			}
			if (write(led4, "1", 1) != 1) {
				perror("Error writing to /sys/class/gpio/gpio19/value");
				exit(1);
			}
			sleep(2);
			if (write(led1, "0", 1) != 1) {
				perror("Error writing to /sys/class/gpio/gpio16/value");
				exit(1);
			}

			if (write(led2, "0", 1) != 1) {
				perror("Error writing to /sys/class/gpio/gpio17/value");
				exit(1);
			}
			if (write(led3, "0", 1) != 1) {
				perror("Error writing to /sys/class/gpio/gpio18/value");
				exit(1);
			}
			if (write(led4, "0", 1) != 1) {
				perror("Error writing to /sys/class/gpio/gpio19/value");
				exit(1);
			}
			sleep(2);
		}
		close(led1);
		close(led2);
		close(led3);
		close(led4);
		} 
   }
}
