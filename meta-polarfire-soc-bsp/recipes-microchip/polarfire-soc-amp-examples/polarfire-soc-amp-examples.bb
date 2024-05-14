SUMMARY = "Polarfire SoC AMP example applications"
DESCRIPTION = "Example FreeRTOS application to run in AMP build \
along with a Linux context"

PACKAGE_ARCH = "${MACHINE_ARCH}"

AMP_BOARD ?= "${MACHINE}"

python () {
    # Translate MACHINE into AMP_BOARD
    # AMP_BOARD is basically our MACHINE, except we must use "-" instead of "_"
    board = d.getVar('AMP_BOARD', True)
    board = board.replace('-', '_')
    board = board.upper()
    d.setVar('AMP_BOARD',board)
}

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=736a28e202059f476d2e3e1c80e5b009"

DEPENDS = "makedepend-native"

inherit deploy

PV = "1.0+git${SRCPV}"
SRCREV="v2024.02"
SRC_URI = " \
    git://github.com/polarfire-soc/polarfire-soc-amp-examples.git;protocol=https;nobranch=1 \
    file://0001-app-update-demo-to-support-discovery-kit.patch \
    "

S = "${WORKDIR}/git"

EXT_CFLAGS += "--sysroot=${STAGING_DIR_TARGET}"
EXT_CFLAGS += "-DMPFS_HAL_FIRST_HART=4 -DMPFS_HAL_LAST_HART=4 -DBOARD_${AMP_BOARD}"

PARALLEL_MAKE = ""
EXTRA_OEMAKE = "REMOTE=1 REMOTEPROC=1 CROSS_COMPILE=${TARGET_PREFIX} EXT_CFLAGS='${EXT_CFLAGS}'"

ALLOWED_AMP_DEMO = "freertos bm"

do_install() {
    if [[ "${ALLOWED_AMP_DEMO}" =~ "${AMP_DEMO}" ]]; then
        install -d ${D}${nonarch_base_libdir}/firmware
        install ${S}/mpfs-rpmsg-${AMP_DEMO}/Remote-Default/mpfs-rpmsg-remote.elf ${D}${nonarch_base_libdir}/firmware/rproc-miv-rproc-fw
    else
        bbnote "${PN} do_install() have been skipped, because ${AMP_DEMO} is not covered by this recipe"
    fi
}

do_compile() {
    if [[ "${ALLOWED_AMP_DEMO}" =~ "${AMP_DEMO}" ]]; then
        oe_runmake -C ${S}/mpfs-rpmsg-${AMP_DEMO}
    else
        bbnote "${PN} do_compile() have been skipped, because ${AMP_DEMO} is not covered by this recipe"
    fi
}

do_deploy() {
    if [[ "${ALLOWED_AMP_DEMO}" =~ "${AMP_DEMO}" ]]; then
        install -m 755 ${S}/mpfs-rpmsg-${AMP_DEMO}/Remote-Default/mpfs-rpmsg-remote.elf ${DEPLOYDIR}/amp-application.elf
    else
        bbnote "${PN} do_deploy() have been skipped, because ${AMP_DEMO} is not covered by this recipe"
    fi
}

addtask deploy after do_install

COMPATIBLE_MACHINE = "(icicle-kit-es-amp|mpfs-disco-kit-amp)"

FILES:${PN} += "/lib/firmware/"
