FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Aries m100pfsevp machine uses built-in U-boot env
SRC_URI:remove:m100pfsevp = "file://${UBOOT_ENV}.txt"

COMPATIBLE_MACHINE:m100pfsevp = "m100pfsevp"