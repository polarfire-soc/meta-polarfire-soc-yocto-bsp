DESCRIPTION = "Minimal image with minimal set of tools and packages \
to run MPFS application demos. SUitable for initramfs and MTD (Flash) \
images"

DEPENDS += "virtual/bootloader"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-base \
    polarfire-soc-linux-examples \
    python3 \
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

# do mtd last
IMAGE_TYPEDEP:mtd += "${IMAGE_FSTYPES}"
IMAGE_TYPEDEP:mtd:remove = "mtd"

INITRAMFS_MAXSIZE = "262144"

inherit core-image
