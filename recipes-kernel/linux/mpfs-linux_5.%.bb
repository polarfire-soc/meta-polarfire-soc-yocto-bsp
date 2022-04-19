require recipes-kernel/linux/mpfs-linux-common.inc

LINUX_VERSION ?= "5.12.19"
KERNEL_VERSION_SANITY_SKIP="1"

BRANCH = "mpfs-linux-5.12.x"
SRCREV="402a0b093d31e09d356e938ae48c6567b2432bb2"
SRC_URI = " \
    git://github.com/polarfire-soc/linux.git;protocol=https;branch=${BRANCH} \
"

SRC_URI:append:icicle-kit-es = " file://bsp_cmdline.cfg \
    file://rpi_sense_hat.cfg \
"
SRC_URI:append:icicle-kit-es-amp = " file://bsp_cmdline.cfg \
    file://rpi_sense_hat.cfg \
"

do_deploy:append() {

    if [ -n "${INITRAMFS_IMAGE}" ]; then

        if [ "${INITRAMFS_IMAGE_BUNDLE}" != "1" ]; then
                ln -snf fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}${KERNEL_FIT_BIN_EXT} "$deployDir/fitImage"
        fi
    fi
}

addtask deploy after do_install

