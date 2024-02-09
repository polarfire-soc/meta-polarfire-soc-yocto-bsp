#!/bin/sh

/srv/www/h264/stop.sh > /srv/www/h264/kill_messages

media-ctl -v -V '"imx334 0-001a":0 [fmt:SRGGB10_1X10/1920x1080 field:none colorspace:srgb xfer:none]' -d /dev/media0
media-ctl -v -V '"60001000.csi2rx":1 [fmt:SRGGB10_1X10/1920x1080 field:none colorspace:srgb xfer:none]' -d /dev/media0
media-ctl -v -V '"60005000.rgb-scaler":0 [fmt:RBG888_1X24/1920x1080  crop: (0,0)/1920x1072 field:none colorspace:srgb]' -d /dev/media0
media-ctl -v -V '"60002000.yuv2h264":0 [fmt:UYVY8_2X8/1920x1072 field:none colorspace:srgb]' -d /dev/media0
media-ctl -v -V '"60007000.generic-video-pipeline-connector":0 [fmt:RBG888_1X24/1920x1072 field:none colorspace:srgb]' -d /dev/media0
media-ctl -v -V '"60006000.osd":0 [fmt:RBG888_1X24/1920x1072 field:none colorspace:srgb]' -d /dev/media0

SERVER="192.168.1.1"
test $1 && SERVER=$1
ffmpeg -s 1920x1072 -i /dev/video0 -c:v copy -f rtp -sdp_file video.sdp "rtp://$SERVER:10000" </dev/null  >/srv/www/h264/messages 2>/srv/www/h264/error_log &
v4l2-ctl -d /dev/video0 --set-ctrl=gain_automatic=1
v4l2-ctl -d /dev/v4l-subdev0 --set-ctrl=vertical_blanking=1170
v4l2-ctl -d /dev/video0 --set-ctrl=vertical_blanking=1170
v4l2-ctl -d /dev/video0 --set-ctrl=analogue_gain=80
v4l2-ctl -d /dev/video0 --set-ctrl=osd_color=0x000
v4l2-ctl -d /dev/video0 --set-ctrl=osd_enable=1
/opt/microchip/multimedia/v4l2/auto_enhance_osd/auto-enhance-osd /dev/video0 0 1 > /dev/null  &

