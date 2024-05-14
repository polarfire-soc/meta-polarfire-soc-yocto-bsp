#!/bin/bash

CONFFILE="conf/auto.conf"
INITRAMFS_CONF="conf/initramfs.conf"
BITBAKEIMAGE="mpfs-dev-cli"

unset MACHINE
unset mpfs_setup_error
unset mpfs_setup_help
unset BUILD_DIR

usage()
{
  echo -e "\nUsage: source polarfire-soc_yocto_setup.sh
       Optional parameters: [-b build-dir] [-m machine] [-h]"
  echo "
    * [-b build-dir]: Build directory, if unspecified script uses 'build' as output directory
    * [-m machine]: Machine target, if unspecified script uses 'icicle-kit-es' as the default machine
    * [-h]: help
  "
}

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

OPTIND=1
while getopts ":hb:m:" option; do
   case $option in
      h) 
         mpfs_setup_help=1
         usage;;
      b) 
         BUILD_DIR=$OPTARG
         ;;
      m) 
         MACHINE=$OPTARG
         ;;
     \?) # Invalid option
         echo "Error: Invalid option"
         mpfs_setup_error=1
         ;;
   esac
done

if [[ "$mpfs_setup_error" != "1" && "$mpfs_setup_help" != "1" ]]; then

if [ -z "$BUILD_DIR" ]; then
    BUILD_DIR='build'
fi

if [ -z "$MACHINE" ]; then
    MACHINE='icicle-kit-es'
fi

# bootstrap OE
echo "Init OE"
export BASH_SOURCE="openembedded-core/oe-init-build-env"
. ./openembedded-core/oe-init-build-env $BUILD_DIR

# Symlink the cache
#echo "Setup symlink for sstate"
#ln -s ~/sstate/${MACHINE} sstate-cache

# add the missing layers
echo "Adding layers"
bitbake-layers add-layer ../meta-openembedded/meta-oe
bitbake-layers add-layer ../meta-openembedded/meta-python
bitbake-layers add-layer ../meta-openembedded/meta-multimedia
bitbake-layers add-layer ../meta-openembedded/meta-networking
bitbake-layers add-layer ../meta-openembedded/meta-webserver
bitbake-layers add-layer ../meta-polarfire-soc-yocto-bsp/meta-polarfire-soc-bsp
bitbake-layers add-layer ../meta-polarfire-soc-yocto-bsp/meta-polarfire-soc-community
bitbake-layers add-layer ../meta-polarfire-soc-yocto-bsp/meta-polarfire-soc-extras


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
EXTRA_IMAGE_FEATURES:append = " ssh-server-openssh package-management"
PACKAGECONFIG:append:pn-qemu-native = " sdl"
PACKAGECONFIG:append:pn-nativesdk-qemu = " sdl"
USER_CLASSES:append = " buildstats buildhistory buildstats-summary"

require conf/distro/include/no-static-libs.inc
require conf/distro/include/yocto-uninative.inc
require conf/distro/include/security_flags.inc
INHERIT += "uninative"
DISTRO_FEATURES:append = " largefile multiarch pam systemd "
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"
VIRTUAL-RUNTIME_init_manager = "systemd"
HOSTTOOLS_NONFATAL:append = " ssh"
LICENSE_FLAGS_ACCEPTED = "commercial_ffmpeg"
EOF

echo "Creating initramfs.conf"

if [ -e $INITRAMFS_CONF ]; then
    rm -rf $INITRAMFS_CONF
fi
cat <<EOF > $INITRAMFS_CONF
INITRAMFS_IMAGE = "mpfs-core-image-base"
EOF


echo "To build an image run"
echo "---------------------------------------------------"
echo "MACHINE=${MACHINE} bitbake ${BITBAKEIMAGE}"
echo "---------------------------------------------------"
echo ""
echo "Buildable machine info"
echo "---------------------------------------------------"
echo " Default MACHINE=${MACHINE}"
echo "* icicle-kit-es: Microchip Polarfire SoC Icicle Kit Engineering Sample"
echo "* icicle-kit-es-amp: Microchip Polarfire SoC Icicle Kit Engineering Sample in AMP mode"
echo "* icicle-kit-es-auth: Microchip Polarfire SoC Icicle Kit Engineering Sample in authenticated boot mode"
echo "* mpfs-disco-kit: Microchip Polarfire SoC Discovery Kit"
echo "* mpfs-disco-kit-amp: Microchip Polarfire SoC Discovery Kit in AMP mode"
echo "* mpfs-video-kit: Microchip Polarfire SoC Video Kit"
echo "---------------------------------------------------"
echo "Bitbake Image"
echo "---------------------------------------------------"
echo "* mpfs-dev-cli: MPFS Linux console-only image with development packages."
echo "* mpfs-initramfs-image: A small RAM-based Root Filesystem (initramfs) image"
echo "* core-image-minimal-dev: OE console-only image, with some additional development packages."
echo "* core-image-minimal: OE console-only image"
echo "* core-image-full-cmdline: OE console-only image with more full-featured Linux system functionality installed."
echo "---------------------------------------------------"
fi


# start build
#echo "Starting build"
#bitbake $BITBAKEIMAGE

