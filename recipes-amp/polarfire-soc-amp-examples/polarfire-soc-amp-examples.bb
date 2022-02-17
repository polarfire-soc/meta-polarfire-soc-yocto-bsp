SUMMARY = "Polarfire SoC AMP example applications"
DESCRIPTION = "Example FreeRTOS application to run in AMP build \
along with a Linux context"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=4396bf71d143500c4d9fa09c02527700"

DEPENDS = "makedepend-native"

BRANCH = "main"
SRCREV="216e1665f2feb2b32ba8647f9755324117ae7e10"
SRC_URI = "git://github.com/polarfire-soc/polarfire-soc-amp-examples.git;protocol=https;branch=${BRANCH}"

S = "${WORKDIR}/git/mpfs-rpmsg-freertos"

EXT_CFLAGS += "--sysroot=${STAGING_DIR_TARGET}"
EXT_CFLAGS += "-DMPFS_HAL_FIRST_HART=4 -DMPFS_HAL_LAST_HART=4"

PARALLEL_MAKE = ""
EXTRA_OEMAKE = "REMOTE=1 CROSS_COMPILE=${TARGET_PREFIX} EXT_CFLAGS='${EXT_CFLAGS}'"

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
    install -m 755 ${S}/Remote-Default/mpfs-rpmsg-remote.elf ${DEPLOY_DIR_IMAGE}
    ln -sf ${DEPLOY_DIR_IMAGE}/mpfs-rpmsg-remote.elf ${DEPLOY_DIR_IMAGE}/amp-application.elf 
}

addtask deploy after do_install

