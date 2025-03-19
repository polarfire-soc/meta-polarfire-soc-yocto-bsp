<?php
    // Log file paths
    $outputLog = "/srv/www/tsn/tsn_status_output.log";
    $errorLog = "/srv/www/tsn/tsn_status_error.log";

    // Execute the flood_traffic script in the background
    echo shell_exec("sudo /srv/www/tsn/apply_tsn.sh enable_qb.conf 3 >$outputLog 2>$errorLog &");

    // Confirmation message
    echo "Flood traffic script is running in the background. Check logs for details.";
?>

