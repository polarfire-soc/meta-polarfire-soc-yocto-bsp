DESCRIPTION = "A handy tool to manipulate and read memory mapped registers"
HOMEPAGE = "https://github.com/pengutronix/memtool"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "http://www.pengutronix.de/software/memtool/downloads/memtool-${PV}.tar.xz"
SRC_URI[md5sum] = "b3b16018cda270fa1d375ea09b67d6ae"
SRC_URI[sha256sum] = "87cb7175266ff3a00a9c1f541c4c6c93693ffbe8dcc0d97a60d13c45ff860900"

PACKAGECONFIG ??= "mdio"
PACKAGECONFIG[mdio] = "--enable-mdio,--disable-mdio"

inherit autotools
