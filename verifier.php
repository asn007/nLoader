<?php
$dir = "clients/";
switch ($_GET['act']) {
	case 'list':
		printSums($_GET['client']);
	break;
	case 'verify':
		$file = $_GET['file'];
		$hash = $_GET['hash'];
		if(!file_exists($dir.$_GET['client'].'/'.$file) || $hash != md5_file($dir.$_GET['client'].'/'.$file)) echo "no";
		else echo "ok";
	break;
	case 'dirs':
		printDirs($_GET['client']);
	break;
}

function printDirs($cli) {
	global $dir;
	foreach(glob($dir.$cli.'/*', GLOB_ONLYDIR) as $directory) {
  		echo str_replace($dir.$cli.'/', '', $directory).'<br />';
	}  
}

function printSums($cli) {
	global $dir;
	$arr = directoryToArray($dir.$cli.'/', true);
	sort($arr, SORT_NATURAL);
	$val = "";
	foreach($arr as $file) $val.=str_replace($dir.$cli.'/', '', $file)."<::::>".md5_file($file)."<br />";
	$val = str_lreplace("<br />", "", $val);
	echo $val;
	/*
	if (is_dir($dir)) {
   		if ($dh = opendir($dir)) {
       		while (($file = readdir($dh)) !== false) {
		   		if($file == "." || $file == "..") print "";
           		else print $file."<::::>".md5_file($dir . $file)."<br />";
       		}
       		closedir($dh);
  		} else die("Failed to open directory!");
	} else die("File does not exist or is not a directory!");
	*/
}

function directoryToArray($directory, $recursive) {
	$array_items = array();
	if ($handle = opendir($directory)) {
		while (false !== ($file = readdir($handle))) {
			if ($file != "." && $file != "..") {
				if (is_dir($directory. "/" . $file)) {
					if($recursive) {
						$array_items = array_merge($array_items, directoryToArray($directory. "/" . $file, $recursive));
					}
					$file = $directory . "/" . $file;
					#$array_items[] = preg_replace("/\/\//si", "/", $file);
				} else {
					$file = $directory . "/" . $file;
					$array_items[] = preg_replace("/\/\//si", "/", $file);
				}
			}
		}
		closedir($handle);
	}
	return $array_items;
}

function str_lreplace($search, $replace, $subject) {
    $pos = strrpos($subject, $search);
    if($pos !== false)
        $subject = substr_replace($subject, $replace, $pos, strlen($search));
    return $subject;
}

exit;
?>