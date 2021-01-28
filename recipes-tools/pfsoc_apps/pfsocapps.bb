SUMMARY = "Polarfire SoC Linux example applications"
DESCRIPTION = "Linux Example applications, includes the following \
-iiohttpserver collects ADC measurements and displays them via a webserver.\
-LSRAM read and write example.\
-UIO DMA interrupt example. \ 
"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/ethernet/iio-http-server/LICENSE;md5=06ec214e9fafe6d4515883d77674a453"

DEPENDS = "collectd"
inherit systemd

PV = "1.0+git${SRCPV}"

BRANCH = "master"
SRCREV="${AUTOREV}"
SRC_URI = "git://git@bitbucket.microchip.com/fpga_pfsoc_es/polarfire-soc-linux-examples.git;protocol=ssh;branch=master \
	   " 
S = "${WORKDIR}"
do_compile() {
  :
}


INSANE_SKIP_${PN} += "file-rdeps"

do_install() {
    install -d ${D}/opt/microchip
    install -d ${D}/opt/microchip/iiohttpserver
    chmod a+x ${D}/opt/microchip

    cp -rfd ${S}/git/* ${D}/opt/microchip/
    ln -s ${D}/opt/microchip/ethernet/iio-http-server ${D}/opt/microchip/iiohttpserver
    rm -f ${D}/opt/microchip/.git
    rm -f ${D}/opt/microchip/Jenkinsfile
    ## Install the iio-http-server
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/git/ethernet/iio-http-server/collection/collectdiio.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE_${PN} = "collectdiio.service"
FILES_${PN} += "/opt/microchip/"

