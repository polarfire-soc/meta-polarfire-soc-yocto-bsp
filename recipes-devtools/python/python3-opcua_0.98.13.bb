SUMMARY = "A pure Python OPC UA/IEC 62541 Client and Server library"
HOMEPAGE = "https://github.com/FreeOpcUa/python-opcua"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/LGPL-3.0-only;md5=bfccfe952269fff2b407dd11f2f3083b"

inherit pypi setuptools3

PYPI_PACKAGE = "opcua"
SRC_URI[sha256sum] = "3352f30b5fed863146a82778aaf09faa5feafcb9dd446a4f49ff34c0c3ebbde6"

BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} += "${PYTHON_PN}-cryptography ${PYTHON_PN}-dateutil ${PYTHON_PN}-lxml ${PYTHON_PN}-pytz"
