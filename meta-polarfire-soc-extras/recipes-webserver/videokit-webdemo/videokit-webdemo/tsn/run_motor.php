<?php
    // Log file paths
    $outputLog = "/srv/www/tsn/run_motor_output.log";
    $errorLog = "/srv/www/tsn/run_motor_error.log";

    // Command to check if the script is already running
    $scriptName = "/srv/www/tsn/run_motor.sh";
    $processCheck = shell_exec("pgrep -f '$scriptName'");

    if ($processCheck) {
        // Log and respond if the script is already running
        file_put_contents($outputLog, "[$scriptName] Motor script is already running.\n", FILE_APPEND);
    } else {
        // Log the start of the script and execute it in the background
        file_put_contents($outputLog, "[$scriptName] Starting motor script...\n", FILE_APPEND);
        shell_exec("sudo $scriptName >$outputLog 2>$errorLog &");
        file_put_contents($outputLog, "[$scriptName] Motor spinning script has started.\n", FILE_APPEND);
    }
?>

