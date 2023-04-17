SUMMARY = "Microchip Polarfire SoC Hart Software Services (HSS) Payload Generator"
DESCRIPTION = "HSS requires U-Boot to be packaged into bootable format using HSS payload generator tool"
HOMEPAGE = "https://github.com/polarfire-soc/hart-software-services"

LICENSE = "MIT | GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=2cd3c5e5cf4de899b2ed773b471011f2"

inherit native

DEPENDS = "elfutils-native libyaml-native"

PV = "1.0+git${SRCPV}"
SRCREV = "8d9c56fb68c07a763942a919d07ff5577a2d6613"
SRC_URI = "git://github.com/polarfire-soc/hart-software-services.git;protocol=https;nobranch=1"

S = "${WORKDIR}/git"

do_compile () {
    oe_runmake -C ${S}/tools/hss-payload-generator -e CFLAGS="${CFLAGS}"
}

do_install() {
    install -d ${D}${bindir}
    install -m 755 ${S}/tools/hss-payload-generator/hss-payload-generator ${D}${bindir}
}

FILES_${PN} = "${bindir}/hss-payload-generator"

