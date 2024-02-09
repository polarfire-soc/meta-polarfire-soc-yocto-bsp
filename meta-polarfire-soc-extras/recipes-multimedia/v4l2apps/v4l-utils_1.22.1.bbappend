FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:mpfs-video-kit = " \
	file://0001-media-bus-format-header-update.patch \
"

