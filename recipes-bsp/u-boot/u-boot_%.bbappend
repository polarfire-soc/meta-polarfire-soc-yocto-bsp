FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
            file://tftp-mmc-boot.txt \
            file://v0001-1-5-riscv-dts-Split-device-tree.patch \
            file://v0001-2-5-riscv-update-Microchip-MPFS-Icicle-Kit-support.patch \
            file://v0001-3-5-i2c-Add-Microchip-PolarFire-SoC-i2c-driver.patch \
            file://v0001-4-5-net-macb-Compatible-as-per-device-tree.patch \
            file://v0001-5-5-doc-board-Update-Microchip-MPFS-Icicle-Kit-doc.patch \
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

}


FILES:${PN} += "/boot/boot.scr.uimg"
