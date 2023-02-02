FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:mpfs-video-kit = "file://httpd.conf"

do_install:append:mpfs-video-kit () {
	install -d ${D}${sysconfdir}/apache2/
        install -m 0755 ${WORKDIR}/httpd.conf ${D}${sysconfdir}/apache2/
}

FILES_${PN}:mpfs-video-kit += "/etc/apache2/httpd.conf"

