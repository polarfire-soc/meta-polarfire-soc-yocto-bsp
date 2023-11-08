SUMMARY = "A pure Python OPC UA/IEC 62541 Client and Server library"
HOMEPAGE = "https://github.com/FreeOpcUa/opcua-asyncio.git"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/LGPL-3.0-only;md5=bfccfe952269fff2b407dd11f2f3083b"

inherit pypi setuptools3

PYPI_PACKAGE = "asyncua"
SRC_URI[sha256sum] = "dde453fb759589c2b467f34cb3732a1445b3287a3d65e554418a6f4499b0043a"

BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} += "\
    ${PYTHON_PN}-cryptography \
    ${PYTHON_PN}-dateutil \
    ${PYTHON_PN}-lxml \
    ${PYTHON_PN}-pytz \
    ${PYTHON_PN}-aiofiles \
"

