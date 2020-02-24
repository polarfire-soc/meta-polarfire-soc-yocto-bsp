
## Description
Yocto Project, through its standard layers mechanism, can directly accept the format described as a layer. The BSP (Board Support Package) captures all the hardware-specific details in one place in a standard format, which is useful for any person wishing to use the hardware platform regardless of the build system they are using.

The polarfire-soc-yocto-bsp is a minimal layer on top of meta-riscv to provide additional modifications:
 - Out of kernel device tree & config support.
 - Additional Kernel support for Polarfire SoC

Our BSP targets the mpfs 'PolarFire SoC'.

## Yocto Overview

For more information please refer to the [Yocto Project Quick Start guide](https://www.yoctoproject.org/docs/2.7/brief-yoctoprojectqs/brief-yoctoprojectqs.html).

For the host tools/packages refer the section on 'The Build Host Packages'

Release Activity:

Zeus	3.0


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
build/tmp-glibc/deploy/images/{MACHINE}
```
This directory receives complete filesystem images. If you want to flash the resulting image from a build onto a device, look here for the image.

Be careful when deleting files in this directory. You can safely delete old images from this directory (e.g. core-image-\*). However, the kernel (*zImage*, *uImage*, etc.), bootloader and other supplementary files might be deployed here prior to building an image. Because these files are not directly produced from the image, if you delete them they will not be automatically re-created when you build the image again.

If you do accidentally delete files here, you will need to force them to be re-created. In order to do that, you will need to know the target that produced them. For example, these commands rebuild and re-create the kernel files:


## Using Devtool to modify kernel Sources
 Suppose you have a requirement of customizing the existing kernel sources, 
devtool will be the best option to go, as it will do all the steps required to create a patch, bbappend file etc.

Steps:

    From your build directory run "devtool modify mpfs-linux".
    It will create a workspace directory containing the kernel sources in build directory

cd workspace/sources/mpfs-linux

    Modify the sources as per your requirement
    Build the updated kernel by running the following command: "devtool build mpfs-linux"
    If the build is successful, you can generate the yocto image, by running: "devtool build-image core-image-minimal"
    After you test your image, by flashing on the hardware and found everything is working as per your requirement, you can instruct devtool to add the patches to your own layer
        git status
        git add <files>
        git commit -m <message>
        devtool finish mpfs-linux polarfire-soc-yocto-bsp
    Once the above command finishes, the patches and .bbapend files are added to polarfire-soc-yocto-bsp/recipes-kernel/linux directory.



## Run in QEMU
Simulation
```
./openembedded-core/scripts/runqemu nographic
```

## Running wic.gz image on hardware

The output of the build will be a ```<image><machine>.wic.gz``` file. You can write this file to an sd card using:

```
$ zcat <image>-<machine>.wic.gz | sudo dd of=/dev/sdX bs=4M iflag=fullblock oflag=direct conv=fsync status=progress
```

Example: Image name: core-image-full-cmdline, MACHINE=lc-mpfs, SD Card /sdb

zcat core-image-full-cmdline-lc-mpfs.wic.gz | sudo dd of=/dev/sdb bs=4M iflag=fullblock oflag=direct conv=fsync status=progress

You will need to modify MSEL to allow using FSBL and OpenSBI + U-Boot bootloaders from uSD card instead of SPI NAND chip:

      USB   LED    Mode Select                  Ethernet
 +===|___|==****==+-+-+-+-+-+-+=================|******|===+
 |                | | | | |X| |                 |      |   |
 |                | | | | | | |                 |      |   |
 |        HFXSEL->|X|X|X|X| |X|                 |______|   |
 |                +-+-+-+-+-+-+                            |
 |        RTCSEL-----/ 0 1 2 3 <--MSEL                     |
 |                                                         |

You can login with root account. Password set for root account is 'microchip'. SSH daemon is started automatically.

