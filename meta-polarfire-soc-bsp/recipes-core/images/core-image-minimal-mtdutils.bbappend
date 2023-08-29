# do mtd last
IMAGE_TYPEDEP:mtd += "${IMAGE_FSTYPES}"
IMAGE_TYPEDEP:mtd:remove = "mtd"

IMAGE_INSTALL += "kernel"

EXTRA_IMAGE_FEATURES:remove = "ssh-server-dropbear package-management"
