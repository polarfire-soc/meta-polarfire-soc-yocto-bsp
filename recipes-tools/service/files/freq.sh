#! /bin/sh

if [ $# -eq 1 ]; then
	rate="$1"
	rate=$((rate*1000000))

	if [ $rate -gt 1700000000 ]; then
		echo $rate is too fast
		exit 1
	fi
	if [ $rate -lt 50000000 ]; then
		echo $rate is too slow
		exit 1
	fi
fi

echo -n $rate > /sys/devices/platform/soc/10000000.prci/rate
echo -n "Core rate: "
rate=$(cat /sys/devices/platform/soc/10000000.prci/rate)
rate=$(((rate+500000)/1000000))
echo "${rate}MHz"
