SUMMARY = "Deploy gateware scripts to /usr/share/beagleboard/gateware"
DESCRIPTION = "This recipe installs and creates symbolic links for change-gateware and update-gateware scripts."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Original scripts:
# https://openbeagle.org/beaglev-fire/gateware/-/tree/main/debian?ref_type=heads

CHANGE_SCRIPT = "change-gateware"
UPDATE_SCRIPT = "update-gateware"

SRC_URI = "file://${CHANGE_SCRIPT}.sh \
           file://${UPDATE_SCRIPT}.sh"

S = "${WORKDIR}"

inherit nopackages deploy

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}/usr/share/beagleboard/gateware
    install -d ${D}${bindir}

    install -m 0755 ${WORKDIR}/${CHANGE_SCRIPT}.sh ${D}/usr/share/beagleboard/gateware/${CHANGE_SCRIPT}
    install -m 0755 ${WORKDIR}/${UPDATE_SCRIPT}.sh ${D}/usr/share/beagleboard/gateware/${UPDATE_SCRIPT}
    
    ln -srvf /usr/share/beagleboard/gateware/${CHANGE_SCRIPT} ${D}${bindir}/${CHANGE_SCRIPT}
    ln -srvf /usr/share/beagleboard/gateware/${UPDATE_SCRIPT} ${D}${bindir}/${UPDATE_SCRIPT}
}

FILES:${PN} += "/usr/share/beagleboard/gateware/${CHANGE_SCRIPT} \
                /usr/share/beagleboard/gateware/${UPDATE_SCRIPT} \
                /usr/bin/${CHANGE_SCRIPT} \
                /usr/bin/${UPDATE_SCRIPT}"

RDEPENDS:${PN} = "bash"
