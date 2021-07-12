# Microchip Yocto BSP for the MPFS Polarfire-SoC


Support for Microchip Polarfire-SoC (MPFS-DEV-KIT and LC-MPFS-DEV-KIT) are available in Yocto/OE provided by either the OpenEmbedded Core or for additional and more complete support the meta-polarfire-soc-yocto-bsp layer. 
This Yocto BSP layer builds a complete compressed uSD Image for booting the development board.

The 'Polarfire SoC Yocto BSP' is build on top of the RISCV Architectural layer (meta-riscv) to provide additional hardware specific features. 
Using Yocto 'Openembedded' you will build the following:
  - RISCV Toolchain
  - Predefined Disk Images 
  - Bootloader Binaries (ZSBL, FSBL, OpenSBI, U-Boot)
  - Device Tree Binary (DTB)
  - Linux Kernel Images

Currently the following development platforms are supported:
- [MPFS-DEV-KIT](doc/MPFS-DEV-KIT_user_guide.md) (HiFive Unleashed Expansion Board)
- [LC-MPFS-DEV-KIT](doc/LC-MPFS-DEV-KIT_user_guide.md)

The complete User Guides for each development platform, containing board and boot instructions, are available in the `doc/` subdirectory. 


## Building Linux Using Yocto
This section describes the procedure to build the Disk image and loading it into an uSD card using
bitbake and standard disk utilities.

Yocto Release Activity:
Zeus (Revision 3.0)	(Released October 2019)

### Required packages for the Build Host
This document assumes you are running on a modern Linux system. The process documented here was tested using Ubuntu 18.04 LTS. 
It should also work with other Linux distributions if the equivalent prerequisite packages are installed.

Detailed instructions for various distributions can be found in "[Required Packages for the Build Host](https://www.yoctoproject.org/docs/3.0/ref-manual/ref-manual.html#required-packages-for-the-build-host)" section in Yocto Project Reference Manual.

### Dependencies

The BSP uses the Yocto RISCV Architecture Layer.
 For Ubuntu 18.04 (or newer) install python3-distutils package.

**Make sure to install the [repo command](https://source.android.com/setup/build/downloading#installing-repo) by Google first.**
 
### Supported Machine Targets
The `MACHINE` option can be used to set the target board for which linux is built, and if left blank it will default to `MACHINE=lc-mpfs`.           
The following table details the available targets:

| `MACHINE` | Board Name |
| --- | --- |
| `MACHINE=mpfs` | MPFS-DEV-KIT, HiFive Unleashed Expansion Board |
| `MACHINE=lc-mpfs` | LC-MPFS-DEV-KIT |
| `MACHINE=qemuriscv64` | Simulation |


## Linux Images

 - 'mpfs-dev-cli' A console image with development tools.
```
     *You can login with `root` account. The password is `microchip`.
```

```     
    * With the OE core-image-*  you can login with `root` account, there is no password set.
```
 - 'core-image-minimal' A small console image to allow you to boot.
 - 'core-image-full-cmdline' A console only image with more full Featured Linux support.

 For more information on available images refer to [Yocto reference manual](https://www.yoctoproject.org/docs/3.0/ref-manual/ref-manual.html#ref-images)

## Quick Start

### Create the Workspace

This needs to be done every time you want a clean setup based on the latest layers.

```bash
mkdir mpfs-yocto && cd mpfs-yocto
repo init -u https://github.com/polarfire-soc/meta-polarfire-soc-yocto-bsp -b mpfs-dev-kit -m tools/manifests/riscv-yocto.xml
repo sync
```

### Updating Existing Workspace

If you want to pull in the latest changes in all layers.

```bash
cd mpfs-yocto
repo sync
repo rebase
```

### Setting up Build Environment

```bash
cd mpfs-yocto
. ./meta-polarfire-soc-yocto-bsp/polarfire-soc_yocto_setup.sh
```

### Building Disk Image

Using yocto bitbake command and setting the MACHINE and image requried.

```bash
MACHINE=<machine> bitbake <image>
```
Example: MACHINE=lc-mpfs bitbake mpfs-dev-cli

### Yocto Image and Binaries directory
```
build/tmp-glibc/deploy/images/{MACHINE}
```

### Running wic.gz image on hardware

Compressed Disk images files use `<image>-<machine>.wic.gz` format, for example,

`mpfs-dev-cli-lc-mpfs.wic.gz`. We are interested in `.wic.gz` disk images for writing to uSD card.

> Be very careful while picking /dev/sdX device! Look at dmesg, lsblk, blkid, GNOME Disks, etc. before and after plugging in your uSD card to find a proper device. Double check it to avoid overwriting any of system disks/partitions!
> 
> Unmount any mounted partitions from uSD card before writing!
> 
> We advice to use 16GB or 32GB uSD cards. 8GB cards (shipped with HiFive Unleashed) can still be used with CLI images.

Example write the disk image to the uSD card:

```bash
zcat mpfs-dev-cli-lc-mpfs.wic.gz | sudo dd of=/dev/sdX bs=512K iflag=fullblock oflag=direct conv=fsync status=progress
```

## Run in Simulation (QEMU)

```bash
./openembedded-core/scripts/runqemu nographic
```

## Additional Reading

[Yocto User Manual](https://www.yoctoproject.org/docs/3.0/mega-manual/mega-manual.html) 

[Yocto Development Task Manual](https://www.yoctoproject.org/docs/3.0/dev-manual/dev-manual.html) 
 
[Yocto Bitbake User Manual](https://www.yoctoproject.org/docs/3.0/bitbake-user-manual/bitbake-user-manual.html)
 
[PolarFire SoC Buildroot BSP](https://github.com/polarfire-soc/polarfire-soc-buildroot-sdk) 
 
[MPFS-DEV-KIT User Guide](doc/MPFS-DEV-KIT_user_guide.md) 

[LC-MPFS-DEV-KIT User Guide](doc/LC-MPFS-DEV-KIT_user_guide.md) 

[U-Boot Documentation](https://www.denx.de/wiki/U-Boot/Documentation) 

[Kernel Documentation for v5.4](https://www.kernel.org/doc/html/v5.4/) 

