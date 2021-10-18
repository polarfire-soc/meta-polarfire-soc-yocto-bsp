FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
            file://mmc-tftp.txt \
            file://v0001_1_5_riscv:_dts:_Split_device_tree.patch \
            file://v0001_2_5_riscv:_update_Microchip_MPFS_Icicle_Kit_support.patch \
            file://v0001_3_5_i2c:_Add_Microchip_PolarFire_SoC_i2c_driver.patch \
            file://v0001_4_5_net:_macb:_Compatible_as_per_device_tree.patch \
            file://v0001_5_5_doc:_board:_Update_Microchip_MPFS_Icicle_Kit_doc.patch \
           "
SRC_URI:append:icicle-kit-es-amp = "file://microchip-mpfs-icicle-kit-context-a.dts"

DEPENDS:append = " u-boot-tools-native"


# Overwrite this for your server
TFTP_SERVER_IP ?= "127.0.0.1"

do_configure:prepend:icicle-kit-es() {
    sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/tftp-mmc-boot.txt

    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        mkimage -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/boot.scr.uimg
    fi
}
do_configure:prepend:icicle-kit-es-amp() {
    cp -f ${WORKDIR}/microchip-mpfs-icicle-kit-context-a.dts ${S}/arch/riscv/dts/microchip-mpfs-icicle-kit.dts
    sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/tftp-mmc-boot.txt

    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        mkimage -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/boot.scr.uimg
    fi
}

do_deploy:append() {
    if [ -f "${WORKDIR}/boot.scr.uimg" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 755 ${WORKDIR}/boot.scr.uimg ${DEPLOY_DIR_IMAGE}
    fi

    if [ -f "${WORKDIR}/uEnv.txt" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 755 ${WORKDIR}/uEnv.txt ${DEPLOY_DIR_IMAGE}
    fi
}


FILES:${PN}:append:icicle-kit-es = " /boot/boot.scr.uimg
FILES:${PN}:append:icicle-kit-es-amp = " /boot/boot.scr.uimg

