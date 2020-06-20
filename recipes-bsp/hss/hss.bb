SUMMARY = "Hart Software Services (HSS)"
DESCRIPTION = "Microchip Polarfire SoC Bootloader\
- Zeroth Stage Boot Loader (ZSBL)\
- First Stage Boot Loader (FSBL)\
- Service Provider"

LICENSE = "MIT & BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=18287c7f3e6c73444d65f5ca062ea060"

inherit deploy
# Strict dependency
do_compile[depends] += "virtual/bootloader:do_deploy"


PV = "1.0+git${SRCPV}"
SRCREV="${AUTOREV}"
#SRCREV="a165f67e9a62b453d0cdaf10f464b9d1307a39e9"
SRC_URI = "git://git@bitbucket.microchip.com/fpga_pfsoc_es/hart-software-services.git;protocol=ssh;branch=aloevera-polarfiresoc-merge \
	   "

SRC_URI_append_lc-mpfs = " \
    file://default_config \
"
SRC_URI_append_mpfs = " \
    file://default_config \
"

SRC_URI_append_icicle-kit-es = " \
    file://def_config \
    file://mpfs_configuration_generator.py \
    file://ICICLE_MSS_0.xml \
"

SRC_URI_append_icicle-kit-es-sd = " \
    file://def_config \
    file://mpfs_configuration_generator.py \
    file://ICICLE_MSS_0.xml \
"
S = "${WORKDIR}/git"

RDEPENDS_${PN} += " \
    python3-psutil \
"

EXTRA_OEMAKE = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${TARGET_PREFIX}gcc ${TOOLCHAIN_OPTIONS}" V=1'
EXTRA_OEMAKE += 'HOSTCC="${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}"'
EXTRA_OEMAKE += 'PYTHON=nativepython STAGING_INCDIR=${STAGING_INCDIR_NATIVE} STAGING_LIBDIR=${STAGING_LIBDIR_NATIVE}'


# NOTE: this is a Makefile-only piece of software, so we cannot generate much of the
# recipe automatically - you will need to examine the Makefile yourself and ensure
# that the appropriate arguments are passed in.

do_configure_icicle-kit-es () {

	## Parsing the Polarfore SoC XML Hardware Configuration file from Libero Design
	##echo "Creating Polarfore SoC XML Hardware Configuration folder "
	#python3 ${WORKDIR}/mpfs_configuration_generator.py ${WORKDIR}/ICICLE_MSS_0.xml
	#if [ -d ${WORKDIR}/git/hardware ]; then
	#if [ -d ${WORKDIR}/git/boards/${MACHINE}/config/hardware ]; then
  	## force remove the hardware directory
	#rm -rf ${WORKDIR}/git/boards/${MACHINE}/config/hardware
	#fi
	# Finally move the configuration over to HSS configuration folder
	#mv -f ${WORKDIR}/git/hardware ${WORKDIR}/git/boards/${MACHINE}/config/hardware
	#fi ## Finished if hardware folder generated from xml

	# Specify any needed configure commands here
	## We can use the yocto config in recipes_bsp/hss/files
	##cp -f ${WORKDIR}/defconfig  ${WORKDIR}/git/.config
	## For now use default configuration from HSS
	cp -f ${WORKDIR}/git/boards/icicle-kit-es/def_config  ${WORKDIR}/git/.config

	## HSS uses kconfiglib, must be installed in default location ~/.local/bin/
	## Create a symbolic link to the tool
	if [ ! -f ${TOPDIR}/tmp-glibc/hosttools/genconfig ]; then
	ln -s ~/.local/bin/genconfig ${TOPDIR}/tmp-glibc/hosttools/
    fi

	# Clear the old config file
	if [ -f ${WORKDIR}/git/config.h ]; then
	rm -f ${WORKDIR}/git/config.h
    fi
}
do_configure_icicle-kit-es-sd () {

	## Parsing the Polarfore SoC XML Hardware Configuration file from Libero Design
	##echo "Creating Polarfore SoC XML Hardware Configuration folder "
	python3 ${WORKDIR}/mpfs_configuration_generator.py ${WORKDIR}/ICICLE_MSS_0.xml
	if [ -d ${WORKDIR}/git/hardware ]; then
	if [ -d ${WORKDIR}/git/boards/icicle-kit-es/config/hardware ]; then
  	# force remove the hardware directory
	rm -rf ${WORKDIR}/git/boards/icicle-kit-es/config/hardware
	fi
	# Finally move the configuration over to HSS configuration folder
	mv -f ${WORKDIR}/git/hardware ${WORKDIR}/git/boards/icicle-kit-es/config
	fi ## Finished if hardware folder generated from xml

	# Specify any needed configure commands here
	## We can use the yocto config in recipes_bsp/hss/files
	##cp -f ${WORKDIR}/default_config  ${WORKDIR}/git/.config
	## For now use default configuration from HSS
	cp -f ${WORKDIR}/git/boards/icicle-kit-es/def_config.sdcard  ${WORKDIR}/git/.config

	## HSS uses kconfiglib, must be installed in default location ~/.local/bin/
	## Create a symbolic link to the tool
	if [ ! -f ${TOPDIR}/tmp-glibc/hosttools/genconfig ]; then
	ln -s ~/.local/bin/genconfig ${TOPDIR}/tmp-glibc/hosttools/
    fi

	# Clear the old config file
	if [ -f ${WORKDIR}/git/config.h ]; then
	rm -f ${WORKDIR}/git/config.h
    fi
}
do_configure_mpfs () {
	# Specify any needed configure commands here
	###cp  ${WORKDIR}/hss.config  ${WORKDIR}/git/.config
	cp -f ${WORKDIR}/default_config  ${WORKDIR}/git/.config
	## HSS uses kconfiglib, must be installed in default location ~/.local/bin/
	## Create a symbolic link to the tool
	if [ ! -f ${TOPDIR}/tmp-glibc/hosttools/genconfig ]; then
	ln -s ~/.local/bin/genconfig ${TOPDIR}/tmp-glibc/hosttools/
        fi

	# Clear the old config file
	if [ -f ${WORKDIR}/git/config.h ]; then
	rm -f ${WORKDIR}/git/config.h
        fi
}

