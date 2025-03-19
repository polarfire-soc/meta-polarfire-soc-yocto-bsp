#!/bin/sh

#STATUS_FILE="/sys/kernel/config/device-tree/overlays/mpfs_mcp23s08_ioexpander.dtbo/status"

# Check if the SPI device overlay does not exist or is not applied
#if ! grep -q "applied" "$STATUS_FILE"; then
#    ./load_spi_device.sh
#fi

# Default number of steps
steps=50

# Default direction - Clockwise
direction=0

# Override steps if an argument is provided
if [ -n "$1" ]; then
    steps=$1
fi

# Override direction if the second argument is provided
if [ -n "$2" ]; then
    direction=$2
fi

# Activate GPIO 21 on GPIO chip 0
gpioset 0 20=1

# Read state of GPIOs 0 to 7 on GPIO chip 1 and discard output
gpioget 1 0 1 2 3 4 5 6 7 > /dev/null 2>&1

# Reset all GPIOs on GPIO chip 1 to 0
gpioset 1 0=0 1=0 2=0 3=0 4=0 5=0 6=0 7=0

# Loop for the specified number of steps
for ((i=1; i<=steps; i++)); do
    if [[ $direction -eq 0 ]]; then
        # Sequence of GPIO settings for clockwise direction
        gpioset 1 0=0 1=0 2=0 3=0 4=0 5=0 6=1 7=1
        gpioset 1 0=0 1=0 2=0 3=0 4=0 5=0 6=0 7=1
        gpioset 1 0=0 1=0 2=0 3=0 4=0 5=0 6=0 7=0
        gpioset 1 0=0 1=0 2=0 3=0 4=0 5=0 6=1 7=0
    else
        # Sequence of GPIO settings for anti-clockwise direction
        gpioset 1 0=0 1=0 2=0 3=0 4=0 5=0 6=1 7=1
        gpioset 1 0=0 1=0 2=0 3=0 4=0 5=0 6=1 7=0
        gpioset 1 0=0 1=0 2=0 3=0 4=0 5=0 6=0 7=0
        gpioset 1 0=0 1=0 2=0 3=0 4=0 5=0 6=0 7=1
    fi
done

# Deactivate GPIO 21 on GPIO chip 0
gpioset 0 20=0
