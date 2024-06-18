DESCRIPTION = "Microchip MPFS Initramfs image \
This recipe wraps an initramfs kernel into a WIC image"

DEPENDS += "mpfs-core-image-base"
do_image_wic[depends] += "mpfs-core-image-base:do_image_complete"

LICENSE = "MIT"

WKS_FILE_DEPENDS:append ?= " \
    mpfs-core-image-base \
"

WKS_FILE = "mpfs-initramfs.wks"

IMAGE_INSTALL = ""

IMAGE_FSTYPES:remove = "ext4"

do_rootfs[depends] += "mpfs-core-image-base:do_image_complete"

inherit core-image

python do_check_initramfs_image () {
    initramfs_image = d.getVar("INITRAMFS_IMAGE", True)

    if initramfs_image is None or initramfs_image != "mpfs-core-image-base":
        bb.error("INITRAMFS_IMAGE = \"mpfs-core-image-base\" not set")
        bb.fatal("Use bitbake -R conf/initramfs.conf mpfs-initramfs-image to build an Initramfs WIC image ")
}
addtask check_initramfs_image before do_rootfs