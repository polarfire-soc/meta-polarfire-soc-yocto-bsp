<?php
    	if(isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on')   
		$url = "https://";   
    	else  
		$url = "http://";   
    	$url.= $_SERVER['HTTP_HOST'];   
	$url.= $_SERVER['REQUEST_URI'];    
	$ip_client = $_SERVER['REMOTE_ADDR'];
	$ip_server = $_SERVER['SERVER_ADDR'];

#$myfile = fopen("board", "r") or die("Unable to open file!");
$myfile = fopen("board", "r");
$val = fgets($myfile);
# MCHP_OVX_SOBEL_v1.0 - for Sobel
# MCHP_MJPEG_v1.0 - for mJPEG

	if (strcmp($val,"'H264'") == 1) {
                header('Refresh: 1; URL=http://'.$ip_server.'/h264/index.htm');
                exit();
        }


fclose($myfile);
?>
