DESCRIPTION = "Microchip MPFS Initramfs image \
This recipe wraps the initramfs kernel into a WIC image"

DEPENDS += "mpfs-initramfs-base"
do_image_wic[depends] += "mpfs-initramfs-base:do_image_complete"

LICENSE = "MIT"

IMAGE_BOOT_FILES = " \
    fitImage \
    boot.scr.uimg \
"

WKS_FILE_DEPENDS:append ?= " \
    mpfs-initramfs-base \
"

WKS_FILE = "mpfs-initramfs.wks"

IMAGE_INSTALL = ""

IMAGE_FSTYPES:append:icicle-kit-es = " mtd"
IMAGE_FSTYPES:append:icicle-kit-es-amp = " mtd"
IMAGE_FSTYPES:remove = " ext4"

do_rootfs[depends] += "mpfs-initramfs-base:do_image_complete"

inherit core-image
