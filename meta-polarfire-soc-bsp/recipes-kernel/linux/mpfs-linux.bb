require recipes-kernel/linux/mpfs-linux-common.inc

LINUX_VERSION ?= "6.1"
KERNEL_VERSION_SANITY_SKIP="1"

SRCREV="060758d16686da594dcef5d5ecf33c5328736857"
SRC_URI = " \
    git://github.com/linux4microchip/linux.git;protocol=https;nobranch=1 \
    file://0001-riscv-dts-microchip-add-mpfs-disco-kit-context-a.dts.patch \
"

do_assemble_fitimage[depends] = "${@'dt-overlay-mchp:do_deploy' \
                                  if "icicle-kit" in d.getVar('MACHINE') \
                                  or "mpfs-video-kit" in d.getVar('MACHINE') \
                                  or "mpfs-disco-kit" in d.getVar('MACHINE') \
                                  else ''}"

SRC_URI:append:icicle-kit = " file://mpfs_cmdline.cfg \
                              file://qspi_flash.cfg \
                              file://rpi_sense_hat.cfg \
                              file://mcp23s08_spi.cfg \
                            "

SRC_URI:append:icicle-kit-es-amp = "file://mpfs_amp_cmdline.cfg \
                                    file://qspi_flash.cfg \
                                    file://rpi_sense_hat.cfg \
                                   "

SRC_URI:append:mpfs-video-kit = " file://mpfs_cmdline.cfg \
                                  file://mpfs-v4l2.cfg \
                                  file://mpfs_crypto.cfg \
                                "
SRC_URI:append:mpfs-disco-kit =  " file://mpfs_cmdline.cfg "

SRC_URI:append:mpfs-disco-kit-amp =  " file://mpfs_amp_cmdline.cfg "

do_deploy:append() {

    if [ -n "${INITRAMFS_IMAGE}" ]; then

        if [ "${INITRAMFS_IMAGE_BUNDLE}" != "1" ]; then
                ln -snf fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}${KERNEL_FIT_BIN_EXT} "$deployDir/fitImage"
        fi
    fi
}

addtask deploy after do_install

