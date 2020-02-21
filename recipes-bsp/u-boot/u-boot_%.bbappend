FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " \
            file://tftp-mmc-boot.txt \
           "
DEPENDS_append = " u-boot-tools-native"

# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "721d6b594be4dc2d13b61f6afee9e437278d3ddd"

SRC_URI = "git://git.denx.de/u-boot.git \
	   file://mpfs_defconfig\
           file://tftp-mmc-boot.txt \
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

FILES_${PN}_append_lc-mpfs = " /boot/boot.scr.uimg"
FILES_${PN}_append_mpfs = " /boot/boot.scr.uimg"