do_configure_lc-mpfs () {
	# Specify any needed configure commands here
	###cp  ${WORKDIR}/hss.config  ${WORKDIR}/git/.config
	cp -f ${WORKDIR}/default_config  ${WORKDIR}/git/.config
	## HSS uses kconfiglib, must be installed in default location ~/.local/bin/
	## Create a symbolic link to the tool
	if [ ! -f ${TOPDIR}/tmp-glibc/hosttools/genconfig ]; then
	ln -s ~/.local/bin/genconfig ${TOPDIR}/tmp-glibc/hosttools/
        fi

	# Clear the old config file
	if [ -f ${WORKDIR}/git/config.h ]; then
	rm -f ${WORKDIR}/git/config.h
        fi
}

do_compile_icicle-kit-es-sd () {

	## Creating the config for HSS
	oe_runmake BOARD=icicle-kit-es genconfig
	## Adding u-boot as a payload
	## Using bin2chunks application
	make -C ${WORKDIR}/git/tools/bin2chunks      
	${WORKDIR}/git/tools/bin2chunks/bin2chunks 0x80200000 0x80200000 0x80200000 0x80200000 32768 ${WORKDIR}/git/tools/bin2chunks/payload.bin 1 1 ${DEPLOY_DIR_IMAGE}/u-boot.bin 0x80200000
        cp -f ${WORKDIR}/git/tools/bin2chunks/payload.bin ${WORKDIR}/git/
	oe_runmake BOARD=icicle-kit-es

}
do_compile_icicle-kit-es () {

	## Creating the config for HSS
	oe_runmake BOARD=${MACHINE} genconfig
	## Adding u-boot as a payload
	## Using bin2chunks application
	make -C ${WORKDIR}/git/tools/bin2chunks      
	${WORKDIR}/git/tools/bin2chunks/bin2chunks 0x80200000 0x80200000 0x80200000 0x80200000 32768 ${WORKDIR}/git/tools/bin2chunks/payload.bin 1 1 ${DEPLOY_DIR_IMAGE}/u-boot.bin 0x80200000
        cp -f ${WORKDIR}/git/tools/bin2chunks/payload.bin ${WORKDIR}/git/
	oe_runmake BOARD=${MACHINE}

}
do_compile_mpfs () {

	## Creating the config for HSS
	oe_runmake BOARD=${MACHINE} genconfig
	## Adding u-boot as a payload
	## Using bin2chunks application
	make -C ${WORKDIR}/git/tools/bin2chunks      
	${WORKDIR}/git/tools/bin2chunks/bin2chunks 0x80200000 0x80200000 0x80200000 0x80200000 32768 ${WORKDIR}/git/tools/bin2chunks/payload.bin 1 1 ${DEPLOY_DIR_IMAGE}/u-boot.bin 0x80200000
        cp -f ${WORKDIR}/git/tools/bin2chunks/payload.bin ${WORKDIR}/git/
	oe_runmake BOARD=${MACHINE}

}
do_compile_lc-mpfs () {

	## Creating the config for HSS
	oe_runmake BOARD=${MACHINE} genconfig
	## Adding u-boot as a payload
	## Using bin2chunks application
	make -C ${WORKDIR}/git/tools/bin2chunks      
	${WORKDIR}/git/tools/bin2chunks/bin2chunks 0x80200000 0x80200000 0x80200000 0x80200000 32768 ${WORKDIR}/git/tools/bin2chunks/payload.bin 1 1 ${DEPLOY_DIR_IMAGE}/u-boot.bin 0x80200000
        cp -f ${WORKDIR}/git/tools/bin2chunks/payload.bin ${WORKDIR}/git/
	oe_runmake BOARD=${MACHINE}

}

do_install() {
	install -d ${DEPLOY_DIR_IMAGE}
	install -m 755 ${WORKDIR}/git/hss.* ${DEPLOY_DIR_IMAGE}/
	install -m 755 ${WORKDIR}/git/payload.bin ${DEPLOY_DIR_IMAGE}/
}

