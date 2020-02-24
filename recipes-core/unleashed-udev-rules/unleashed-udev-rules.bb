SUMMARY = "udev rules for SiFive Unleashed"
LICENSE = "GPLv2"

PR = "r0"

SRC_URI_freedom-u540 = "file://LICENSE.GPL2 \
                        file://99-eth0-pwm-led.rules"

LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE.GPL2;md5=b234ee4d69f5fce4486a80fdaf4a4263"

S = "${WORKDIR}"

do_install() {
    install -d ${D}/etc/udev/rules.d
    install -m 644 ${B}/99-eth0-pwm-led.rules ${D}/etc/udev/rules.d/
}

FILES_${PN} += "/etc/udev/rules.d/99-eth0-pwm-led.rules"

COMPATIBLE_HOST = "riscv64.*"
COMPATIBLE_MACHINE = "(lc-mpfs|mpfs)"
