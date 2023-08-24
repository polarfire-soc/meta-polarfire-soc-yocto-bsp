#!/bin/sh

/srv/www/h264/stop.sh > /srv/www/h264/kill_messages

SERVER="192.168.1.1"
test $1 && SERVER=$1
ffmpeg -s 1920x1080 -i /dev/video0 -c:v copy -f rtp -sdp_file video.sdp "rtp://$SERVER:10000" </dev/null  >/srv/www/h264/messages 2>/srv/www/h264/error_log &
v4l2-ctl -d /dev/video0 --set-ctrl=gain_automatic=1
v4l2-ctl -d /dev/v4l-subdev0 --set-ctrl=vertical_blanking=1170

