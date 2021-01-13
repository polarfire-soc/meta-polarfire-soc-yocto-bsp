SUMMARY = "Microchip Polarfire SoC Hart Software Services (HSS) Payload Generator"
DESCRIPTION = "HSS requires U-Boot to be packaged with header details applied with hss payload generator"

LICENSE = "MIT & BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=2dc9e752dd76827e3a4eebfd5b3c6226"

inherit deploy native
# Strict dependency
do_configure[depends] += "u-boot:do_deploy"

DEPENDS += "elfutils-native libyaml-native"

PV = "1.0+git${SRCPV}"
BRANCH = "master"
SRCREV="76b34dd0212425f9848eed41575db22cd829cecb"
SRC_URI = "git://github.com/polarfire-soc/hart-software-services.git;branch=${BRANCH} \
           file://0001-hss-payload-generator-Rename-EM_ARC_COMPACT2-to-EM_A.patch \
           file://0002-hss-payload-generator-Respect-LDFLAGS-during-linking.patch \
           file://uboot.yaml \
          "

S = "${WORKDIR}/git"

# NOTE: Only using the Payload generator from the HSS
do_configure () {
	## taking U-Boot binary and package for HSS
	cp -f ${DEPLOY_DIR_IMAGE}/u-boot.bin ${WORKDIR}/git/
	cp -f ${WORKDIR}/uboot.yaml ${WORKDIR}/git/tools/hss-payload-generator/
}

EXTRA_OEMAKE = "CC='${BUILD_CC}' CFLAGS='${BUILD_CFLAGS}' LDFLAGS='${BUILD_LDFLAGS}'"
do_compile () {
	## Adding u-boot as a payload
	## Using hss-payload-generator application
	oe_runmake -C ${S}/tools/hss-payload-generator
	${S}/tools/hss-payload-generator/hss-payload-generator -c ${S}/tools/hss-payload-generator/uboot.yaml -v payload.bin
}

do_install() {
    :
}

do_deploy() {
	install -d ${DEPLOY_DIR_IMAGE}
	install -m 755 ${S}/payload.bin ${DEPLOY_DIR_IMAGE}/
}

addtask deploy after do_install
