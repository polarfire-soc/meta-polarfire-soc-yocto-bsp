SUMMARY = "Polarfire SoC Linux example applications"
DESCRIPTION = "Linux Example applications, includes the following \
-iiohttpserver collects ADC measurements and displays them via a webserver.\
-LSRAM read and write example.\
-UIO DMA interrupt example. \ 
"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=06ec214e9fafe6d4515883d77674a453"

DEPENDS = "collectd libgpiod"
inherit systemd

PV = "1.0+git${SRCPV}"
BRANCH = "master"
SRCREV="1ae82e779341c17f31dbb83aa8a0558d8eac1af4"
SRC_URI = "git://github.com/polarfire-soc/polarfire-soc-linux-examples.git;protocol=https;branch=${BRANCH} \
          "
S = "${WORKDIR}/git"

EXAMPLE_FILES = "\
    can \
    dma \
    fpga-fabric-interfaces/lsram \
    gpio \
    pdma \
    system-services \
    "

EXAMPLE_FILES:append:icicle-kit-es-amp = "\
    amp/rpmsg-pingpong \
    amp/rpmsg-tty-example \
    "

do_compile() {
  for i in ${EXAMPLE_FILES}; do
    oe_runmake -C ${S}/$i
  done
}

INSANE_SKIP_${PN} += "file-rdeps"
INSANE_SKIP:${PN} = "ldflags"
INSANE_SKIP:${PN}-dev = "ldflags"

SECURITY_CFLAGS = ""

do_install() {
    install -d ${D}/opt/microchip
    chmod a+x ${D}/opt/microchip

    cp -rfd ${S}/* ${D}/opt/microchip/
    ## Symbolic Link for iiohttpserver
    ln -s -r ${D}/opt/microchip/ethernet/iio-http-server ${D}/opt/microchip/iiohttpserver
    
    rm -f ${D}/opt/microchip/.git
    rm -f ${D}/opt/microchip/Jenkinsfile
    ## Install the iio-http-server
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/ethernet/iio-http-server/collection/collectdiio.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE:${PN} = "collectdiio.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"
FILES:${PN} += "/opt/microchip/"

