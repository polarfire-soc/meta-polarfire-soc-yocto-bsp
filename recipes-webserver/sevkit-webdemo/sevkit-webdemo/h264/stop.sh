v4l2-ctl -d /dev/video0 --set-ctrl=gain_automatic=0
echo -e "$(date)"
echo -e "Start Time: $(date +"%T.%6N")"
echo "Stopping H.264 wrapper..."
kill $(pidof ffmpeg)
kill $(pidof ffmpeg)
echo -e "End Time: $(date +"%T.%6N")"

