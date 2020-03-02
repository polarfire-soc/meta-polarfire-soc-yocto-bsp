# Microchip Yocto BSP for the MPFS Polarfire-SoC

The 'Polarfire SoC Yocto BSP' is build on top of RISCV Architectural layer (meta-riscv) to provide additional hardware specific features. 
Using Yocto 'Openembedded' you will build the following:

  - Predefined Disk Images 
  - Bootloader Binaries (ZSBL, FSBL, OpenSBI, U-Boot)
  - Device Tree Binary (DTB)
  - Linux Kernel Images

BSP allows you to easily modify U-Boot tftp configuration or disk image configuration. 

Yocto Release Activity:
Zeus (Revision 3.0)	(Released October 2019)

## Building Linux Using Yocto
This section describes the procedure to build the Disk image and loading it into an SD card using
bitbake and standard disk utilities.

### Supported Build Hosts
This document assumes you are running on a modern Linux system. The process documented here was tested using Ubuntu 18.04 LTS. It should also work with other Linux distributions if the equivalent prerequisite packages are installed.

#### Tested Build Hosts:

Ubuntu 18.04 x86_64 host - Working.

Ubuntu 16.04 x86_64 host - Working.

### Required packages for the Build Host
 Please refer to the [Yocto reference manual](https://www.yoctoproject.org/docs/3.0/ref-manual/ref-manual.html#required-packages-for-the-build-host)

## Dependencies

The BSP uses the Yocto RISCV Architecture Layer.

Make sure to install the [repo command](https://source.android.com/setup/build/downloading#installing-repo) by Google first.

 
### Supported Machine Targets
The `MACHINE` option can be used to set the target board for which linux is built, and if left blank it will default to `MACHINE=lc-mpfs`.           
The following table details the available targets:

| `MACHINE` | Board Name |
| --- | --- |
| `MACHINE=mpfs` | MPFS-DEV-KIT, HiFive Unleashed Expansion Board |
| `MACHINE=lc-mpfs` | LC-MPFS-DEV-KIT |
| `MACHINE=qemuriscv64` | Simulation |


## Quick Start
Step 1: Create the Workspace, only needs to executed once.
	Continue to Step 2 if Workspace already created.

Step 2: 
	Update Existing Workspace.
	Setup Build Environment

## Create the Workspace

Note: You only need this if you do not have an existing Yocto Project build environment.
```
mkdir mpfs-yocto && cd mpfs-yocto

repo init -u https://bitbucket.microchip.com/scm/fpga_pfsoc_es/polarfire-soc-yocto-bsp -b master -m tools/manifests/riscv-yocto.xml

repo sync

./polarfire-soc-yocto-bsp/polarfire-soc_yocto_setup.sh


To make modifications to working branches in all repositories issue the following:

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
. ./openembedded-core/oe-init-build-env
```

## Build the sources
Using yocto bitbake command and setting the MACHINE and image requried.
For detailed information on Bitbake refer to the [user manual](https://www.yoctoproject.org/docs/3.0/bitbake-user-manual/bitbake-user-manual.html) 
```
MACHINE=<machine> bitbake <image>

Example: MACHINE=lc-mpfs bitbake mpfs-dev-cli
```

## Images

 - 'mpfs-dev-cli' A console image with development tools.
```
     *You can login with `root` account. The password is `microchip`.
```

```     
    * With the OE core-image-*  you can login with `root` account, there is no password set.
```
 - 'core_image_minimal' A small console image to allow you to boot.
 - 'core_image_full_cmdline' A console only image with more full Featured Linux support.

 For more information on a available images refer to [Yocto reference manual](https://www.yoctoproject.org/docs/3.0/ref-manual/ref-manual.html#ref-images)


## Yocto Image and Binaries directory
```
build/tmp-glibc/deploy/images/{MACHINE}
```

### Running wic.gz image on hardware

Disk images files use `<image>-<machine>.wic.gz` format, for example,

`mpfs-dev-cli-lc-mpfs.wic.gz`. We are interested in `.wic.gz` disk images for writing to uSD card.

> Be very careful while picking /dev/sdX device! Look at dmesg, lsblk, blkid, GNOME Disks, etc. before and after plugging in your uSD card to find a proper device. Double check it to avoid overwriting any of system disks/partitions!
> 
> Unmount any mounted partitions from uSD card before writing!
> 
> We advice to use 16GB or 32GB uSD cards. 8GB cards (shipped with HiFive Unleashed) can still be used with CLI images.

Example write uSD card:

```bash
zcat mpfs-dev-cli-lc-mpfs.wic.gz | sudo dd of=/dev/sdX bs=512K iflag=fullblock oflag=direct conv=fsync status=progress
```

You will need to modify MSEL to allow using FSBL and OpenSBI + U-Boot bootloaders from uSD card instead of SPI NAND chip:

```
      USB   LED    Mode Select                  Ethernet
 +===|___|==****==+-+-+-+-+-+-+=================|******|===+
 |                | | | | |X| |                 |      |   |
 |                | | | | | | |                 |      |   |
 |        HFXSEL->|X|X|X|X| |X|                 |______|   |
 |                +-+-+-+-+-+-+                            |
 |        RTCSEL-----/ 0 1 2 3 <--MSEL                     |
 |                                                         |
```

## Run in QEMU
Simulation
```
./openembedded-core/scripts/runqemu nographic
```

## Some Usefull bitbake commands.
Bake an image (add -k to continue building even errors are found in the tasks execution)
```
bitbake <image>
```


Execute a particular package's task. Default Tasks names: clean, fetch, unpack, patch, configure, compile, install, package, package_write, and build.
```
bitbake <package> -c <task>

Example: To (force) compiling the mpfs kernel (virtual/kernel) and then build, type:

$ bitbake  mpfs-linux -f -c compile

$ bitbake mpfs-linux
```


Look at the layers in the build
```
bitbake-layers show-layers

Check if certain package is present on current Yocto Setup
bitbake -s | grep <package>
```


