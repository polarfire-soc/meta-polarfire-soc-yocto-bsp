FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS_append = " u-boot-tools-native"

SRC_URI_append_mpfs = " \
            file://${UBOOT_ENV}.txt \
            file://mpfs_defconfig\
           "
SRC_URI_append_lc-mpfs = " \
            file://${UBOOT_ENV}.txt \
            file://mpfs_defconfig\
           "

SRC_URI_append_icicle-kit-es = " \
            file://${UBOOT_ENV}.txt \
            file://0001-riscv-Add-DMA-64-bit-address-support.patch \
	    file://0002-net-macb-Add-DMA-64-bit-address-support-for-macb.patch \
	    file://0003-clk-Add-Microchip-PolarFire-SoC-clock-driver.patch \
	    file://0004-riscv-dts-Add-device-tree-for-Microchip-Icicle-Kit.patch \
	    file://0005-riscv-Add-Microchip-MPFS-Icicle-Kit-support.patch \
           "

SRC_URI_append_icicle-kit-es-sd = " \
            file://${UBOOT_ENV}.txt \
            file://0001-riscv-Add-DMA-64-bit-address-support.patch \
	    file://0002-net-macb-Add-DMA-64-bit-address-support-for-macb.patch \
	    file://0003-clk-Add-Microchip-PolarFire-SoC-clock-driver.patch \
	    file://0004-riscv-dts-Add-device-tree-for-Microchip-Icicle-Kit.patch \
	    file://0005-riscv-Add-Microchip-MPFS-Icicle-Kit-support.patch \
           "
# Overwrite this for your server
TFTP_SERVER_IP ?= "127.0.0.1"

do_configure_prepend_mpfs() {
    cp -f ${WORKDIR}/mpfs_defconfig ${S}/configs
    sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/${UBOOT_ENV}.txt

    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        mkimage -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/boot.scr.uimg
    fi
}

do_configure_prepend_lc-mpfs() {
    cp -f ${WORKDIR}/mpfs_defconfig ${S}/configs
    sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/${UBOOT_ENV}.txt

    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        mkimage -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/boot.scr.uimg
    fi
}

do_configure_prepend_icicle-kit-es() {
    sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/${UBOOT_ENV}.txt
    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        mkimage -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/boot.scr.uimg
    fi
}

do_configure_prepend_icicle-kit-es-sd() {
    sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/${UBOOT_ENV}.txt
    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        mkimage -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/boot.scr.uimg
    fi
}

do_deploy_append_mpfs() {
    install -d ${DEPLOY_DIR_IMAGE}
    install -m 755 ${WORKDIR}/boot.scr.uimg ${DEPLOY_DIR_IMAGE}
}
do_deploy_append_lc-mpfs() {
    install -d ${DEPLOY_DIR_IMAGE}
    install -m 755 ${WORKDIR}/boot.scr.uimg ${DEPLOY_DIR_IMAGE}
}
do_deploy_append_icicle-kit-es-sd() {
    install -d ${DEPLOY_DIR_IMAGE}
    install -m 755 ${WORKDIR}/boot.scr.uimg ${DEPLOY_DIR_IMAGE}
}
do_deploy_append_icicle-kit-es() {
    install -d ${DEPLOY_DIR_IMAGE}
    install -m 755 ${WORKDIR}/boot.scr.uimg ${DEPLOY_DIR_IMAGE}
}

FILES_${PN}_append = " /boot/boot.scr.uimg"
