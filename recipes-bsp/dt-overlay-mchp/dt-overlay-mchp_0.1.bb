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

SRCREV="${AUTOREV}"
SRC_URI="git://git@bitbucket.microchip.com/fpga_pfsoc_es/dt-overlay-polarfire-soc.git;protocol=ssh;branch=develop-rpi_sense_hat \
"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'icicle_kit_dtbos -C ${S} DTC=dtc ARCH=riscv KERNEL_DIR=${STAGING_KERNEL_DIR}'

do_install() {
	install -d ${D}/boot
	install ${S}/mpfs-icicle/*.dtbo ${D}/boot
}

FILES:${PN} += "/boot/*.dtbo"
