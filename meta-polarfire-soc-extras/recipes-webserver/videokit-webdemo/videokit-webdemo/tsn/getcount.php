<?php
// getcount.php
$file = 'count.txt'; // Ensure this matches the path in countdown.php

if (file_exists($file)) {
    echo file_get_contents($file);
} else {
    echo "00:00:00:000"; // Default value if file doesn't exist
}

