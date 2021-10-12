SUMMARY = "Polarfire SoC AMP example applications"
DESCRIPTION = "Example FreeRTOS application to run in AMP build \
along with a Linux context"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=6476811715b02d71f67f70238e7e8948"

DEPENDS = "makedepend-native"

BRANCH = "main"
SRCREV="${AUTOREV}"
SRC_URI = "git://github.com/polarfire-soc/polarfire-soc-amp-examples.git;branch=${BRANCH}"

S = "${WORKDIR}/git/mpfs-amp-freertos"

EXT_CFLAGS += "--sysroot=${STAGING_DIR_TARGET}"
EXT_CFLAGS += "-DMPFS_HAL_FIRST_HART=3 -DMPFS_HAL_LAST_HART=3"

PARALLEL_MAKE = ""
EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX} EXT_CFLAGS='${EXT_CFLAGS}'"

do_compile() {
   oe_runmake
}

do_install() {
    :
}

do_deploy() {
    :
}

do_deploy:append:icicle-kit-es-amp() {
    install -d ${DEPLOY_DIR_IMAGE}
    install -m 755 ${S}/Default/mpfs-amp-freertos.elf ${DEPLOY_DIR_IMAGE}
    ln -sf ${DEPLOY_DIR_IMAGE}/mpfs-amp-freertos.elf ${DEPLOY_DIR_IMAGE}/amp-application.elf 
}

addtask deploy after do_install

