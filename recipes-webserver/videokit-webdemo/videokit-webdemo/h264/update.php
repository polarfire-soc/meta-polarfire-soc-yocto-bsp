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
		$reso = "v4l2-ctl --device /dev/video0 --set-fmt-video=width=432,height=240";
	} elseif( $resolution == 2) {
		$reso = "v4l2-ctl --device /dev/video0 --set-fmt-video=width=640,height=480";
	} elseif( $resolution == 3) {
		$reso = "v4l2-ctl --device /dev/video0 --set-fmt-video=width=960,height=544";
	} elseif( $resolution == 4) {
		$reso = "v4l2-ctl --device /dev/video0 --set-fmt-video=width=1280,height=720";
	} elseif( $resolution == 5) {
		$reso = "v4l2-ctl --device /dev/video0 --set-fmt-video=width=1920,height=1072";
	} else {
		$reso = "v4l2-ctl --device /dev/video0 --set-fmt-video=width=1280,height=720";
	}

$myfile = fopen("update.sh", "w") or die("Unable to open file!");
$txt = "/usr/bin/v4l2-ctl -d /dev/video0 --set-ctrl=quality_factor=".$qf." --set-ctrl=brightness=".$brightness." --set-ctrl=contrast=".$contrast." --set-ctrl=gain_red=".$cbred." --set-ctrl=gain_green=".$cbgreen." --set-ctrl=gain_blue=".$cbblue."\n";
fwrite($myfile, $txt);
$txt = "$reso"."\n";
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

header('Refresh: 3; URL=http://'.$ip_server.'/h264/index.htm?v_resolution='.$resolution.'&v_bright='.$brightness.'&v_contrast='.$contrast.'&v_qf='.$qf.'&v_red='.$cbred.'&v_green='.$cbgreen.'&v_blue='.$cbblue.'&stream=started');
exit();
?>

</body>
</html>
