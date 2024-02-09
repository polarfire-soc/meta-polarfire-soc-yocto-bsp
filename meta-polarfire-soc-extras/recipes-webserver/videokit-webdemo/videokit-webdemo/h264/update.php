<html>
<head>
	<title>Microchip H.264 GUI Demo</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="pfsoc.css">
</style>
</head>
<body>		
<!--    <img src="microchip_logo.png" alt="Microchip" width="150" height="100"></br> -->
        <img src="mchp_logo2.png" alt="Microchip" width="187" height="100"></br>
	<h1 align=center style="background-color:powderblue;color:blue">H.264 GUI</h1>

<?php
$brightness = $_REQUEST['brightness'];
$contrast = $_REQUEST['contrast'];
$qf = $_REQUEST['qf'];
$cbred = $_REQUEST['cbred'];
$cbgreen = $_REQUEST['cbgreen'];
$cbblue = $_REQUEST['cbblue'];
$resolution = $_REQUEST['resolution'];
$color = $_REQUEST['color'];
$p_osd = $_REQUEST['p_osd'];
$pattern = $_REQUEST['pattern'];
?>

<table align=center>
<tr></tr>
<tr align=center> <td>
<h2> Please wait while camera configurations are being updated... </h2>
</td></tr>
<tr> 
	<td> <b>Brightness : <?php echo $brightness ?> </td>
	<td> <b>Color Red  : <?php echo $cbred ?> </td>
</tr>
<tr> 
	<td> <b>Contrast   : <?php echo $contrast ?> </td>
	<td> <b>Color Green: <?php echo $cbgreen ?> </td>
</tr>
<tr> 
	<td> <b>Quality Factor: <?php echo $qf ?> </td>
	<td> <b>Color Blue: <?php echo $cbblue ?> </td>
</tr>
</table>

<?php
        if( $resolution == 1) {
		$hreso = "432";
		$vreso = "240";
		$hvreso = "432x240";
	} elseif( $resolution == 2) {
		$hreso = "640";
		$vreso = "480";
		$hvreso = "640x480";
	} elseif( $resolution == 3) {
		$hreso = "960";
		$vreso = "544";
		$hvreso = "960x544";
	} elseif( $resolution == 4) {
		$hreso = "1280";
		$vreso = "720";
		$hvreso = "1280x720";
	} elseif( $resolution == 5) {
		$hreso = "1920";
		$vreso = "1072";
		$hvreso = "1920x1072";
	} else {
		$hreso = "1280";
		$vreso = "720";
		$hvreso = "1280x720";
	}

	if (isset($_REQUEST['e_osd'])) {
		$e_osd = true;
    $e_osd_state = "1";
		$osd = "v4l2-ctl -d /dev/video0 --set-ctrl=osd_enable=1";
	} else {
		$e_osd = false;
    $e_osd_state = "0";
		$osd = "v4l2-ctl -d /dev/video0 --set-ctrl=osd_enable=0";
	}

	if (isset($_REQUEST['e_enhance'])) {
		$e_enhance = true;
    $e_enhance_state = "1";
		$enhance = "v4l2-ctl -d /dev/video0 --set-ctrl=gain_automatic=1";
	} else {
	  $e_enhance = false;
    $e_enhance_state = "0";
		$enhance = "v4l2-ctl -d /dev/video0 --set-ctrl=gain_automatic=0";
	}

        if( $pattern == 1) {
		$v_pattern = "v4l2-ctl -d /dev/v4l-subdev0 --set-ctrl=test_pattern=2";
	} elseif( $pattern == 2) {
		$v_pattern = "v4l2-ctl -d /dev/v4l-subdev0 --set-ctrl=test_pattern=1";
	} elseif( $pattern == 3) {
		$v_pattern = "v4l2-ctl -d /dev/v4l-subdev0 --set-ctrl=test_pattern=0";
	} else {
		$v_pattern = "v4l2-ctl -d /dev/v4l-subdev0 --set-ctrl=test_pattern=0";
	}

        if( $color == 1) {
		$v_color = "v4l2-ctl -d /dev/video0 --set-ctrl=osd_color=0x0071c5";
	} elseif( $color == 2) {
		$v_color = "v4l2-ctl -d /dev/video0 --set-ctrl=osd_color=0x40E0D0";
	} elseif( $color == 3) {
		$v_color = "v4l2-ctl -d /dev/video0 --set-ctrl=osd_color=0x008000";
	} elseif( $color == 4) {
		$v_color = "v4l2-ctl -d /dev/video0 --set-ctrl=osd_color=0xFFD700";
	} elseif( $color == 5) {
		$v_color = "v4l2-ctl -d /dev/video0 --set-ctrl=osd_color=0xFF8C00";
	} elseif( $color == 6) {
		$v_color = "v4l2-ctl -d /dev/video0 --set-ctrl=osd_color=0xFF0000";
	} else {
		$v_color = "v4l2-ctl -d /dev/video0 --set-ctrl=osd_color=0x000";
	}

	$hreso_num = intval($hreso)/2;
	$vreso_num = intval($vreso)/2;

	if( $p_osd == 1) {
		$v_osd_x = "v4l2-ctl -d /dev/video0 --set-ctrl=osdx_position=0";
		$v_osd_y = "v4l2-ctl -d /dev/video0 --set-ctrl=osdy_position=0";
	} elseif( $p_osd == 2) {
		$v_osd_x = "v4l2-ctl -d /dev/video0 --set-ctrl=osdx_position=".$hreso;
		$v_osd_y = "v4l2-ctl -d /dev/video0 --set-ctrl=osdy_position=0";
	} elseif( $p_osd == 3) {
		$v_osd_x = "v4l2-ctl -d /dev/video0 --set-ctrl=osdx_position=".$hreso_num;
		$v_osd_y = "v4l2-ctl -d /dev/video0 --set-ctrl=osdy_position=".$vreso_num;
	} elseif( $p_osd == 4) {
		$v_osd_x = "v4l2-ctl -d /dev/video0 --set-ctrl=osdx_position=0";
		$v_osd_y = "v4l2-ctl -d /dev/video0 --set-ctrl=osdy_position=".$vreso;
	} elseif( $p_osd == 5) {
		$v_osd_x = "v4l2-ctl -d /dev/video0 --set-ctrl=osdx_position=".$hreso;
		$v_osd_y = "v4l2-ctl -d /dev/video0 --set-ctrl=osdy_position=".$vreso;
	} else {
		$v_osd_x = "v4l2-ctl -d /dev/video0 --set-ctrl=osdx_position=0";
		$v_osd_y = "v4l2-ctl -d /dev/video0 --set-ctrl=osdy_position=0";
	}

