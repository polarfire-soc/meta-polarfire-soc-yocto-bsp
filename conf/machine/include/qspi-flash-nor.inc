# UBIFS for PMOD SF3 NOR FLash + Pmod HAT Adapter (32 MB)
MKUBIFS_ARGS ?= " -e 65408 -c 422 -m 1 -x zlib"
UBINIZE_ARGS ?= " -m 1 -p 64KiB"
MKENVIMAGE_EXTRA_ARGS = " -r "

MPFS_MTDPARTS = "spi2.0:2m(payload),128k(env),28m(rootfs)"
MPFS_IS_ON_MTDID = "spi2.0"
MPFS_MTD_TYPE = "nor"
UBI_VOLNAME = "rootfs"
