SUMMARY = "Embedded Microprocessor Benchmark Consortium Coremark-pro"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=c84d8f508b20d579641ad151a79a8bf3"

SRCREV="d5b4f2ba7ba31e37a5aa93423831e7d5eb933868"
SRC_URI = "git://github.com/eembc/coremark-pro.git;branch=main"        



do_install() {
    install -d ${D}/opt/microchip/coremark-pro
    cp -r ${WORKDIR}/git/benchmarks ${D}/opt/microchip/coremark-pro
    cp -r ${WORKDIR}/git/docs ${D}/opt/microchip/coremark-pro
    cp -r ${WORKDIR}/git/mith ${D}/opt/microchip/coremark-pro
    cp -r ${WORKDIR}/git/util ${D}/opt/microchip/coremark-pro
    cp -r ${WORKDIR}/git/workloads ${D}/opt/microchip/coremark-pro
    cp ${WORKDIR}/git/coremarkpro.md5 ${D}/opt/microchip/coremark-pro
    cp ${WORKDIR}/git/LICENSE.md ${D}/opt/microchip/coremark-pro
    cp ${WORKDIR}/git/Makefile ${D}/opt/microchip/coremark-pro
    cp ${WORKDIR}/git/Makefile.mak ${D}/opt/microchip/coremark-pro
    cp ${WORKDIR}/git/README.md ${D}/opt/microchip/coremark-pro
}

FILES_${PN} += "/opt/microchip/coremark-pro/"
FILES_${PN}-dev += "/opt/microchip/coremark-pro/ ${includedir}"
