require recipes-kernel/linux/mpfs-linux-common.inc

LINUX_VERSION ?= "5.15"
KERNEL_VERSION_SANITY_SKIP="1"

BRANCH = "linux-5.15-mchp+fpga"
SRCREV="5566e0a6522ff3c236d73140370d80fec12a636a"
SRC_URI = " \
    git://github.com/linux4microchip/linux.git;protocol=https;branch=${BRANCH} \
"

do_assemble_fitimage[depends] += "dt-overlay-mchp:do_deploy"

SRC_URI:append:icicle-kit-es = " file://bsp_cmdline.cfg \
    file://rpi_sense_hat.cfg \
    file://qspi_flash.cfg \
"
SRC_URI:append:icicle-kit-es-amp = " file://bsp_cmdline.cfg \
    file://rpi_sense_hat.cfg \
    file://qspi_flash.cfg \
"

SRC_URI:append:m100pfsevp = "file://m100pfsevp_configs.cfg"

SRC_URI:append:mpfs-video-kit = " \
    file://bsp_cmdline.cfg \
    file://mpfs-v4l2.cfg \
"

do_deploy:append() {

    if [ -n "${INITRAMFS_IMAGE}" ]; then

        if [ "${INITRAMFS_IMAGE_BUNDLE}" != "1" ]; then
                ln -snf fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}${KERNEL_FIT_BIN_EXT} "$deployDir/fitImage"
        fi
    fi
}

addtask deploy after do_install

