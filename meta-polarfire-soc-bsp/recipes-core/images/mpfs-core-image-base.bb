DESCRIPTION = "Minimal image with minimal set of tools and packages \
to run MPFS application demos. SUitable for initramfs and MTD (Flash) \
images"

DEPENDS += "virtual/bootloader"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    polarfire-soc-linux-examples \
    dt-overlay-mchp \
    python3 \
    python3-flask \
    rng-tools \
    i2c-tools \
    libgpiod \
    libgpiod-tools \
    util-linux \
    wget \
    kernel-modules \
    "

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = "debug-tweaks"

export IMAGE_BASENAME = "mpfs-core-image-base"
IMAGE_NAME_SUFFIX ?= ""
IMAGE_LINGUAS = ""

LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

IMAGE_FSTYPES:append:icicle-kit-es = "${@bb.utils.contains('INITRAMFS_IMAGE', 'mpfs-core-image-base', \
' ', ' mtd ubi ubifs', d)}"

IMAGE_FSTYPES:append:icicle-kit-es-amp = "${@bb.utils.contains('INITRAMFS_IMAGE', 'mpfs-core-image-base', \
' ', ' mtd ubi ubifs', d)}"

# do mtd last
IMAGE_TYPEDEP:mtd += "${IMAGE_FSTYPES}"
IMAGE_TYPEDEP:mtd:remove = "mtd"

INITRAMFS_MAXSIZE = "262144"

inherit core-image
