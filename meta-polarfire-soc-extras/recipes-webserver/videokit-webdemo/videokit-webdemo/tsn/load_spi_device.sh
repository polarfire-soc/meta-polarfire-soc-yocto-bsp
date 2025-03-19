#!/bin/bash

# Path to the overlay script, overlay name, and status file
OVERLAY_SCRIPT="/opt/microchip/dt-overlays/overlay.sh"
OVERLAY_NAME="mpfs_mcp23s08_ioexpander.dtbo"
OVERLAY_PATH="/sys/kernel/config/device-tree/overlays/$OVERLAY_NAME"
STATUS_FILE="$OVERLAY_PATH/status"
BOOT_DIR="/boot"

# Save the current directory
CURRENT_DIR=$(pwd)

# Function to return to the original directory
return_to_original_dir() {
    cd "$CURRENT_DIR" || { echo "Failed to return to the original directory. Exiting."; exit 1; }
}

# Change to /boot directory
cd "$BOOT_DIR" || { echo "Failed to change directory to $BOOT_DIR. Exiting."; exit 1; }

# Check if the SPI device overlay exists and is applied
if [ -d "$OVERLAY_PATH" ] && [ -f "$STATUS_FILE" ] && grep -q "applied" "$STATUS_FILE"; then
    echo "SPI device '$OVERLAY_NAME' is already loaded and applied. Skipping overlay application."
    return_to_original_dir
    exit 0
fi

# Attempt to load the SPI device
echo "Attempting to load SPI device '$OVERLAY_NAME'..."
if $OVERLAY_SCRIPT $OVERLAY_NAME; then
    echo "SPI device '$OVERLAY_NAME' loaded successfully."
else
    echo "Failed to load SPI device '$OVERLAY_NAME'."
    return_to_original_dir
    exit 1
fi

# Return to the original directory
return_to_original_dir

