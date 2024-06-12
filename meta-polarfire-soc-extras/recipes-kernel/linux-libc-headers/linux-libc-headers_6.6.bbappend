FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:mpfs-video-kit = " \
       file://0001-YUV420_H264_1X8-media-bus-format.patch \
"


