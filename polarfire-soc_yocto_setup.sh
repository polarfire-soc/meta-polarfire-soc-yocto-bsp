#!/bin/bash

if [ "$1"  == "CISERVER" ]; then
echo "CI SERVER configuration to shared folders tbd"
dlDIR="$HOME/dldir"
sstateDIR="$HOME/sstate"
else
echo "Yocto Developer folders for dl / sstate"
dlDIR="$HOME/dldir"

sstateDIR="$HOME/sstate"
fi


# make sure sstate is there
echo "Creating sstate directory"
if [ ! -d $sstateDIR ]; then
 mkdir -p $sstateDIR
fi

# make sure dldir is there
if [ ! -d $dlDIR ]; then
 mkdir -p $dlDIR
fi

echo $(pwd)

. ./openembedded-core/oe-init-build-env

echo $(pwd)
# we will be in build here
if [ ! -d "./build" ]; then
 mkdir -p build
else
exit
fi


#cat <<EOF > ./conf/local.conf
echo 'DL_DIR ?= "'$dlDIR'"' >> ../build/conf/local.conf
echo 'SSTATE_DIR ?= "'$sstateDIR'"' >> ../build/conf/local.conf
#echo 'MACHINE ?= "qemuriscv64"' >> ../build/conf/local.conf
echo 'MACHINE ?= "mpfs"' >> ../build/conf/local.conf
echo 'EXTRA_IMAGE_FEATURES_append = " ssh-server-dropbear"' >> ../build/conf/local.conf
echo 'EXTRA_IMAGE_FEATURES_append = " package-management"' >> ../build/conf/local.conf
echo 'PACKAGECONFIG_append_pn-qemu-native = " sdl"' >> ../build/conf/local.conf
echo 'PACKAGECONFIG_append_pn-nativesdk-qemu = " sdl"' >> ../build/conf/local.conf
echo 'USER_CLASSES ?= "buildstats buildhistory buildstats-summary image-mklibs image-prelink"' >> ../build/conf/local.conf

# would question these requires, may not need
echo 'require conf/distro/include/no-static-libs.inc' >> ../build/conf/local.conf
echo 'require conf/distro/include/yocto-uninative.inc' >> ../build/conf/local.conf
echo 'require conf/distro/include/security_flags.inc' >> ../build/conf/local.conf
echo 'INHERIT += "uninative"' >> ../build/conf/local.conf

echo '# same here, check if these are really needed' >> ../build/conf/local.conf
echo 'DISTRO_FEATURES_append = " largefile opengl ptest multiarch wayland pam systemd " ' >> ../build/conf/local.conf
echo 'DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"' >> ../build/conf/local.conf

echo 'VIRTUAL-RUNTIME_init_manager = "systemd"' >> ../build/conf/local.conf
echo 'HOSTTOOLS_NONFATAL_append = " ssh"' >> ../build/conf/local.conf


#CI SERVER DOES NOT USE THIS, for developers only!
if [ "$1"  == "CISERVER" ]; then
echo "CI SERVER SHOULD NOT SET SSTATE_MIRRORS/ PREMIRRORS_prepend"
else
    echo "Developer setting up SSTATE mirrors and premirrors prepend"	
    echo 'SSTATE_MIRRORS ?= "\
    file://.* http://someserver.tld/share/sstate/PATH;downloadfilename=PATH \n \
    file://.* file:///some/local/dir/sstate/PATH"' >> ../build/conf/local.conf

    echo 'PREMIRRORS_prepend = "\
         git://.*/.* http://www.lewis.org/sources/ \n \
         ftp://.*/.* http://www.lewis.org/sources/ \n \
         http://.*/.* http://www.lewis.org/sources/ \n \
         https://.*/.* http://www.lewis.org/sources/ \n"' >> ./conf/local.conf


fi

cd ../build

# add the missing layers
echo "Adding layers"
bitbake-layers add-layer ../meta-openembedded/meta-oe
bitbake-layers add-layer ../meta-openembedded/meta-python
bitbake-layers add-layer ../meta-openembedded/meta-multimedia
bitbake-layers add-layer ../meta-openembedded/meta-networking
bitbake-layers add-layer ../meta-riscv
bitbake-layers add-layer ../polarfire-soc-yocto-bsp

