FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " "

SRC_URI:append:icicle-kit-es = "file://0001-U-Boot-v2022.01-with-high-memory-0x1000000000.patch \
                                file://0002-spi-Add-Microchip-PolarFire-SoC-QSPI-driver.patch \
                                file://0003-spi-pt-2-add-Microchip-PolarFire-SoC-QSPI-support.patch \
                                file://0004-icicle-enable-overlay-support-in-defconfig.patch \
                                file://${UBOOT_ENV}.txt \
                                file://${HSS_PAYLOAD}.yaml \
                               "

SRC_URI:append:icicle-kit-es-amp = "file://0001-U-Boot-v2022.01-with-high-memory-0x1000000000.patch \
                                    file://0002-riscv-icicle-kit-change-to-amp-dts.patch \
                                    file://0002-spi-Add-Microchip-PolarFire-SoC-QSPI-driver.patch \
                                    file://0003-spi-pt-2-add-Microchip-PolarFire-SoC-QSPI-support.patch \
                                    file://0004-icicle-enable-overlay-support-in-defconfig.patch \
                                    file://${UBOOT_ENV}.txt \
                                    file://${HSS_PAYLOAD}.yaml \
                                   "

SRC_URI:append:m100pfsevp = "file://0001-Aries-board-m100pfsevp-u-boot-support.patch \
                             file://${HSS_PAYLOAD}.yaml \
                            "

DEPENDS:append = " u-boot-tools-native hss-payload-generator-native"
DEPENDS:append:icicle-kit-es-amp = " polarfire-soc-amp-examples"

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
       rm -f ${DEPLOYDIR}/boot.scr.uimg
       install -m 755 ${WORKDIR}/boot.scr.uimg ${DEPLOYDIR}
    fi

    #
    # for icicle-kit-es-amp, we'll already have an amp-application.elf in
    # DEPLOY_DIR_IMAGE, so smuggle it in here for the payload generator ...
    #
    if [ -f "${DEPLOY_DIR_IMAGE}/amp-application.elf" ]; then
        cp -f ${DEPLOY_DIR_IMAGE}/amp-application.elf ${DEPLOYDIR}
    fi

    cd ${DEPLOYDIR}
    hss-payload-generator -c ${WORKDIR}/${HSS_PAYLOAD}.yaml -v ${DEPLOYDIR}/payload.bin

    #
    # for icicle-kit-es-amp, if we smuggled in an amp-application.elf, then
    # clean-up here before the Yocto framework gets angry that we're trying to install
    # files (from DEPLOYDIR) into a shared area (DEPLOY_IMAGE_DIR) when they already
    # exist
    #
    if [ -f "${DEPLOYDIR}/amp-application.elf" ]; then
        rm -f ${DEPLOYDIR}/amp-application.elf
    fi
}

FILES:${PN} += "/boot/boot.scr.uimg"
