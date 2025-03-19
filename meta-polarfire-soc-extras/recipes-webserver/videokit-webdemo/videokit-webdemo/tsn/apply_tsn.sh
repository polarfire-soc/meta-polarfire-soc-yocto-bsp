#!/bin/sh

/opt/microchip/tsn/microchip-tsn-replace-tsnbasetime --ptpfile=/dev/ptp0 --addtimesec=$2 --addtimensec=0 --infile=$1 --outfile=$1 --secroundoff
/opt/microchip/tsn/microchip-tsn-cli --device=40000000 --conf-file=$1
