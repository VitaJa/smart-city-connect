<?php
	require_once("upload_res.php");
	
	$MAX_SIZE = 5;

	$allowed_dir = array(
		'/uploads/place/',
		'/uploads/news/'
	);

	// validation is file image
	if(!isImage($_FILES['file']['tmp_name'])){
		$res->response($res->json(array('status' => "failed", "msg" => "Failed format")), 200);
	}
	
	// validation file size
	if($_FILES['file']['size'] > ($MAX_SIZE * 1000000)){
		$res->response($res->json(array('status' => "failed", "msg" => "Exceed file size")), 200);
	}

	$base_path 			= dirname(dirname(dirname(__FILE__)));
	$target_dir 		= $_POST["target_dir"];
	$file_name 			= $_POST["file_name"];
	$old_name 			= $_POST["old_name"];

	// validation directory
	if(!in_array($target_dir, $allowed_dir)){
		$res->response($res->json(array('status' => "failed", "msg" => "Invalid target dir")), 200);
	}

	// validation file name
	if($file_name != basename($file_name)){
		$res->response($res->json(array('status' => "failed", "msg" => "Invalid file name")), 200);
	}

	// validation old name
	if($old_name != "" && $old_name != basename($old_name)){
		$res->response($res->json(array('status' => "failed", "msg" => "Invalid old name")), 200);
	}

	$target_file 		= $base_path . $target_dir . $file_name;
	$target_file_old 	= $base_path . $target_dir . $old_name;

	$res = new UploadRes();

	// Check if new file already exists
	if (file_exists($target_file)) {
		unlink($target_file);
	}
	
	// Check if old file already exists
	if ($old_name != "" && file_exists($target_file_old)) {
		unlink($target_file_old);
	}

	// Check if $uploadOk is set to 0 by an error
	if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file)) {
		$success = array('status' => "success", "name" => $file_name);
		$res->response($res->json($success), 200);
	} else {
		$success = array('status' => "failed", "msg" => "Failed uploading file");
		$res->response($res->json($success), 200);
	}

	function isImage($path){
        if(!$a = getimagesize($path)){
            return false;
        };
        $image_type = $a[2];
        $support_file = array(
            IMAGETYPE_GIF, 
            IMAGETYPE_JPEG,
            IMAGETYPE_PNG, 
            IMAGETYPE_BMP
        );
        if(in_array($image_type, $support_file)){
            return true;
        }
        return false;
    }

?>
