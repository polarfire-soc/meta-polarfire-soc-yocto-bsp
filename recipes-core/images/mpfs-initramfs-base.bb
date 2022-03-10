DESCRIPTION = "Creates a cpio archive to be used by \
a RAM-based Root Filesystem (initramfs)"

INITRAMFS_SCRIPTS ?= "\
		initramfs-framework-base \
		initramfs-module-udev \
                "

PACKAGE_INSTALL = "\
    polarfire-soc-linux-examples \
    busybox\
    dt-overlay-mchp \
    udev \
    python3-flask \
    rng-tools \
    pciutils \
    wget \
    i2c-tools \
    libgpiod \
    util-linux \
    libgpiod-tools \
    kernel-modules \
"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = "debug-tweaks"

export IMAGE_BASENAME = "mpfs-initramfs-base"
IMAGE_NAME_SUFFIX ?= ""
IMAGE_LINGUAS = ""

LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

INITRAMFS_MAXSIZE = "262144"

inherit core-image

python do_check_initramfs_image () {
    initramfs_image = d.getVar("INITRAMFS_IMAGE", True)

    if initramfs_image is None or initramfs_image != "mpfs-initramfs-base":
        bb.error("INITRAMFS_IMAGE = \"mpfs-initramfs-base\" not set")
        bb.fatal("Use bitbake -R conf/initramfs.conf mpfs-initramfs-image to build an Initramfs WIC image ")
}
addtask check_initramfs_image before do_rootfs
