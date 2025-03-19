#!/bin/bash

# Global flag to ensure cleanup runs only once
CLEANED_UP=0

# Function to ensure cleanup
cleanup() {
    # Check if cleanup has already been done
    if [ $CLEANED_UP -eq 1 ]; then
        return
    fi
    CLEANED_UP=1

    echo "Executing cleanup..."
    # Stop flood traffic
    devmem2 0x40003080 w 0
    # Kill any lingering flood_traffic or sleep processes
    pkill -f "flood_traffic" 2>/dev/null || true
    pkill -f "sleep" 2>/dev/null || true

    # Reset traps to avoid recursion
    trap - EXIT SIGINT SIGTERM
}

# Trap signals to ensure cleanup runs even if the script is interrupted
trap cleanup SIGINT SIGTERM EXIT

# Validate and read the time argument, default to 5 seconds if invalid or missing
if [ -z "$1" ] || ! [[ "$1" =~ ^[0-9]+$ ]]; then
    echo "No valid time argument provided. Defaulting to 5 seconds."
    TIME=15
else
    TIME=$1
fi

# Initial setup
echo "Setting up flood traffic registers..."
# Number of packets to be transmitted
devmem2 0x4000304c w 100 || { echo "Failed to write to register 0x4000304c"; exit 1; }
# Number of bytes for packet(Packet length) 
devmem2 0x40003010 w 1250 || { echo "Failed to write to register 0x40003010"; exit 1; }
# Configuration register for MAC Source Address [31:0]
devmem2 0x40003014 w 0x11223344 || { echo "Failed to write to register 0x40003014"; exit 1; }
# Configuration register for MAC Source Address [47:32]
devmem2 0x40003018 w 0x55665566 || { echo "Failed to write to register 0x40003018"; exit 1; }
# Configuration register for MAC Destination Address [31:0]
devmem2 0x4000301C w 0xaabbccdd || { echo "Failed to write to register 0x4000301C"; exit 1; }
# Configuration register for MAC Destination Address [47:32]
devmem2 0x40003020 w 0xeeffeeff || { echo "Failed to write to register 0x40003020"; exit 1; }
# Number of Idles to be sent between packets
devmem2 0x40003024 w 100 || { echo "Failed to write to register 0x40003024"; exit 1; }

echo "Starting flood traffic..."
# Start flood traffic
devmem2 0x40003080 w 1 || { echo "Failed to start flood traffic"; exit 1; }

# Run flood traffic for the specified time
echo "Flood traffic running for $TIME seconds..."
sleep "$TIME"

# Cleanup after timeout
cleanup
