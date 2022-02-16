SUMMARY = "Freedom U540-C000 Bootloader Code"
DESCRIPTION = "Provides booloaders for Aloevera board: \
- Zeroth Stage Boot Loader (ZSBL)\
- First Stage Boot Loader (FSBL)"

LICENSE = "Apache-2.0 | GPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=baa8670511efd7b5f64d27b8ca0aa9cd"

DEPENDS += "dtc-native git-native"

inherit deploy
# strict dependency
do_compile[depends] += "virtual/kernel:do_deploy"

PV = "git${SRCPV}"
BRANCH = "master"
SRCREV = "128f282d177d0e43c9c449fb98462f453cc47258"
SRC_URI = "git://github.com/sifive/freedom-u540-c000-bootloader.git;protocol=https;branch=${BRANCH} \
           file://import-detect-null.patch \
           file://use-oe-default-cmd.patch \
           file://drop-gnu-build-id.patch \
           file://gcc-nopie.patch"

S = "${WORKDIR}/git"

do_compile() {
    # Update DTB from the current kernel build
    rm -f ${B}/fsbl/ux00_fsbl.dts
    cp -f ${DEPLOY_DIR_IMAGE}/${RISCV_SBI_FDT} ${B}/fsbl/ux00_fsbl.dtb

    oe_runmake
}

do_install() {
    install -d ${D}/platform/${MACHINE}/firmware/
    install -m 755 fsbl.bin ${D}/platform/${MACHINE}/firmware/
    install -m 755 zsbl.bin ${D}/platform/${MACHINE}/firmware/
}

do_deploy() {
    install -d ${DEPLOY_DIR_IMAGE}
    install -m 755 ${D}/platform/${MACHINE}/firmware/fsbl.bin ${DEPLOY_DIR_IMAGE}/
    install -m 755 ${D}/platform/${MACHINE}/firmware/zsbl.bin ${DEPLOY_DIR_IMAGE}/
}

addtask deploy after do_install

FILES_${PN} += "/platform/${MACHINE}/firmware/fsbl.bin"
FILES_${PN} += "/platform/${MACHINE}/firmware/zsbl.bin"

COMPATIBLE_HOST = "(riscv64|riscv32).*"
