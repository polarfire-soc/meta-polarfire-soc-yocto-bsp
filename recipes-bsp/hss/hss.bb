SUMMARY = "Microchip Polarfire SoC Hart Software Services (HSS) Payload Generator"
DESCRIPTION = "HSS requires U-Boot to be packaged with header details applied with hss payload generator"

LICENSE = "MIT & BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=2cd3c5e5cf4de899b2ed773b471011f2"

require hss.inc

# Strict dependency
do_configure[depends] += "${@hss_get_do_compile_depends(d)}"

DEPENDS += "elfutils-native libyaml-native"

PV = "1.0+git${SRCPV}"
BRANCH = "master"
SRCREV="ae14baa0b4a26de2063a610ac54a883a91db7646"
SRC_URI = "git://github.com/polarfire-soc/hart-software-services.git;protocol=https;branch=${BRANCH} \
           file://${HSS_PAYLOAD}.yaml \
          "

S = "${WORKDIR}/git"

# NOTE: Only using the Payload generator from the HSS
do_configure:icicle-kit-es () {
	## taking U-Boot binary and package for HSS
	cp -f ${DEPLOY_DIR_IMAGE}/u-boot.bin ${WORKDIR}/git/
	cp -f ${WORKDIR}/${HSS_PAYLOAD}.yaml ${WORKDIR}/git/tools/hss-payload-generator/
}

# NOTE: Only using the Payload generator from the HSS
do_configure:icicle-kit-es-amp () {
	## taking U-Boot binary and package for HSS
	cp -f ${DEPLOY_DIR_IMAGE}/u-boot.bin ${WORKDIR}/git/
   cp -L ${DEPLOY_DIR_IMAGE}/amp-application.elf ${WORKDIR}/git/
	cp -f ${WORKDIR}/${HSS_PAYLOAD}.yaml ${WORKDIR}/git/tools/hss-payload-generator/
}

EXTRA_OEMAKE = "CC='${BUILD_CC}' CFLAGS='${BUILD_CFLAGS}' LDFLAGS='${BUILD_LDFLAGS}'"
do_compile () {
    ## Adding u-boot as a payload
    ## Using hss-payload-generator application
    oe_runmake -C ${S}/tools/hss-payload-generator
    ${S}/tools/hss-payload-generator/hss-payload-generator -c ${S}/tools/hss-payload-generator/${HSS_PAYLOAD}.yaml -v payload.bin
}

do_install() {
   :
}

do_deploy() {
   install -d ${DEPLOY_DIR_IMAGE}
   install -m 755 ${S}/payload.bin ${DEPLOY_DIR_IMAGE}/
}

addtask deploy after do_install

