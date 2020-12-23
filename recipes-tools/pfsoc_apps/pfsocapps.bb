SUMMARY = "Polarfire SoC Linux example applications"
DESCRIPTION = "Linux Example applications, includes the following \
-iiohttpserver collects ADC measurements and displays them via a webserver.\
-LSRAM read and write example.\
-UIO DMA interrupt example. \ 
"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/iiohttpserver/LICENSE;md5=7ab4f300905f96ded424f2161e031d93"

DEPENDS = "collectd"
inherit systemd

PV = "1.0+git${SRCPV}"

BRANCH = "master"
SRCREV="d4e4f2c41aa0e2739a83481d15e2ccfa98d80113"
SRC_URI="git://github.com/polarfire-soc/polarfire-soc-linux-examples;branch=${BRANCH} \
         file://LSRAM_read_write.c \
	 file://uio_dma_interrupt.c  \
        "
S = "${WORKDIR}"
do_compile() {
	${CC} LSRAM_read_write.c ${LDFLAGS} -o LSRAM_read_write
	${CC} uio_dma_interrupt.c ${LDFLAGS} -o uio_dma_interrupt
}

do_install() {
    install -d ${D}/opt/microchip/iiohttpserver
    chmod a+x ${D}/opt/microchip/iiohttpserver
 
    ## Install the iiohttserver
    cp -r ${WORKDIR}/git/iiohttpserver ${D}/opt/microchip/
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/git/iiohttpserver/collection/collectdiio.service ${D}${systemd_unitdir}/system

    ## Microchip Sample Applications, 
    install -d ${D}/opt/microchip/apps
    ##install -m 0755 led_blinky ${D}/opt/microchip/apps
    install -m 0755 LSRAM_read_write.c ${D}/opt/microchip/apps
    install -m 0755 LSRAM_read_write ${D}/opt/microchip/apps
    install -m 0755 uio_dma_interrupt ${D}/opt/microchip/apps
    install -m 0755 uio_dma_interrupt.c ${D}/opt/microchip/apps
}

SYSTEMD_SERVICE_${PN} = "collectdiio.service"
FILES_${PN} += "/opt/microchip/iiohttpserver"
FILES_${PN} += "/opt/microchip/apps"
