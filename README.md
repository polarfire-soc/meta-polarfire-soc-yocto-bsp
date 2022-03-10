# Microchip PolarFire SoC Yocto BSP

Microchip Polarfire-SoC Yocto 'Board Support Package' (BSP) is based on OpenEmbedded (OE). The 'Polarfire SoC Yocto BSP' layer is build on top of the RISC-V Architectural layer (meta-riscv) to provide hardware specific features and additional disk images. 
Using Yocto 'Openembedded' you will build the following:

  - RISC-V Toolchain
  - Predefined Disk Images
  - Bootloader Binaries (FSBL / U-Boot)
  - Device Tree Binary (DTB)
  - Linux Kernel Images

The complete User Guides for each development platform, containing board and boot instructions, are available for the following supported platforms:

  - [ICICLE-KIT-ES](https://github.com/polarfire-soc/polarfire-soc-documentation/blob/master/boards/mpfs-icicle-kit-es/icicle-kit-sw-developer-guide/icicle-kit-sw-developer-guide.md) (Icicle Kit Engineering Sample) (Requires minimum Design Tag 2020.11 )

## Build Instructions
Before continuing, ensure that the prerequisite packages are present on your system. Please see the [Host PC setup for Yocto section](#Dependencies) for further details.

### Create the Workspace
This needs to be done every time you want a clean setup based on the latest BSP.
```bash
mkdir yocto-dev && cd yocto-dev
repo init -u https://github.com/polarfire-soc/meta-polarfire-soc-yocto-bsp.git -b master -m tools/manifests/riscv-yocto.xml
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

#### Building a Linux Image with a root file system (RootFS)

Using Yocto bitbake command and setting the MACHINE and image required.
```bash
MACHINE=icicle-kit-es bitbake mpfs-dev-cli
```
#### Building a RAM-based Root Filesystem (initramfs)

Using Yocto bitbake command and setting the initramfs configuration file (conf/initramfs.conf) and the mpfs-initramfs-image

```bash
MACHINE=icicle-kit-es -R conf/initramfs.conf bitbake mpfs-initramfs-image
```
### Copy the created Disk Image to flash device (USB mmc flash/SD/uSD)

> Be very careful while picking /dev/sdX device! Look at dmesg, lsblk, GNOME Disks, etc. before and after plugging in your usb flash device/uSD/SD to find a proper device. Double check it to avoid overwriting any of system disks/partitions!
>

We recommend using the `bmaptool` utility to program the storage device. `bmaptool` is a generic tool for creating the block map (bmap) for a file and copying files using this block map. Raw system image files can be flashed a lot faster with bmaptool than with traditional tools, like "dd" or "cp".

The created disk image is a 'wic' file, and is located in `yocto-dev/build/tmp-glibc/deploy/images/<MACHINE>/` directory,
 - e.g., for mpfs-dev-cli image and machine icicle-kit-es, it will be located in 
`yocto-dev/build/tmp-glibc/deploy/images/icicle-kit-es/mpfs-dev-cli-icicle-kit-es.wic`.

```bash

bmaptool copy yocto-dev/build/tmp-glibc/deploy/images/icicle-kit-es/mpfs-dev-cli-icicle-kit-es.wic /dev/sdX
```

The wic image uses a GUID Partition Table (GPT). GPT stores its primary GPT header at the start of the storage device, and a secondary GPT header at the end of the device.  The wic creation scripts do not correctly place this secondary GPT header at the current time.  To avoid later warnings about the GPT secondary header location, open the device with fdisk at this stage and rewrite the partition table:

```bash
fdisk /dev/sdX
```

This will output something like the following:

```bash
Welcome to fdisk (util-linux 2.34).
Changes will remain in memory only, until you decide to write them.
Be careful before using the write command.

GPT PMBR size mismatch (13569937 != 15273599) will be corrected by write.
The backup GPT table is not on the end of the device. This problem will be corrected by write.

Command (m for help):
```

Press `w` to write the partition table and exit `fdisk`:

```bash
Command (m for help): w

The partition table has been altered.
Calling ioctl() to re-read partition table.
Syncing disks.
```

### Supported Machine Targets
The `MACHINE` (board) option can be used to set the target board for which linux is built, and if left blank it will default to `MACHINE=icicle-kit-es`.
The following table details the available targets:

| `MACHINE`                  | Board Name                                                            |
| -------------------------- | --------------------------------------------------------------------- |
| `MACHINE=icicle-kit-es`    | ICICLE-KIT-ES, Icicle Kit engineering samples                         |
| `MACHINE=icicle-kit-es-amp`| ICICLE-KIT-ES, Icicle Kit engineering samples in AMP mode             |
| `MACHINE=m100pfsevp`       | Aries Embedded M100PFSEVP PolarFire SoC-FPGA Evaluation Platform      |

The `icicle-kit-es-amp` machine can be used to build the Icicle Kit engineering sample with AMP support. Please see the [Asymmetric Multiprocessing (AMP)](https://github.com/polarfire-soc/polarfire-soc-documentation/blob/master/asymmetric-multiprocessing/amp.md) documentation for further details.

When building for different 'Machines' or want a 'clean' build, we recommend deleting the 'build' directory when switching.
This will delete all cache / configurations and downloads.
```
cd yocto-dev
rm -rf build
```

### Linux Images
The table below summarizes the most common Linux images that can be built using this BSP.

| bitbake `<image>` argument | Description                                        |
| -------------------------- | -------------------------------------------------- |
| `core-image-minimal-dev`   | A small console image with some development tools. |
| `mpfs-dev-cli`             | A console image with development tools.            |
| `mpfs-initramfs-image`     | A small RAM-based Root Filesystem (initramfs) image |

For more information on available images refer to [Yocto reference manual](https://docs.yoctoproject.org/ref-manual/images.html)

#### Target machine Linux login
Login with `root` account, there is no password set.


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
View/Edit the Kernel menuconfig:
```bash
MACHINE=<MACHINE> bitbake mpfs-linux -c menuconfig
```
Run the diffconfig command to prepare a configuration fragment.
The resulting file fragment.cfg should be copied to meta-polarfire-soc-yocto-bsp/recipes-kernel/linux/files directory:
Afterwards the mpfs-linux.bb src_uri should be updated to include the <fragment>.cfg,
```bash
MACHINE=<MACHINE> bitbake mpfs-linux -c diffconfig
```

**Available BSP recipes:**

  - hss (Microchip Hart Software Services) payload generator
  - u540-c000-bootloader (Sifive FSBL)
  - u-boot
  - mpfs-linux (BSP kernel)

**Available commands:**

  - clean
  - configure
  - compile
  - install

### Yocto Image and Binaries directory
```
build/tmp-glibc/deploy/images/{MACHINE}
```
For Example the following is the path for the Icicle-kit-es
```
build/tmp-glibc/deploy/images/icicle-kit-es
```

## Run in Simulation (QEMU)

```bash
MACHINE=qemuriscv64 runqemu nographic
```

<a name="Dependencies"></a>
## Host PC setup for Yocto

### Yocto Dependencies
This document assumes you are running on a modern Linux system. The process documented here was tested using Ubuntu 18.04 LTS.
It should also work with other Linux distributions if the equivalent prerequisite packages are installed.

The BSP uses the Yocto RISCV Architecture Layer, and the Yocto release Honister (Revision 3.4)	(Released April 2020).

**Make sure to install the [repo command](https://source.android.com/setup/develop#installing-repo) by Google first.**

Detailed instructions for various distributions can be found in the "[Required Packages for the Build Host](https://docs.yoctoproject.org/3.4/ref-manual/system-requirements.html#required-packages-for-the-build-host)" section in the Yocto Project Reference Manual.

```bash
**Note: Some extra packages are requried to support this Yocto 3.4 Release (codename “honister”) from the prior release.**
```

<a name="OtherDeps"></a>
### Other Dependencies

For Ubuntu 18.04 (or newer) install python3-distutils:
```
sudo apt install python3-distutils
```
HSS Payload Generator uses libelf and libyaml, as well as zlib (a dependency of libelf).

```
sudo apt-get install libyaml-dev
sudo apt-get install libelf-dev
```
You can install the bmap-tools package using the following command:

```
sudo apt-get install bmap-tools
```

## Additional Reading

[Yocto Overview Manual](https://docs.yoctoproject.org/overview-manual/index.html)

[Yocto Development Task Manual](https://docs.yoctoproject.org/dev-manual/index.html)

[Yocto Bitbake User Manual](https://docs.yoctoproject.org/bitbake/index.html)

[Yocto Application Development and Extensible Software Development Kit (sSDK)](https://docs.yoctoproject.org/sdk-manual/index.html)

[PolarFire SoC Buildroot BSP](https://github.com/polarfire-soc/polarfire-soc-buildroot-sdk)

[U-Boot Documentation](https://www.denx.de/wiki/U-Boot/Documentation)

[Kernel Documentation for v5.12](https://www.kernel.org/doc/html/v5.12/)

[Yocto Flashing images using bmaptool](https://www.yoctoproject.org/docs/current/mega-manual/mega-manual.html#flashing-images-using-bmaptool)

## Known issues

### Issue 001: Required binaries not available before creating the disk image
We sometimes get dependencies not building correctly.
During the process do_wic_install payload may not be present for hss

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
MACHINE=icicle-kit-es bitbake u-boot -c clean
MACHINE=icicle-kit-es bitbake u-boot -c install
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

See [Other Dependencies](#OtherDeps) for installation instructions.
