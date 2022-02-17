require recipes-kernel/linux/mpfs-linux-common.inc

LINUX_VERSION ?= "5.12.1"
KERNEL_VERSION_SANITY_SKIP="1"

BRANCH = "mpfs-linux-5.12.x"
SRCREV = "05e2f4d5f13c29b8e73a2cdd7e682f28a51962c2"
SRC_URI = " \
    git://github.com/polarfire-soc/linux.git;branch=${BRANCH} \
"

SRC_URI_append_icicle-kit-es = " \
    file://icicle-kit-es-microchip.dts \
 "

SRC_URI_append_icicle-kit-es-amp = " \
    file://icicle-kit-es-microchip-context-a.dts \
 "

do_configure_prepend_icicle-kit-es() {
    cp -f ${WORKDIR}/icicle-kit-es-microchip.dts ${S}/arch/riscv/boot/dts/microchip/microchip-mpfs-icicle-kit.dts
}

do_configure_prepend_icicle-kit-es-amp() {
    cp -f ${WORKDIR}/icicle-kit-es-microchip-context-a.dts ${S}/arch/riscv/boot/dts/microchip/microchip-mpfs-icicle-kit-context-a.dts
}

SRC_URI_append_icicle-kit-es = " file://defconfig"
SRC_URI_append_icicle-kit-es-amp = " file://defconfig"




