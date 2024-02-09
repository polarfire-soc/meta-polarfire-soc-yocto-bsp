FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:remove:icicle-kit-es-nand = "file://fstab"
SRC_URI:remove:icicle-kit-es-nor = "file://fstab"

