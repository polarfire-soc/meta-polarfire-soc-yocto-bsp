SUMMARY = "Device Trees overlay for BSPs"
DESCRIPTION = "Device Tree overlay generation and packaging for BSP"
SECTION = "bsp"

LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = " \
		file://LICENSES/MIT;md5=e8f57dd048e186199433be2c41bd3d6d \
		file://LICENSES/GPL-2.0;md5=e6a75371ba4d16749254a51215d13f97 \
		"
PR = "r0"

inherit autotools

DEPENDS += "virtual/kernel dtc-native"

BRANCH = "master"
SRCREV="${AUTOREV}"
SRC_URI="git://github.com/linux4microchip/dt-overlay-mchp.git;protocol=https;branch=${BRANCH} \
"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'icicle_kit_dtbos -C ${S} DTC=dtc ARCH=riscv KERNEL_DIR=${STAGING_KERNEL_DIR}'

do_install() {
	install -d ${D}/boot
	install ${S}/mpfs-icicle/*.dtbo ${D}/boot
}

FILES:${PN} += "/boot/*.dtbo"
