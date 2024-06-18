DESCRIPTION = "Systemd script to configure and capture video from the camera module using Video4Linux"
SECTION = "examples"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
PR = "r0"

SRC_URI = " \
	file://v4l2-start_service.sh \
	file://v4l2-start_service.service \
"

inherit systemd features_check

RDEPENDS:${PN} += "bash"

S = "${WORKDIR}"

do_compile() {
	:
}

do_install() {
	install -d ${D}/opt/microchip/multimedia/v4l2/
	install -m 0755 v4l2-start_service.sh ${D}/opt/microchip/multimedia/v4l2/v4l2-start_service.sh
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/v4l2-start_service.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE:${PN} = "v4l2-start_service.service"

SYSTEMD_AUTO_ENABLE:${PN} = "enable"

FILES:${PN} += "/lib/systemd/system"
FILES:${PN} += "/opt/microchip/multimedia/v4l2/v4l2-start_service.sh"

REQUIRED_DISTRO_FEATURES= "systemd"
