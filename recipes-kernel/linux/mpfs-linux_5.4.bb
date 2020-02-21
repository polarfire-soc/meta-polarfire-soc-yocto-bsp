require recipes-kernel/linux/linux-common.inc

LINUX_VERSION ?= "5.4.x"
KERNEL_VERSION_SANITY_SKIP="1"

BRANCH = "linux-5.4.y"
SRCREV = "${AUTOREV}"
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git;branch=${BRANCH} \
           file://0001-Microchip-GPIO-Support-for-the-Polarfire-SoC.patch \
           file://0002-Microchip-SPI-Support-for-the-Polarfire-SoC.patch \
           file://0003-Microchip-Adding-I2C-Support-for-the-Polarfire-SoC.patch \
           file://0004-Microchip-Adding-QSPI-driver-for-Polarfire-SoC.patch \
           file://0005-Microchip-usb-musb-support-for-the-Polarfire-SoC.patch \
           file://0006-Microchip-UIO-CAN-support-for-the-Polarfire-SoC.patch \
           file://0001-include-uio_driver.h-changes.patch \
           file://0003-HACK-Revert-of-device-Really-only-set-bus-DMA-mask-w.patch \
           "

SRC_URI_append_mpfs = " \
    file://mpfs.dts \
    file://extra.cfg \
    file://0001-PCI-microchip-Add-host-driver-for-Microchip-PCIe-con.patch \
"

SRC_URI_append_lc-mpfs = " \
    file://lc-mpfs.dts \
    file://extra.cfg \
"


do_compile_append() {

# Overwriting device-tree sources
#
   cp -f ${WORKDIR}/${MACHINE}.dts ${S}/arch/${ARCH}/boot/dts
# Build the custom device-tree
#
   make ${RISCV_SBI_FDT}
}


do_deploy_append() {

# Deploy device-tree
#
   install -d ${DEPLOY_DIR_IMAGE}
   install -m 0644 ${B}/arch/${ARCH}/boot/dts/${RISCV_SBI_FDT} ${DEPLOY_DIR_IMAGE}/${RISCV_SBI_FDT}

}
