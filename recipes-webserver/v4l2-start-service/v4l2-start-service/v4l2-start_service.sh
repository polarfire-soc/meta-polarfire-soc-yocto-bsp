#!/bin/bash
/usr/bin/v4l2-ctl -d /dev/video0  --all | grep "Pixel Format" | awk '{print $4}' > /srv/www/board
/usr/bin/v4l2-ctl -d /dev/video0 --set-ctrl=brightness=137 --set-ctrl=contrast=154 --set-ctrl=gain_red=122 --set-ctrl=gain_green=102 --set-ctrl=gain_blue=138
gpioset gpiochip0 18=1
sleep 1
