DESCRIPTION = "Web server application showcasing video streaming over Ethernet \
using the SEV kit's Video4Linux pipeline"

SECTION = "examples"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
PR = "r0"

RDEPENDS:${PN} += "bash"
WEB_PATH="/srv/www"
MJPEG_PATH="/srv/www/mjpeg"
OPENVX_PATH="/srv/www/php"
H264_PATH="/srv/www/h264"
SYSTEM_CONF="/etc"

SRC_URI:append = "file://. "

do_install:append () {
	install -d ${D}${WEB_PATH}
	install -m 0755 ${WORKDIR}/index.php ${D}${WEB_PATH}
	install -m 0755 ${WORKDIR}/LICENSE ${D}${WEB_PATH}
	install -d ${D}${H264_PATH}
	install -m 0755 ${WORKDIR}/h264/* ${D}${H264_PATH}
	install -d ${D}/etc/systemd/network/
	cp ${WORKDIR}/60-static-eth0.network ${D}/etc/systemd/network/
	cp ${WORKDIR}/70-static-eth1.network ${D}/etc/systemd/network/
	chown -R 1:root ${D}${WEB_PATH}/
}

FILES:${PN} += "${WEB_PATH}/*"
FILES:${PN} += "/etc/systemd/network/*"

COMPATIBLE_MACHINE = "(sev-kit-es)"

