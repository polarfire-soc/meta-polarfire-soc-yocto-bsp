DESCRIPTION = "Library for parsing configuration files"
HOMEPAGE = "http://www.nongnu.org/confuse/"
LICENSE = "ISC"
SECTION = "libs"

LIC_FILES_CHKSUM = "file://LICENSE;md5=42fa47330d4051cd219f7d99d023de3a"

SRC_URI = "https://github.com/libconfuse/libconfuse/releases/download/v${PV}/confuse-${PV}.tar.gz"
SRC_URI[sha256sum] = "71316b55592f8d0c98924242c98dbfa6252153a8b6e7d89e57fe6923934d77d0"

SRC_URI += "file://0001-only-apply-search-path-logic-to-relative-pathnames.patch"

EXTRA_OECONF = "--enable-shared"

inherit autotools gettext binconfig pkgconfig lib_package

BBCLASSEXTEND = "native nativesdk"
