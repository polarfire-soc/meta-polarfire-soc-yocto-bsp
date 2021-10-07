require recipes-kernel/linux/mpfs-linux-common.inc

LINUX_VERSION ?= "5.12.1"
KERNEL_VERSION_SANITY_SKIP="1"

BRANCH = "mpfs-linux-5.12.x"
SRCREV = "${AUTOREV}"
SRC_URI = " \
    git://git@bitbucket.microchip.com/fpga_pfsoc_es/linux.git;branch=${BRANCH};protocol=ssh; \
"

SRC_URI_append_icicle-kit-es = " file://defconfig"
SRC_URI_append_icicle-kit-es-amp = " file://defconfig"




