
DESCRIPTION = "MPFS Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
##5.6.16
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION_EXTENSION = ""

PV = "${LINUX_VERSION}+git${SRCPV}"

KCONFIG_MODE="--alldefconfig"

KBUILD_DEFCONFIG_qemuriscv32 = "rv32_defconfig"
KBUILD_DEFCONFIG_qemuriscv64 = "defconfig"
KBUILD_DEFCONFIG_lc-mpfs = "defconfig"
KBUILD_DEFCONFIG_mpfs = "defconfig"
KBUILD_DEFCONFIG_icicle-kit-es = "defconfig"
KBUILD_DEFCONFIG_icicle-kit-es-sd = "defconfig"

COMPATIBLE_MACHINE = "(qemuriscv32|qemuriscv64|mpfs|lc-mpfs|icicle-kit-es|icicle-kit-es-sd)"

KERNEL_FEATURES_remove = "features/debug/printk.scc"
KERNEL_FEATURES_remove = "features/kernel-sample/kernel-sample.scc"
