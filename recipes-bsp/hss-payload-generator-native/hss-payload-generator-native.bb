SUMMARY = "Microchip Polarfire SoC Hart Software Services (HSS) Payload Generator"
DESCRIPTION = "HSS requires U-Boot to be packaged into bootable format using HSS payload generator tool"
HOMEPAGE = "https://github.com/polarfire-soc/hart-software-services"

LICENSE = "MIT | GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=2cd3c5e5cf4de899b2ed773b471011f2"

inherit native

DEPENDS = "elfutils-native libyaml-native"

PV = "1.0+git${SRCPV}"
BRANCH = "master"
SRCREV = "01e82e3e05b5b00a22920eba811d95ed885ff16d"
SRC_URI = "git://bitbucket.microchip.com/scm/fpga_pfsoc_es/hart-software-services.git;protocol=https;branch=${BRANCH}"
SRC_URI[sha256sum] = "e158c56e41f69d51992c5743f806aae2af6922ec0cfec2f91842b5c9294bc01b"

S = "${WORKDIR}/git"

do_compile () {
    oe_runmake -C ${S}/tools/hss-payload-generator -e CFLAGS="${CFLAGS}"
}

do_install() {
    install -d ${D}${bindir}
    install -m 755 ${S}/tools/hss-payload-generator/hss-payload-generator ${D}${bindir}
}

FILES_${PN} = "${bindir}/hss-payload-generator"

