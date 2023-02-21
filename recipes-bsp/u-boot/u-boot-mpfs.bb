require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

BRANCH = "mpfs-uboot-2022.01"
SRCREV="6f9f4791d2ce0793c3cb27e4a0c07d352e069171"
SRC_URI = "git://github.com/polarfire-soc/u-boot.git;protocol=https;branch=${BRANCH}  \
           file://${UBOOT_ENV}.txt \
           file://${HSS_PAYLOAD}.yaml \
          "

# Aries m100pfsevp machine uses built-in U-boot env
SRC_URI:remove:m100pfsevp = "file://${UBOOT_ENV}.txt"

DEPENDS:append = " u-boot-tools-native hss-payload-generator-native"
DEPENDS:append:icicle-kit-es-amp = " polarfire-soc-amp-examples"

# Overwrite this for your server
TFTP_SERVER_IP ?= "127.0.0.1"

do_configure:prepend () {

    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        cp ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/${UBOOT_ENV}.txt.pp
        sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/${UBOOT_ENV}.txt.pp
        sed -i -e 's/@MTDPARTS@/${MPFS_MTDPARTS}/gI' ${WORKDIR}/${UBOOT_ENV}.txt.pp
        sed -i -e 's/@MTDTYPE@/${MPFS_MTD_TYPE}/gI' ${WORKDIR}/${UBOOT_ENV}.txt.pp
        mkimage -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt.pp ${WORKDIR}/boot.scr.uimg
    fi
}

do_install:append() {
    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        install -m 644 ${WORKDIR}/${UBOOT_ENV_BINARY}.pp ${D}/boot/${UBOOT_ENV_IMAGE}
        ln -sf ${UBOOT_ENV_IMAGE} ${D}/boot/${UBOOT_ENV_BINARY}
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

FILES:${PN}:append = " /boot/boot.scr.uimg"

COMPATIBLE_MACHINE = "(icicle-kit-es|icicle-kit-es-amp|mpfs-video-kit|m100pfsevp)"
