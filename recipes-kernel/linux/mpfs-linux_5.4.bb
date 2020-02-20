require recipes-kernel/linux/linux-common.inc

LINUX_VERSION ?= "5.4.x"
KERNEL_VERSION_SANITY_SKIP="1"

BRANCH = "linux-5.4.y"
SRCREV = "${AUTOREV}"
SRC_URI = " \
    git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git;branch=${BRANCH} \
"

##    Not using at the moment
#    file://0001-PCI-microsemi-Add-host-driver-for-Microsemi-PCIe-con.patch
#    file://0002-Microsemi-PCIe-expansion-board-DT-entry.patch 

SRC_URI_append_mpfs = " \
    file://mpfs.dts \
    file://extra.cfg \
    file://0001-PCI-microsemi-Add-host-driver-for-Microsemi-PCIe-con.patch \
    file://0003-HACK-Revert-of-device-Really-only-set-bus-DMA-mask-w.patch \
	file://0001-include-uio_driver.h-changes.patch \
"

SRC_URI_append_lc-mpfs = " \
    file://lc-mpfs.dts \
    file://extra.cfg \
    file://0003-HACK-Revert-of-device-Really-only-set-bus-DMA-mask-w.patch \
"


do_compile_append_mpfs() {

# Overwriting device-tree sources
#
cp -f ${WORKDIR}/mpfs.dts ${S}/arch/${ARCH}/boot/dts


# Build the custom device-tree
#
make ${RISCV_SBI_FDT}
}

do_compile_append_lc-mpfs() {

# Overwriting device-tree sources
#
cp -f ${WORKDIR}/lc-mpfs.dts ${S}/arch/${ARCH}/boot/dts


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
