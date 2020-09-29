SUMMARY = "Polarfire SoC Linux example applications"
DESCRIPTION = "Linux Example applications, includes the following \
-iiohttpserver collects ADC measurements and displays them via a webserver.\
- "

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/iiohttpserver/LICENSE;md5=7ab4f300905f96ded424f2161e031d93"

DEPENDS = "collectd"
inherit systemd

PV = "1.0+git${SRCPV}"

BRANCH = "master"
SRCREV="d4e4f2c41aa0e2739a83481d15e2ccfa98d80113"
SRC_URI="git://github.com/polarfire-soc/polarfire-soc-linux-examples;branch=${BRANCH}"


do_install() {
    install -d ${D}/opt/microchip/iiohttpserver
    chmod a+x ${D}/opt/microchip/iiohttpserver
 
    ## Install the iiohttserver
    cp -r ${WORKDIR}/git/iiohttpserver ${D}/opt/microchip/
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/git/iiohttpserver/collection/collectdiio.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE_${PN} = "collectdiio.service"
FILES_${PN} += "/opt/microchip/iiohttpserver"
