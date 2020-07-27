#!/bin/bash

DIR="build"
MACHINE="icicle-kit-es"
CONFFILE="conf/auto.conf"
BITBAKEIMAGE="mpfs-dev-cli"

# clean up the output dir
#echo "Cleaning build dir"
#rm -rf $DIR

# make sure sstate is there
#echo "Creating sstate directory"
#mkdir -p ~/sstate/$MACHINE

echo $(pwd)

# Reconfigure dash on debian-like systems
which aptitude > /dev/null 2>&1
ret=$?
if [ "$(readlink /bin/sh)" = "dash" -a "$ret" = "0" ]; then
  sudo aptitude install expect -y
  expect -c 'spawn sudo dpkg-reconfigure -freadline dash; send "n\n"; interact;'
elif [ "${0##*/}" = "dash" ]; then
  echo "dash as default shell is not supported"
  return
fi
# bootstrap OE
echo "Init OE"
export BASH_SOURCE="openembedded-core/oe-init-build-env"
. ./openembedded-core/oe-init-build-env $DIR

# Symlink the cache
#echo "Setup symlink for sstate"
#ln -s ~/sstate/${MACHINE} sstate-cache

# add the missing layers
echo "Adding layers"
bitbake-layers add-layer ../meta-openembedded/meta-oe
bitbake-layers add-layer ../meta-openembedded/meta-python
bitbake-layers add-layer ../meta-openembedded/meta-multimedia
bitbake-layers add-layer ../meta-openembedded/meta-networking
bitbake-layers add-layer ../meta-riscv
bitbake-layers add-layer ../meta-polarfire-soc-yocto-bsp



# fix the configuration
echo "Creating auto.conf"

if [ -e $CONFFILE ]; then
    rm -rf $CONFFILE
fi
cat <<EOF > $CONFFILE
MACHINE ?= "${MACHINE}"
#IMAGE_FEATURES += "tools-debug"
#IMAGE_FEATURES += "tools-tweaks"
#IMAGE_FEATURES += "dbg-pkgs"
# rootfs for debugging
#IMAGE_GEN_DEBUGFS = "1"
#IMAGE_FSTYPES_DEBUGFS = "tar.gz"
IMAGE_INSTALL_append = " python3"
IMAGE_INSTALL_append = " python3-requests"
EXTRA_IMAGE_FEATURES_append = " ssh-server-dropbear"
EXTRA_IMAGE_FEATURES_append = " package-management"
PACKAGECONFIG_append_pn-qemu-native = " sdl"
PACKAGECONFIG_append_pn-nativesdk-qemu = " sdl"
USER_CLASSES ?= "buildstats buildhistory buildstats-summary image-mklibs image-prelink"

require conf/distro/include/no-static-libs.inc
require conf/distro/include/yocto-uninative.inc
require conf/distro/include/security_flags.inc

INHERIT += "uninative"
INHERIT += " pypi setuptools"


DISTRO_FEATURES_append = " largefile opengl ptest multiarch wayland pam  systemd "
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"
VIRTUAL-RUNTIME_init_manager = "systemd"
HOSTTOOLS_NONFATAL_append = " ssh"
#DEBUG_BUILD = "1"
EOF


echo "To build an image run"
echo "---------------------------------------------------"
echo "MACHINE=${MACHINE} bitbake ${BITBAKEIMAGE}"
echo "---------------------------------------------------"
echo ""
echo "Buildable machine info"
echo "---------------------------------------------------"
echo " Default MACHINE=${MACHINE}"
echo "* icicle-kit-es: Microchip Polarfire SoC Icicle Kit Engineering Sample."
echo "* mpfs: HiFive Unleashed board with the Microsemi’s HiFive Unleashed Expansion kit."
echo "* lc-mpfs: Microchip’s PolarFire SoC FPGA using the Sifive U540 processor on a single board."
echo "* qemuriscv64: The 64-bit RISC-V machine"
echo "---------------------------------------------------"
echo "Bitbake Image"
echo "---------------------------------------------------"
echo "* mpfs-dev-cli: MPFS Linux console-only with development tools Image."
echo "* core-image-minimal: OE console-only image"
echo "* core-image-full-cmdline: OE console-only image with more full-featured Linux system functionality installed."
echo "* qemuriscv64: The 64-bit RISC-V machine"
echo "---------------------------------------------------"

# start build
#echo "Starting build"
#bitbake $BITBAKEIMAGE

