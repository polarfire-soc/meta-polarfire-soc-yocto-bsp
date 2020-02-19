#include <sys/mman.h>
#include <fcntl.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <dirent.h>

#define SOC "/sys/firmware/devicetree/base/soc"
#define MEM "/dev/mem"

int main(int argc, const char** argv) {
  int fd;
  FILE* f;
  DIR *dir;
  struct dirent *dp;
  char path[PATH_MAX];
  char compat[100];
  unsigned long control;
  volatile uint8_t *regs;
  volatile uint64_t *waymask;
  char *extra = "";
  long ways = 0; // a no-op
  int master;

  if (argc == 2) ways = strtol(argv[1], &extra, 0);

  if (argc > 3 || *extra) {
    fprintf(stderr, "Syntax: %s [ways]\n", argv[0]);
    return 1;
  }

  if ((fd = open(MEM, O_RDWR)) == -1) {
    fprintf(stderr, "Can't open " MEM ": %s\n", strerror(errno));
    return 1;
  }

  if ((dir = opendir(SOC)) == 0) {
    fprintf(stderr, "Can't open " SOC ": %s\n", strerror(errno));
    return 1;
  }

  while ((dp = readdir(dir)) != 0) {
    snprintf(path, sizeof(path), SOC "/%s/compatible", dp->d_name);
    if ((f = fopen(path, "r")) == 0) continue;
    fgets(compat, sizeof(compat), f);
    fclose(f);

    // printf("FOUND %s => %s\n", dp->d_name, compat);
    if (strcmp(compat, "sifive,ccache0")) continue;
    control = strtoul(strchr(dp->d_name, '@') + 1, 0, 16);

    // printf("ADDRESS 0x%lx\n", control);
    if ((regs = mmap(0, 4096, PROT_WRITE, MAP_SHARED, fd, control)) == 0) {
      fprintf(stderr, "Can't mmap " MEM ": %s\n", strerror(errno));
      return 1;
    }
    waymask = (uint64_t*)(regs + 0x800);

    printf("Ways previously enabled: %d\n", regs[8] + 1);
    if (ways > 0) {
      regs[8] = ways-1;
      printf("Ways now enabled: %d\n", regs[8] + 1);
    }

    for (master = 0; master < 256; ++master) {
      if (waymask[master] == 0) break; // no more masters
      printf("Master %d mask was: %lx\n", master, waymask[master]);
      if (ways > 0) {
        waymask[master] = (1UL << ways) - 1;
        printf("Master %d mask now: %lx\n", master, waymask[master]);
      }
    }
  }

  return 0;
}
