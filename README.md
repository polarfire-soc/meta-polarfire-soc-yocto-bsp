# Microchip Yocto BSP for the MPFS Polarfire-SoC


Support for Microchip Polarfire-SoC are available in Yocto/OE provided by either the OpenEmbedded Core or for additional and more complete support the meta-polarfire-soc-yocto-bsp layer. 
This Yocto BSP layer builds a complete compressed Image for booting the development board.

The 'Polarfire SoC Yocto BSP' is build on top of the RISCV Architectural layer (meta-riscv) to provide additional hardware specific features. 
Using Yocto 'Openembedded' you will build the following:
  - RISCV Toolchain
  - Predefined Disk Images 
  - Bootloader Binaries (FSBL, HSS, U-Boot)
  - Device Tree Binary (DTB)
  - Linux Kernel Images

The complete User Guides for each development platform, containing board and boot instructions, are available for the following supported platforms:

- [MPFS-DEV-KIT](https://github.com/polarfire-soc/polarfire-soc-documentation/blob/master/boards/mpfs-dev-kit/MPFS-DEV-KIT_user_guide.md) (HiFive Unleashed Expansion Board)
- [LC-MPFS-DEV-KIT](doc/LC-MPFS-DEV-KIT_user_guide.md)
- [ICICLE-KIT-ES] (tbd)

## Build Instructions (Installation and further details below)

### Create the Workspace
This needs to be done every time you want a clean setup based on the latest layers.
```bash
mkdir yocto-dev && cd yocto-dev
repo init -u https://bitbucket.microchip.com/scm/fpga_pfsoc_es/meta-polarfire-soc-yocto-bsp.git -b develop_icicle-kit-es -m tools/manifests/icicle.xml
```
### Update the repo workspace
```bash
repo sync
repo rebase
```
### Setup Bitbake environment
```bash
. ./meta-polarfire-soc-yocto-bsp/polarfire-soc_yocto_setup.sh
```
### Building board Disk Image
Using yocto bitbake command and setting the MACHINE and image requried.
```bash
MACHINE=icicle-kit-es bitbake mpfs-dev-cli
```
### Copy the created Disk Image to flash device (USB mmc flash/SD/uSD)

> Be very careful while picking /dev/sdX device! Look at dmesg, lsblk, GNOME Disks, etc. before and after plugging in your usb flash device/uSD/SD to find a proper device. Double check it to avoid overwriting any of system disks/partitions!
> 
```bash
cd yocto-dev/build
zcat tmp-glibc/deploy/images/icicle-kit-es/mpfs-dev-cli-icicle-kit-es.wic.gz | sudo dd of=/dev/sdX bs=512 iflag=fullblock oflag=direct conv=fsync status=progress
```

## Yocto Setup and BSP 

Yocto Release Activity:
Dunfell (Revision 3.1)	(Released April 2020)

### Required packages for the Build Host
This document assumes you are running on a modern Linux system. The process documented here was tested using Ubuntu 18.04 LTS. 
It should also work with other Linux distributions if the equivalent prerequisite packages are installed.

Detailed instructions for various distributions can be found in "[Required Packages for the Build Host](https://www.yoctoproject.org/docs/3.1/ref-manual/ref-manual.html#required-packages-for-the-build-host)" section in Yocto Project Reference Manual.

### Dependencies

The BSP uses the Yocto RISCV Architecture Layer.
 For Ubuntu 18.04 (or newer) install python3-distutils package.

**Make sure to install the [repo command](https://source.android.com/setup/build/downloading#installing-repo) by Google first.**

**The Microchip 'Hart Software Services' (HSS) requires the Python library kconfiglib:**

Use the default install location.
```
pip3 install kconfiglib
```
 
### Supported Machine Targets
The `MACHINE` (board) option can be used to set the target board for which linux is built, and if left blank it will default to `MACHINE=lc-mpfs`.           
The following table details the available targets:

| `MACHINE` | Board Name |
| --- | --- |
| `MACHINE=mpfs` | MPFS-DEV-KIT, HiFive Unleashed Expansion Board |
| `MACHINE=lc-mpfs` | LC-MPFS-DEV-KIT |
| `MACHINE=icicle-kit-es` | ICICLE-KIT-ES, Icicle Kit engineering samples (supports emmc boot) |
| `MACHINE=icicle-kit-es-sd` | ICICLE-KIT-ES, Icicle Kit engineering samples (supports SD card boot)|
| `MACHINE=qemuriscv64` | Simulation |

When building for different 'Machines' or want a 'clean' build, we recommend deleting the 'build' directory when switching.
This will delete all cache / configurations and downloads. 
```
cd yocto-dev
rm -rf build
```

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

 For more information on available images refer to [Yocto reference manual](https://www.yoctoproject.org/docs/3.1/ref-manual/ref-manual.html#ref-images)

## Bitbake commands

With the bitbake environment setup, execute the bitbake command in the following format to build the disk images.
```bash
MACHINE=<machine> bitbake <image>
```
Example building the icicle-kit-es machine and the mpfs-dev-cli Linux image
```bash
MACHINE=icicle-kit-es bitbake mpfs-dev-cli
```

To work with individual recipes:
```bash
MACHINE=<MACHINE> bitbake <recipe> -c <command>
```
BSP recipes avaialble: 
* hss (Microchip HSS)
* u540-c000-bootloader (Sifive FSBL)
* u-boot 
* mpfs-linux (kerenl our BSP)
  
Available commands: clean / configure / compile / install

## HSS Hardware Configuration from Libero Design

(Support for the Icicle-kit only)

The HSS recipe generates embedded software header files from information 
supplied by Libero from the Libero design. Libero supplies the information in 
the form of an xml file. This can be found in the Libero component subdirectory
e.g: /component/work/PFSOC_MSS_C0/PFSOC_MSS_C0_0

Update the following folder with the updated XML file (use the same name) :

```bash
meta-polarfire-soc-yocto-bsp/recipes-bsp/hss/files/${MACHINE}/ICICLE_MSS_0.xml
```

### Yocto Image and Binaries directory
```
build/tmp-glibc/deploy/images/{MACHINE}
```
For Example the following is the path for the Icicle-kit-es
```
build/tmp-glibc/deploy/images/icicle-kit-es
```

### Running wic.gz image on hardware

Compressed Disk images files use `<image>-<machine>.wic.gz` format, for example,

`mpfs-dev-cli-<machine>.wic.gz`. We are interested in `.wic.gz` disk images for writing to emmc/uSD/SD card.

> Be very careful while picking /dev/sdX device! Look at dmesg, lsblk, GNOME Disks, etc. before and after plugging in your usb flash device/uSD/SD to find a proper device. Double check it to avoid overwriting any of system disks/partitions!
> 
> Unmount any mounted partitions from uSD card before writing!
> 
> We advice to use 16GB or 32GB uSD cards. 8GB cards (shipped with HiFive Unleashed) can still be used with CLI images.

Example write the disk image to the SD card for the icicle kit:

```bash
cd yocto-dev/build
zcat tmp-glibc/deploy/images/icicle-kit-es/mpfs-dev-cli-icicle-kit-es.wic.gz | sudo dd of=/dev/sdX bs=512 iflag=fullblock oflag=direct conv=fsync status=progress
```
## Run in Simulation (QEMU)

```bash
./openembedded-core/scripts/runqemu nographic
```

## Additional Reading

[Yocto User Manual](https://www.yoctoproject.org/docs/3.1/mega-manual/mega-manual.html) 

[Yocto Development Task Manual](https://www.yoctoproject.org/docs/3.1/dev-manual/dev-manual.html) 
 
[Yocto Bitbake User Manual](https://www.yoctoproject.org/docs/3.1/bitbake-user-manual/bitbake-user-manual.html)
 
[PolarFire SoC Buildroot BSP](https://github.com/polarfire-soc/polarfire-soc-buildroot-sdk) 
 
[MPFS-DEV-KIT User Guide](doc/MPFS-DEV-KIT_user_guide.md) 

[LC-MPFS-DEV-KIT User Guide](doc/LC-MPFS-DEV-KIT_user_guide.md) 

[U-Boot Documentation](https://www.denx.de/wiki/U-Boot/Documentation) 

[Kernel Documentation for v5.4](https://www.kernel.org/doc/html/v5.4/) 

## Known issues

### Issue 001: Required binaries not available before creating the disk image
  We sometimes get dependencies not building correctly.
  During the process do_wic_install payload may not be present for hss or opensbi.

  For example after requesting a complete build:

  ```bash
  MACHINE=icicle-kit-es bitbake mpfs-dev-cli
  ```
  If payload is missing execute the following:
  ```bash
  MACHINE=icicle-kit-es bitbake hss -c clean
    MACHINE=icicle-kit-es bitbake hss -c install
  ```
  If u-boot or boot.src.uimg missing
  ```bash
  MACHINE=icickle-kit-es bitbake u-boot -c clean
   MACHINE=icickle-kit-es bitbake u-boot -c install
  ```
  And finally a complete build:
  ```bash
  MACHINE=icicle-kit-es bitbake mpfs-dev-cli
  ```

### Issue 002 fs.inotify.max_user_watches

Listen uses inotify by default on Linux to monitor directories for changes. It's not uncommon to encounter a system limit on the number of files you can monitor. For example, Ubuntu Lucid's (64bit) inotify limit is set to 8192.

You can get your current inotify file watch limit by executing:

```bash
$ cat /proc/sys/fs/inotify/max_user_watches
```

When this limit is not enough to monitor all files inside a directory, the limit must be increased for Listen to work properly.

Run the following command:

  ```bash
 echo fs.inotify.max_user_watches=524288 | sudo tee -a /etc/sysctl.conf && sudo sysctl -p
  ```

[Details on max_user_watches](https://github.com/guard/listen/wiki/Increasing-the-amount-of-inotify-watchers)


