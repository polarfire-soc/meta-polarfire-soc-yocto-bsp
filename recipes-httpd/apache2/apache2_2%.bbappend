FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:sev-kit-es = "file://httpd.conf"

do_install:append:sev-kit-es () {
	install -d ${D}${sysconfdir}/apache2/
        install -m 0755 ${WORKDIR}/httpd.conf ${D}${sysconfdir}/apache2/
}

FILES_${PN}:sev-kit-es += "/etc/apache2/httpd.conf"

