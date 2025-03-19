<?php
    // Log file paths
    $outputLog = "/srv/www/tsn/flood_traffic_output.log";
    $errorLog = "/srv/www/tsn/flood_traffic_error.log";

    // Default time value
    $time = 15;

    // Check if the 'time' argument is provided and valid
    if (isset($_GET['time']) && is_numeric($_GET['time'])) {
        $time = escapeshellarg($_GET['time']); // Escape the 'time' argument for safety
    }

    // Execute the flood_traffic script in the background
    echo shell_exec("sudo /srv/www/tsn/flood_traffic.sh $time >$outputLog 2>$errorLog &");

    // Confirmation message
    echo "Flood traffic script is running in the background. Check logs for details.";
?>

