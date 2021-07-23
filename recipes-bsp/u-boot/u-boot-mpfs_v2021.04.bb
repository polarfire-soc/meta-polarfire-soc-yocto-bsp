require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "git://git.denx.de/u-boot.git \
	       file://${UBOOT_ENV}.txt \
          "
SRC_URI_append_icicle-kit-es = " \
    file://microchip-mpfs-icicle-kit.dts \
"
SRCREV = "b46dd116ce03e235f2a7d4843c6278e1da44b5e1"

DEPENDS_append = " u-boot-tools-native"

# Overwrite this for your server
TFTP_SERVER_IP ?= "127.0.0.1"

do_configure_prepend_icicle-kit-es() {
    cp -f ${WORKDIR}/microchip-mpfs-icicle-kit.dts ${S}/arch/riscv/dts
    sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/${UBOOT_ENV}.txt
    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        mkimage -O linux -T script  -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/boot.scr.uimg
    fi
}

do_deploy_append_icicle-kit-es() {
    if [ -f "${WORKDIR}/boot.scr.uimg" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 755 ${WORKDIR}/boot.scr.uimg ${DEPLOY_DIR_IMAGE}
    fi
}

FILES_${PN}-env += "/boot/boot.scr.uimg"
COMPATIBLE_MACHINE = "(icicle-kit-es)"

TOOLCHAIN = "gcc"
