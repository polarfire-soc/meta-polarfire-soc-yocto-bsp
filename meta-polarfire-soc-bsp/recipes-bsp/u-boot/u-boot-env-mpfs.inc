# U-Boot Environments for AT91 boards

ENV_SOURCE = "uEnv"
ENV_FILENAME = "uboot.env"

do_compile:append() {
    if [ -f "${WORKDIR}/${ENV_SOURCE}.txt" ]; then
        mkenvimage ${MKENVIMAGE_EXTRA_ARGS} -s ${UBOOT_ENV_SIZE} ${WORKDIR}/${ENV_SOURCE}.txt -o ${ENV_FILENAME}
    fi
}

do_deploy:append() {
    if [ -e  ${B}/${ENV_FILENAME} ]; then
        install -Dm 0644 ${B}/${ENV_FILENAME} ${DEPLOYDIR}
    fi
}