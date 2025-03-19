<?php
// countdown.php
set_time_limit(0); // Ensure the script doesn't timeout
$file = 'count.txt'; // Change this to an appropriate path

// Initial countdown time (adjust as needed)
$hours = 10;
$minutes = 0;
$seconds = 0;

// Infinite loop to keep updating the countdown
while (true) {
    // Format the time as HH:MM:SS:MS (MS always set to 000)
    $formattedTime = sprintf('%02d:%02d:%02d:000', $hours, $minutes, $seconds);

    // Write the time to the file
    file_put_contents($file, $formattedTime);

    // Decrement the countdown
    $seconds--;
    if ($seconds < 0) {
        $seconds = 59;
        $minutes--;
    }
    if ($minutes < 0) {
        $minutes = 59;
        $hours--;
    }
    if ($hours < 0) {
        // Stop the countdown if time reaches 00:00:00:000
        file_put_contents($file, "00:00:00:000");
        break;
    }

    // Delay to simulate real-time countdown (1-second interval)
    sleep(1);
}
?>

