FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://0001-networkd-add-any-option-to-the-wait-online.patch"

PACKAGE_ARCH = "${MACHINE_ARCH}"