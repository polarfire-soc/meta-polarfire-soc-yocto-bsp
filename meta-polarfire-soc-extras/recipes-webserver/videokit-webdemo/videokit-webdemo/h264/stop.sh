#!/bin/sh

v4l2-ctl -d /dev/video0 --set-ctrl=gain_automatic=0
echo -e "$(date)"
echo -e "Start Time: $(date +"%T.%6N")"
echo "Stopping H.264 wrapper..."
kill $(pidof ffmpeg)
kill $(pidof ffmpeg)
kill $(pidof auto-enhance-osd)
kill $(pidof auto-enhance-osd)
echo -e "End Time: $(date +"%T.%6N")"

