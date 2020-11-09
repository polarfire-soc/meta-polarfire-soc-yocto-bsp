require recipes-kernel/linux/mpfs-linux-common.inc

LINUX_VERSION ?= "5.6.x"
KERNEL_VERSION_SANITY_SKIP="1"

BRANCH = "linux-5.6.y"
SRCREV = "v5.6.16"
SRC_URI = " \
    git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git;branch=${BRANCH} \
"

SRC_URI_append_icicle-kit-es = " \
    file://icicle-kit-es-a000-microchip.dts \
    file://0001-PFSoC-Icicle-kit-Adding-DTS-makefile.patch \
    file://0001-Microchip-Polarfire-SoC-Clock-Driver.patch \
    file://0002-PFSoC-MAC-Interface-auto-negotiation.patch \
    file://0001-Microchip-GPIO-Support-for-the-Polarfire-SoC.patch \
    file://0003-Microchip-Adding-I2C-Support-for-the-Polarfire-SoC.patch \
    file://0008-pac139x.patch \
    file://v11-0004-PCI-microchip-Add-host-driver-for-Microchip-PCIe.patch \
    file://v1-0002-Add-definition-for-Microchip-PolarFire-SoC.patch \
    file://v1-0001-dt-bindings-CLK-microchip-Add-Microchip-PolarFire.patch \
 "
SRC_URI_append_icicle-kit-es-sd = " \
    file://icicle-kit-es-a000-microchip.dts \
    file://0001-PFSoC-Icicle-kit-Adding-DTS-makefile.patch \
    file://0001-Microchip-Polarfire-SoC-Clock-Driver.patch \
    file://0002-PFSoC-MAC-Interface-auto-negotiation.patch \
    file://0001-Microchip-GPIO-Support-for-the-Polarfire-SoC.patch \
    file://0003-Microchip-Adding-I2C-Support-for-the-Polarfire-SoC.patch \
    file://0008-pac139x.patch \
    file://v11-0004-PCI-microchip-Add-host-driver-for-Microchip-PCIe.patch \
    file://v1-0002-Add-definition-for-Microchip-PolarFire-SoC.patch \
    file://v1-0001-dt-bindings-CLK-microchip-Add-Microchip-PolarFire.patch \
 "
 
SRC_URI_append_mpfs = " \
    file://mpfs.dts \
    file://0004-SiFive-Unleashed-CPUFreq.patch \
    file://0007-Add-PWM-LEDs-D1-D2-D3-D4.patch \
    file://riscv-add-support-to-determine-no-of-L2-cache-way-enabled.patch \
    file://0001-Polarfire-SoC-DTS-support.patch \
    file://v11-0003-PCI-microchip-Add-host-driver-for-Microchip-PCIe.patch \
    file://v11-0004-PCI-microchip-Add-host-driver-for-Microchip-PCIe.patch \
    file://0001-Microchip-GPIO-Support-for-the-Polarfire-SoC.patch \
    file://0002-Microchip-SPI-Support-for-the-Polarfire-SoC.patch \
    file://0003-Microchip-Adding-I2C-Support-for-the-Polarfire-SoC.patch \
    file://0004-Microchip-Adding-QSPI-driver-for-Polarfire-SoC.patch \
    file://0006-Microchip-UIO-CAN-support-for-the-Polarfire-SoC.patch \
"

SRC_URI_append_lc-mpfs = " \
    file://mpfs.dts \
    file://0004-SiFive-Unleashed-CPUFreq.patch \
    file://0007-Add-PWM-LEDs-D1-D2-D3-D4.patch \
    file://riscv-add-support-to-determine-no-of-L2-cache-way-enabled.patch \
    file://0001-Polarfire-SoC-DTS-support.patch \
    file://0001-Microchip-GPIO-Support-for-the-Polarfire-SoC.patch \
    file://0002-Microchip-SPI-Support-for-the-Polarfire-SoC.patch \
    file://0003-Microchip-Adding-I2C-Support-for-the-Polarfire-SoC.patch \
    file://0004-Microchip-Adding-QSPI-driver-for-Polarfire-SoC.patch \
    file://0006-Microchip-UIO-CAN-support-for-the-Polarfire-SoC.patch \
"

do_configure_prepend_icicle-kit-es() {
    cp -f ${WORKDIR}/icicle-kit-es-a000-microchip.dts ${S}/arch/riscv/boot/dts/microchip
}
do_configure_prepend_icicle-kit-es-sd() {
    cp -f ${WORKDIR}/icicle-kit-es-a000-microchip.dts ${S}/arch/riscv/boot/dts/microchip
}

do_configure_prepend_mpfs() {
    cp -f ${WORKDIR}/mpfs.dts ${S}/arch/riscv/boot/dts/sifive
}
do_configure_prepend_lc-mpfs() {
    cp -f ${WORKDIR}/mpfs.dts ${S}/arch/riscv/boot/dts/sifive
}

SRC_URI_append_icicle-kit-es = " file://defconfig"
SRC_URI_append_icicle-kit-es-sd = " file://defconfig"
SRC_URI_append_mpfs = " file://defconfig"
SRC_URI_append_lc-mpfs = " file://defconfig"


