SUMMARY = "The RISCV Tools"
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"
PR = "r0"
SRC_URI = "file://l2ways.c \
           file://COPYING \
           file://tune-my-box \
	   file://freq.sh \
	   file://coremarkpro_1.1.2743.tar \
	   "	

do_compile () {
	${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/l2ways.c -o ${WORKDIR}/l2ways
}

FILES_${PN} += "/mpfs/tools"
FILES_${PN} += "/mpfs/coremarkpro/*"


do_install() {
    install -d ${D}/mpfs/coremarkpro
    chmod a+x ${D}/mpfs/coremarkpro
    install -d ${D}/mpfs/tools
    cp -r ${WORKDIR}/coremarkpro_1.1.2743 ${D}/mpfs/coremarkpro
}
