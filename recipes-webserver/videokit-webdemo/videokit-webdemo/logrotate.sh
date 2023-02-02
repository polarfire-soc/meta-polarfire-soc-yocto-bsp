#!/bin/bash
log1="/srv/www/mjpeg/error_log"
log2="/srv/www/mjpeg/messages"
log3="/srv/www/mjpeg/error_log.1"
log4="/srv/www/mjpeg/messages.1"
[ -e $log3 ] && rm $log3
[ -e $log4 ] && rm $log4
[ -e $log1 ] && mv $log1 $log3 && touch $log1 && chown daemon:daemon $log1
[ -e $log2 ] && mv $log2 $log4 && touch $log2 && chown daemon:daemon $log2
