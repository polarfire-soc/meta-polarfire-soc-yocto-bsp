#include <stdio.h>
#include <stdlib.h>

static void display_output(char *data, unsigned int byte_length)
{
    unsigned int inc;
    char byte = 0;
    char buffer[2048];
	char *bufferp = buffer;

	for (inc = 0; inc < byte_length; ++inc)
	{
		if(inc%32 == 0 && inc != 0)
		{
			bufferp += sprintf(bufferp, "\r\n");
		}
		byte = data[inc];
		bufferp += sprintf(bufferp, "%02x", byte);
    }

    printf("%s\n", buffer);
}

int main()
{
    char c[4096];
    char chr;
    FILE *fptr;

    for (; /*ever*/;)
    {
        printf("PFSoC system services test program.\r\nPress:\r\n");
        printf("a - to show the serial number\r\nb - to show the fpga digest\r\nc - to cat hwrng, until ctrl+c\r\ne - to exit this program\r\n");
        chr = getchar();
        getchar();

        switch (chr)
        {
        case 'a':
            if ((fptr = fopen("/dev/mpfs_serial_num", "r")) == NULL)
            {
                printf("Error! opening file");
                exit(1);
            }
            fscanf(fptr, "%[^\n]", c);
            printf("Icicle kit serial number: %s\n", c);
            fclose(fptr);
            break;
        case 'b':
            if ((fptr = fopen("/dev/mpfs_fpga_digest", "r")) == NULL)
            {
                printf("Error! opening file");
                exit(1);
            }
            printf("pfsoc fpga digest:\n", c);
            while (fread(c, 66, 1, fptr) == 1)
            {
                printf("%s", c);
            }
            if (feof(fptr))
            {
                printf("%s\n", c);
            }
            fclose(fptr);
            break;
        case 'c':
            if ((fptr = fopen("/dev/hwrng", "r")) == NULL)
            {
                printf("Error! opening file");
                exit(1);
            }
            for (;/*ever*/;)
            {
                fread(c, 32, 1, fptr);
                display_output(c, 32);
            }
            fclose(fptr);
            break;
        case 'd':
            if ((fptr = fopen("/dev/mpfs_fpga_digest", "r")) == NULL)
            {
                printf("Error! opening file");
                exit(1);
            }
            fseek(fptr, 50, SEEK_SET);
            fscanf(fptr, "%[^\n]", c);
            printf("Icicle kit serial number: %s\n", c);
            fclose(fptr);
            break;
        case 'e':
            return 0;

        default:
            break;
        }
    }
    return 0;
}
