SUMMARY = "The Microschip HTTP Server, Reading Hardware ADC's"
SECTION = "base"
LICENSE = "CLOSED"

PR = "r0"
SRC_URI = "file://iiohttpserver-0.0.9.tar \
	   "	

do_install() {
    install -d ${D}/opt/microchip/iiohttpserver
    chmod a+x ${D}/opt/microchip/iiohttpserver
    cp -r ${WORKDIR}/iiohttpserver-0.0.9/iiohttpserver ${D}/opt/microchip/
}


FILES_${PN} += "/opt/microchip/iiohttpserver"
