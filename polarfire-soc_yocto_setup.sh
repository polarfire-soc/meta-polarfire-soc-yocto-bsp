#!/bin/bash

DIR="build"
MACHINE="icicle-kit-es"
CONFFILE="conf/auto.conf"
INITRAMFS_CONF="conf/initramfs.conf"
BITBAKEIMAGE="mpfs-dev-cli"

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
bitbake-layers add-layer ../meta-polarfire-soc-yocto-bsp/meta-polarfire-soc-bsp
bitbake-layers add-layer ../meta-polarfire-soc-yocto-bsp/meta-polarfire-soc-community
bitbake-layers add-layer ../meta-polarfire-soc-yocto-bsp/meta-polarfire-soc-extras
bitbake-layers add-layer ../meta-openembedded/meta-webserver


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
PACKAGECONFIG:append:pn-qemu-native = " sdl"
PACKAGECONFIG:append:pn-nativesdk-qemu = " sdl"
USER_CLASSES:append = " buildstats buildhistory buildstats-summary"
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

# start build
#echo "Starting build"
#bitbake $BITBAKEIMAGE

