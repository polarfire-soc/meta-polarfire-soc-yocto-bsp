SUMMARY = "Polarfire SoC AMP example applications"
DESCRIPTION = "Example FreeRTOS application to run in AMP build \
along with a Linux context"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=736a28e202059f476d2e3e1c80e5b009"

DEPENDS = "makedepend-native"

inherit deploy

PV = "1.0+git${SRCPV}"
SRCREV="27fce17b4c84c66bfceb784ddfd2ca35f6d33cdd"
SRC_URI = "git://github.com/polarfire-soc/polarfire-soc-amp-examples.git;protocol=https;nobranch=1"

S = "${WORKDIR}/git"

EXT_CFLAGS += "--sysroot=${STAGING_DIR_TARGET}"
EXT_CFLAGS += "-DMPFS_HAL_FIRST_HART=4 -DMPFS_HAL_LAST_HART=4"

PARALLEL_MAKE = ""
EXTRA_OEMAKE = "REMOTE=1 REMOTEPROC=1 CROSS_COMPILE=${TARGET_PREFIX} EXT_CFLAGS='${EXT_CFLAGS}'"

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware
    install ${S}/mpfs-rpmsg-${AMP_DEMO}/Remote-Default/mpfs-rpmsg-remote.elf ${D}${nonarch_base_libdir}/firmware/rproc-miv-rproc-fw
}

do_compile() {
   oe_runmake -C ${S}/mpfs-rpmsg-${AMP_DEMO}
}

do_deploy() {
    install -m 755 ${S}/mpfs-rpmsg-${AMP_DEMO}/Remote-Default/mpfs-rpmsg-remote.elf ${DEPLOYDIR}/amp-application.elf
}

addtask deploy after do_install

COMPATIBLE_MACHINE = "(icicle-kit-es-amp)"

FILES:${PN} += "/lib/firmware/"
