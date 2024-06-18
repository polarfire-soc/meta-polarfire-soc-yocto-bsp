#!/bin/bash

# Function to display the usage of the script
usage() {
    echo "Usage: $0 [--reboot]"
    exit 1
}

# Check if the script is run with root privileges
if [[ $EUID -ne 0 ]]; then
    echo "This script must be run as root."
    exit 1
fi

# Check for the --reboot option
REBOOT=false
if [[ "$1" == "--reboot" ]]; then
    REBOOT=true
elif [[ -n "$1" ]]; then
    usage
fi

echo "================================================================================"
echo "|                            FPGA Gateware Update                              |"
echo "|                                                                              |"
echo "|   Please ensure that the mpfs_bitstream.spi file containing the gateware     |"
echo "|   update has been copied to the directory /lib/firmware.                     |"
echo "|                                                                              |"
echo "|                 !!! This will take a couple of minutes. !!!                  |"
echo "|                                                                              |"
echo "|               Give the system a few minutes to reboot itself                 |"
echo "|                        after Linux has shut down.                            |"
echo "|                                                                              |"
echo "================================================================================"

if [[ ! -f /usr/sbin/mtd_debug ]]; then
    echo "Install mtd-utils package"
    exit 2
fi

if [[ ! -f /lib/firmware/mpfs_bitstream.spi ]]; then
    exit 2
fi

#read -rsp $'Press any key to continue...\n' -n1 key

if [[ ! -f /sys/kernel/debug/fpga/microchip_exec_update ]]; then
    /usr/bin/mount -t debugfs none /sys/kernel/debug
fi

# Erase existing device tree overlay in case the rest of the process fails:
/usr/sbin/mtd_debug erase /dev/mtd0 0x0 0x10000

# Write device tree overlay
dtbo_ls=$(ls -l /lib/firmware/mpfs_dtbo.spi)
dtbo_size=$(echo $dtbo_ls | cut -d " " -f 5)

echo "Writing mpfs_dtbo.spi to /dev/mtd0"
/usr/sbin/mtd_debug write /dev/mtd0 0x400 $dtbo_size /lib/firmware/mpfs_dtbo.spi > /dev/zero

# Fake the presence of a golden image for now.
/usr/sbin/mtd_debug write /dev/mtd0 0 4 /dev/zero > /dev/zero

# Initiate FPGA update.
echo "Triggering FPGA Gateware Update (/sys/kernel/debug/fpga/microchip_exec_update)"
echo 1 > /sys/kernel/debug/fpga/microchip_exec_update

# Reboot Linux for the gateware update to take effect, if --reboot option is provided.
# FPGA reprogramming takes place between Linux shut-down and HSS restarting the board.
if $REBOOT; then
    echo "Rebooting the system for the gateware update to take effect."
    /usr/sbin/reboot
else
    echo "Please reboot the system manually for the gateware update to take effect."
fi
