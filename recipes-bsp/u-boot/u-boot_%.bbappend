FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS_append = " u-boot-tools-native"

SRC_URI_append = " \
            file://tftp-mmc-boot.txt \
            file://mpfs_defconfig\
           "
# Overwrite this for your server
TFTP_SERVER_IP ?= "127.0.0.1"

do_configure_prepend() {
    cp -f ${WORKDIR}/mpfs_defconfig ${S}/configs
    sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/tftp-mmc-boot.txt

    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        mkimage -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/boot.scr.uimg
    fi
}

do_deploy_append() {
    if [ -f "${WORKDIR}/boot.scr.uimg" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 755 ${WORKDIR}/boot.scr.uimg ${DEPLOY_DIR_IMAGE}
    fi

    if [ -f "${WORKDIR}/uEnv.txt" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 755 ${WORKDIR}/uEnv.txt ${DEPLOY_DIR_IMAGE}
    fi
}

FILES_${PN}_append = " /boot/boot.scr.uimg"
