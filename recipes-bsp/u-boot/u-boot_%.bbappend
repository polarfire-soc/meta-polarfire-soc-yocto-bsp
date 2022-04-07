FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " "

SRC_URI:append:icicle-kit-es = "file://0001-U-Boot-v2022.01-with-high-memory-0x1000000000.patch \
                                file://0002-spi-Add-Microchip-PolarFire-SoC-QSPI-driver.patch \
                                file://0003-spi-pt-2-add-Microchip-PolarFire-SoC-QSPI-support.patch \
                                file://${UBOOT_ENV}.txt \
"

SRC_URI:append:icicle-kit-es-amp = "file://0002-riscv-icicle-kit-change-to-amp-dts.patch \
                                    file://0001-U-Boot-v2022.01-with-high-memory-0x1000000000.patch \
                                    file://0002-spi-Add-Microchip-PolarFire-SoC-QSPI-driver.patch \
                                    file://0003-spi-pt-2-add-Microchip-PolarFire-SoC-QSPI-support.patch \
                                    file://${UBOOT_ENV}.txt \
                                   "

SRC_URI:append:m100pfsevp = "file://0001-Aries-board-m100pfsevp-u-boot-support.patch"

DEPENDS:append = " u-boot-tools-native"


# Overwrite this for your server
TFTP_SERVER_IP ?= "127.0.0.1"

do_configure:prepend () {
    
    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/${UBOOT_ENV}.txt
        mkimage -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/boot.scr.uimg
    fi    
}

do_deploy:append () {
    if [ -f "${WORKDIR}/boot.scr.uimg" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 755 ${WORKDIR}/boot.scr.uimg ${DEPLOY_DIR_IMAGE}
    fi

}

FILES:${PN} += "/boot/boot.scr.uimg"
