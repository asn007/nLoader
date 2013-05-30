<?php

$skinDirSite = "skins/";
$skinDirURL = "http://minecraft.q-b.eu/mc/skins/";

switch($_GET['act']) {
	case "url":
		if(file_exists($skinDirSite.$_GET['user'].".png")) echo $skinDirURL.$_GET['user'].".png";
		else echo "http://s3.amazonaws.com/MinecraftSkins/char.png";
	break;
	case "upload":
		if($_FILES['skin']['error'] == 0) {
			if(isPNG($_FILES['skin']['tmp_name'])) {
				$skinImage = imagecreatefrompng($_FILES['skin']['tmp_name']);
				if(imagesx($skinImage) != 64 || imagesy($skinImage) != 32) echo "NOTASKIN";
				else {
					if(file_exists($skinDirSite.$_POST['user'].".png")) unlink($skinDirSite.$_POST['user'].".png");
					imagealphablending($skinImage, false);
					imagesavealpha($skinImage, true);
					imagepng($skinImage, $skinDirSite.$_POST['user'].".png");
					echo "OK";
				}
			} else echo "NOTASKIN";
		} else echo "UPLOADERROR";
	break;
}

function isPNG($filename) {
    if (!file_exists($filename)) {
        return false;
    }
    $png_header = array(137, 80, 78, 71, 13, 10, 26, 10);
    $f = fopen($filename, 'r');
    for ($i = 0; $i < 8; $i++) {
        $byte = ord(fread($f, 1));
        if ($byte !== $png_header[$i]) {
            fclose($f);
            return false;
        }
    }
    fclose($f);
    return true;
}

?>