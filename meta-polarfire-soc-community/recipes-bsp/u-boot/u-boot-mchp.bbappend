FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

COMPATIBLE_MACHINE:m100pfsevp = "m100pfsevp"

COMPATIBLE_MACHINE:beaglev-fire = "beaglev-fire"

SRC_URI:append:beaglev-fire = "file://${UBOOT_ENV}.cmd \
                                file://${MACHINE}.cfg \
                                file://uEnv.txt \
			"
