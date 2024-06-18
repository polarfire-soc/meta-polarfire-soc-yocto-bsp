#!/bin/bash

# Check if the script is run with root privileges
if [[ $EUID -ne 0 ]]; then
    echo "This script must be run as root."
    exit 1
fi

# Check if an argument is passed to the script
if [[ -z "$1" ]]; then
    echo "Please provide a directory path."
    exit 1
fi

# Check if the directory exists
if [[ -d "$1" ]]; then
    echo "Changing gateware."

    # Check if the mpfs_bitstream.spi file exists
    if [[ -e "$1/LinuxProgramming/mpfs_bitstream.spi" ]]; then
        
        # Check if the mpfs_dtbo.spi file exists
        if [[ -e "$1/LinuxProgramming/mpfs_dtbo.spi" ]]; then
            cp -v "$1/LinuxProgramming/mpfs_dtbo.spi" /lib/firmware/mpfs_dtbo.spi
            cp -v "$1/LinuxProgramming/mpfs_bitstream.spi" /lib/firmware/mpfs_bitstream.spi
            sync
            . /usr/share/microchip/gateware/update-gateware.sh
        else
            echo "No device tree overlay file found."
        fi
    else
        echo "No gateware file found."
    fi
else
    echo "No directory found for the requested gateware."
fi
