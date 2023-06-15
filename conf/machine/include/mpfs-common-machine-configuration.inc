#@TYPE: Machine Include
#@SOC: Polarfire SoC
#@DESCRIPTION: Common Machine Configuration for Polarfire SoC

require conf/machine/include/riscv/tune-riscv.inc

MACHINE_FEATURES = "screen keyboard ext2 ext3 serial"

KERNEL_CLASSES = "kernel-fitimage"
KERNEL_IMAGETYPE = "fitImage"
EXTERNAL_KERNEL_DEVICETREE = "${DEPLOY_DIR_IMAGE}/overlays"

KBUILD_DEFCONFIG ?= "mpfs_defconfig"

PREFERRED_PROVIDER_virtual/kernel ?= "mpfs-linux"
PREFERRED_VERSION_openocd-native = "riscv"
PREFERRED_VERSION_openocd = "riscv"
PREFERRED_PROVIDER_virtual/bootloader = "u-boot-mpfs"
PREFERRED_PROVIDER_u-boot = "u-boot-mpfs"

UBOOT_ENTRYPOINT = "0x80200000"
UBOOT_DTB_LOADADDRESS = "0x8a000000"
UBOOT_DTBO_LOADADDRESS = "0x8a080000"
UBOOT_RD_LOADADDRESS = "0x8aa00000"
UBOOT_RD_ENTRYPOINT = "0x8aa00000"

MACHINE_EXTRA_RRECOMMENDS += " kernel-modules"

IMAGE_FSTYPES += "wic wic.gz wic.bmap ext4"

## Do not update fstab file when using wic images
WIC_CREATE_EXTRA_ARGS ?= "--no-fstab-update"

## wic default support
WKS_FILE_DEPENDS ?= " \
    e2fsprogs-native \
    bmap-tools-native \
    u-boot-mpfs \
"
