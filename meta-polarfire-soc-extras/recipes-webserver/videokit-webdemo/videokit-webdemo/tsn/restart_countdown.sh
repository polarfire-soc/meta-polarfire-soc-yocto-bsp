#!/bin/sh

# Log file paths
outputLog="/srv/www/tsn/countdown_output.log"
errorLog="/srv/www/tsn/countdown_error.log"

# Kill any existing countdown process
ps -aef | grep "/usr/bin/php /srv/www/tsn/countdown.php" | grep -v grep | awk '{print $2}' | xargs kill -9

# Start a new countdown process
/usr/bin/php /srv/www/tsn/countdown.php >"$outputLog" 2>"$errorLog" &

# Print confirmation
echo "Countdown process restarted successfully."

