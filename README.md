
## Description
Yocto Project, through its standard layers mechanism, can directly accept the format described as a layer. The BSP (Board Support Package) captures all the hardware-specific details in one place in a standard format, which is useful for any person wishing to use the hardware platform regardless of the build system they are using.

Our BSP targets the mpfs 'PolarFire SoC'.

## Yocto Overview

For more information please refer to the [Yocto Project Quick Start guide](https://www.yoctoproject.org/docs/2.7/brief-yoctoprojectqs/brief-yoctoprojectqs.html).

For the host tools/packages refer the section on 'The Build Host Packages'

Release Activity:

Warrior	2.7	April 2019	2.7.0	Stable	21.0	1.42


## Private Repositories (ESSENTIAL SETUP)

This Yocto BSP pulls data from 'Microchip Bitbucket' private repositories, the recipes use ssh to clone the repositories. In order to work without entering username/password ssh key configuration is required.

Follow the instructions from github on [configuring an ssh key](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent).


Summary of commands:
```
#generate the key
$ ssh-keygen -t rsa -b 4096 -C "your_email@example.com" -f ~/.ssh/filename

#Check the agent pid
eval $(ssh-agent -s)

#Copy the filename.pub to the Bitbucket account.
xcopy ~/.ssh/filename.pub

#Verify ssh clone of repository
git clone ssh://git@bitbucket.microchip.com/fpga_pfsoc_es/polarfire-soc-yocto-bsp.git
```

If this is not setup correctly you will see the following error:
```
Host key verification failed.
fatal: Could not read from remote repository.
```

## Dependencies

The BSP uses the Yocto RISCV Architecture Layer.

Make sure to install the [repo command](https://source.android.com/setup/build/downloading#installing-repo) by Google first.


Build dependencies:
```
build-essential git autotools texinfo bison flex libgmp-dev libmpfr-dev libmpc-dev gawk libz-dev libssl-dev
```
Additional build deps for QEMU: 
```
libglib2.0-dev libpixman-1-dev
```

## Quick Start
Step 1: Create the Workspace, only needs to executed once.
	Continue to Step 2 if Workspace already created.

Step 2: 
	Update Existing Workspace.
	Setup Build Environment

Finally Build the Image, note the machine can also be set on the command line if required.

## Create the Workspace

Note: You only need this if you do not have an existing Yocto Project build environment.
```
mkdir mpfs-yocto && cd mpfs-yocto


repo init -u https://bitbucket.microchip.com/scm/fpga_pfsoc_es/polarfire-soc-yocto-bsp -b master -m tools/manifests/riscv-yocto.xml

repo sync
repo start work --all
```

## Update Existing Workspace

In order to bring all layers up to date with upstream
```
cd mpfs-yocto
repo sync
repo rebase
```

## Setup Build Environment
Run the setup script and source the build environment
```
./polarfire-soc-yocto-bsp/polarfire-soc_yocto_setup.sh
. ./openembedded-core/oe-init-build-env
```

## Build a console-only image
```
MACHINE=mpfs bitbake core-image-full-cmdline
```

Default machine is for mpfs (Aloevera) hardware. 

Other Machines you may want to test with:

* lc-mpfs
* qemuriscv64


## Yocto Build Output
The OpenEmbedded build system creates the Build Directory when you run the build environment setup scripts (i.e. oe-init-build-env).

If you do not give the Build Directory a specific name when you run a setup script, the name defaults to "build".


The OpenEmbedded build system creates this directory when you enable the build history feature. The directory tracks build information into image, packages, and SDK subdirectories. For information on the build history feature, see the "Maintaining Build Output Quality" section in the Yocto Project Development Tasks Manual.

```
build/conf/local.conf
```
This configuration file contains all the local user configurations for your build environment. The local.conf file contains documentation on the various configuration options. Any variable set here overrides any variable set elsewhere within the environment unless that variable is hard-coded within a file (e.g. by using '=' instead of '?='). Some variables are hard-coded for various reasons but these variables are relatively rare.

```
build/conf/bblayers.conf
```
This configuration file defines layers, which are directory trees, traversed (or walked) by BitBake. The bblayers.conf file uses the BBLAYERS variable to list the layers BitBake tries to find.

If bblayers.conf is not present when you start the build, the OpenEmbedded build system creates it from bblayers.conf.sample when you source the top-level build environment setup script (i.e. oe-init-build-env).

```
build/tmp-glibc/
```
The OpenEmbedded build system creates and uses this directory for all the build system's output. The TMPDIR variable points to this directory.

BitBake creates this directory if it does not exist. As a last resort, to clean up a build and start it from scratch (other than the downloads), you can remove everything in the tmp directory or get rid of the directory completely. If you do, you should also completely remove the build/sstate-cache directory.

```
build/tmp-glibc/deploy/
```
This directory contains any "end result" output from the OpenEmbedded build process. The DEPLOY_DIR variable points to this directory. For more detail on the contents of the deploy directory, see the "Images" and "Application Development SDK" sections in the Yocto Project Overview and Concepts Manual.

```
build/tmp-glibc/deploy/images/{MACHINE}
```
This directory receives complete filesystem images. If you want to flash the resulting image from a build onto a device, look here for the image.

Be careful when deleting files in this directory. You can safely delete old images from this directory (e.g. core-image-\*). However, the kernel (*zImage*, *uImage*, etc.), bootloader and other supplementary files might be deployed here prior to building an image. Because these files are not directly produced from the image, if you delete them they will not be automatically re-created when you build the image again.

If you do accidentally delete files here, you will need to force them to be re-created. In order to do that, you will need to know the target that produced them. For example, these commands rebuild and re-create the kernel files:


## Yocto Image and Binaries directory
```
build/tmp-glibc/deploy/images/{MACHINE}
```

Files of Interest:
* U-Boot-{MACHINE}-2020.01.r0.bin
* uImage (Link to Linux binary, .bin)
* DTB {MACHINE}.dtb

If using the SD card: 
core-image-xxxxxxx-mpfs.wic.gz 
   


## Run in QEMU
Simulation
```
./openembedded-core/scripts/runqemu nographic
```

## Running wic.gz image on hardware

The output of the build will be a ```<image>.wic.gz``` file. You can write this file to an sd card using:

```
$ zcat <image>-<machine>.wic.gz | sudo dd of=/dev/sdX bs=4M iflag=fullblock oflag=direct conv=fsync status=progress
```
