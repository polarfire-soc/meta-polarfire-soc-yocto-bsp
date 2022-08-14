# UBIFS for Mikroe FLash 5 Click (128 MB)
MKUBIFS_ARGS ?= " -e 126976 -c 926 -m 2048 -x zlib -F"
UBINIZE_ARGS ?= " -m 2048 -p 128KiB -s 2048"
MKENVIMAGE_EXTRA_ARGS = " -r "

MPFS_MTDPARTS = "spi2.0:2m(payload),128k(env),119m(rootfs)"
MPFS_IS_ON_MTDID = "spi2.0"
MPFS_MTD_TYPE = "nand"
UBI_VOLNAME = "rootfs"
