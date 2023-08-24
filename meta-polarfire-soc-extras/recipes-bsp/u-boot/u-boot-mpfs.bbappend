FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:remove:icicle-kit-es-auth = "file://${UBOOT_ENV}.txt"

SRC_URI:append:icicle-kit-es-auth = "file://authenticated-boot.cfg"

do_deploy:append:icicle-kit-es-auth () {

    if [ ! -f "${HSS_PAYLOAD_KEYDIR}/${HSS_PAYLOAD_PRIVATE_KEYNAME}.pem" ];then
        bbfatal "Authentication Boot file check, missing: ${HSS_PAYLOAD_KEYDIR}/${HSS_PAYLOAD_PRIVATE_KEYNAME}.pem, Refer to the Polarfire SoC Documentation"
    fi

    if [ ! -f "${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.crt" ];then
        bbfatal "Authentication Boot file check, missing: ${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.crt, Refer to the Polarfire SoC Documentation"
    fi

    if [ ! -f "${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.key" ];then
        bbfatal "Authentication Boot file check,  missing: ${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.key, Refer to the Polarfire SoC Documentation"
    fi

    bbplain "Using Signing Keys Located in ${HSS_PAYLOAD_KEYDIR}"

    hss-payload-generator -c ${WORKDIR}/${HSS_PAYLOAD}.yaml -v ${DEPLOYDIR}/payload.bin -p ${HSS_PAYLOAD_KEYDIR}/${HSS_PAYLOAD_PRIVATE_KEYNAME}.pem

}