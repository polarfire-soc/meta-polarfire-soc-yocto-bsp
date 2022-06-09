SUMMARY = "Device Trees overlay for BSPs"
DESCRIPTION = "Device Tree overlay generation and packaging for BSP"
SECTION = "bsp"

LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = " \
		file://LICENSES/MIT;md5=e8f57dd048e186199433be2c41bd3d6d \
		file://LICENSES/GPL-2.0;md5=e6a75371ba4d16749254a51215d13f97 \
		"
PR = "r0"

inherit devicetree

COMPATIBLE_MACHINE = "(icicle-kit-es|icicle-kit-es-amp|m100pfsev|sev-kit-es)"

S = "${WORKDIR}/git"

DT_FILES_PATH = "${WORKDIR}/git/mpfs-icicle"

BRANCH = "master"
SRCREV="195213614b15a8cd5d4c8baa92fe94f2144465f2"
SRC_URI="git://github.com/linux4microchip/dt-overlay-mchp.git;protocol=https;branch=${BRANCH} \
"

do_install() {
    install -d ${D}/boot
    install ${B}/*.dtbo ${D}/boot
}

devicetree_do_deploy() {
	install -d ${DEPLOYDIR}/overlays
	install -m 0644 ${B}/*.dtbo ${DEPLOYDIR}/overlays/
}

FILES:${PN} += "/boot/*.dtbo"
