<?php
    $outputLog = "/srv/www/tsn/countdown_output.log";
    $errorLog  = "/srv/www/tsn/countdown_error.log";

    echo shell_exec("/srv/www/tsn/restart_countdown.sh >$outputLog 2>$errorLog &");
?>

