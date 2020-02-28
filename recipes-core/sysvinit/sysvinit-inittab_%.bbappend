do_install_append() {
    sed -i -e 's#/bin/start_getty#/sbin/getty#g' ${D}${sysconfdir}/inittab
    sed -i '/tty1/d' ${D}${sysconfdir}/inittab
}
