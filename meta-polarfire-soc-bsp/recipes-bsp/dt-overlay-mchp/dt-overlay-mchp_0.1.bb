SUMMARY = "Device Trees overlay for BSPs"
DESCRIPTION = "Device Tree overlay generation and packaging for BSP"
SECTION = "bsp"

LICENSE = "MIT & GPL-2.0-or-later"
LIC_FILES_CHKSUM = " \
		file://LICENSES/MIT;md5=e8f57dd048e186199433be2c41bd3d6d \
		file://LICENSES/GPL-2.0;md5=e6a75371ba4d16749254a51215d13f97 \
		"
PR = "r0"

inherit devicetree

S = "${WORKDIR}/git"

DT_FILES_PATH = "${WORKDIR}/git/mpfs_icicle"
DT_FILES_PATH:mpfs-video-kit = "${WORKDIR}/git/mpfs_video"
DT_FILES_PATH:mpfs-disco-kit = "${WORKDIR}/git"

PV = "2024.04"
SRCREV="77df5fd07814283d725d65be6d34c72e78ccccd4"
SRC_URI="git://github.com/linux4microchip/dt-overlay-mchp.git;protocol=https;nobranch=1 \
"

do_install() {
    cd ${B}
    for DTB_FILE in `ls *.dtbo`; do
        install -Dm 0644 ${B}/${DTB_FILE} ${D}/boot/${DTB_FILE}
    done
}

do_deploy() {
    cd ${B}
    for DTB_FILE in `ls *.dtbo`; do
        install -Dm 0644 ${B}/${DTB_FILE} ${DEPLOYDIR}/overlays/${DTB_FILE}
    done
}

FILES:${PN} += "/boot/*.dtbo"

COMPATIBLE_MACHINE = "(icicle-kit|mpfs-video-kit|mpfs-disco-kit|beaglev-fire)"