$auto_osd = "/opt/microchip/multimedia/v4l2/auto_enhance_osd/auto-enhance-osd /dev/video0 ".$e_enhance_state." ".$e_osd_state." & \n";

$reso = "v4l2-ctl --device /dev/video0 --set-fmt-video=width=".$hreso.",height=".$vreso."\nmedia-ctl -v -V '\"60005000.rgb-scaler\":0 [fmt:RBG888_1X24/1920x1080  crop: (0,0)/".$hvreso." field:none colorspace:srgb]' -d /dev/media0 \nmedia-ctl -v -V '\"60002000.yuv2h264\":0 [fmt:UYVY8_2X8/".$hvreso." field:none colorspace:srgb]' -d /dev/media0 \nmedia-ctl -v -V '\"60007000.generic-video-pipeline-connector\":0 [fmt:RBG888_1X24/".$hvreso." field:none colorspace:srgb]' -d /dev/media0 \nmedia-ctl -v -V '\"60006000.osd\":0 [fmt:RBG888_1X24/".$hvreso." field:none colorspace:srgb]' -d /dev/media0 \n";

$myfile = fopen("update.sh", "w") or die("Unable to open file!");
$txt = "kill $(pidof auto-enhance-osd) \n kill $(pidof auto-enhance-osd) \n";
fwrite($myfile, $txt);
$txt = "/usr/bin/v4l2-ctl -d /dev/video0 --set-ctrl=quality_factor=".$qf." --set-ctrl=brightness=".$brightness." --set-ctrl=contrast=".$contrast." --set-ctrl=gain_red=".$cbred." --set-ctrl=gain_green=".$cbgreen." --set-ctrl=gain_blue=".$cbblue."\n";
fwrite($myfile, $txt);
$txt = "$reso"."\n";
fwrite($myfile, $txt);
$txt = "$osd"."\n";
fwrite($myfile, $txt);
$txt = "$v_pattern"."\n";
fwrite($myfile, $txt);
$txt = "$v_color"."\n";
fwrite($myfile, $txt);
$txt = "$v_osd_x"."\n";
fwrite($myfile, $txt);
$txt = "$v_osd_y"."\n";
fwrite($myfile, $txt);
$txt = "$enhance"."\n";
fwrite($myfile, $txt);
$txt = "$auto_osd";
fwrite($myfile, $txt);
fclose($myfile);
?>

<?php
    	if(isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on')   
		$url = "https://";   
    	else  
		$url = "http://";   
    	$url.= $_SERVER['HTTP_HOST'];   
	$url.= $_SERVER['REQUEST_URI'];    
	$ip_server = $_SERVER['SERVER_ADDR'];
	echo shell_exec("sudo /srv/www/h264/update.sh >/srv/www/h264/messages 2>/srv/www/h264/error_log &");

header('Refresh: 3; URL=http://'.$ip_server.'/h264/index.htm?v_resolution='.$resolution.'&v_bright='.$brightness.'&v_contrast='.$contrast.'&v_qf='.$qf.'&v_red='.$cbred.'&v_green='.$cbgreen.'&v_osd='.$e_osd.'&v_enhance='.$e_enhance.'&v_blue='.$cbblue.'&stream=started');
exit();
?>

</body>
</html>
