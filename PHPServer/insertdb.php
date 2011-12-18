<?php
	require('dbaccess.php');
	$json = $_SERVER['HTTP_JSON'];
	$data = json_decode($json);
	
	$con = mysql_connect("localhost", $user, $access);
	if (!$con) {
		die('Could not connect: ' . mysql_error());
	}
	mysql_query("SET character_set_results=utf8", $con);
	mb_language('uni');
	mb_internal_encoding('UTF-8');
	
	$command = $data->Command;
	if ($command == "New") {
		$fname = $data->Firstname;
		$lname = $data->Lastname;
		$major = $data->Major;
		$comment = $data->Comment;
		mysql_select_db("ajatest", $con);
		$q = 'INSERT INTO people VALUES ("'.$fname.'", "'.$lname.'", "'.$major.'", "'.$comment.'")'; 
		mysql_query($q);
		$sql = mysql_query('SELECT * FROM people');
	} elseif ($command == "Read") {
		mysql_select_db("ajatest", $con);
		$sql = mysql_query('SELECT * FROM college_eecs_csie');
	}
	
	while ($row = mysql_fetch_assoc($sql)) $output[] = $row;
	print(json_encode($output));
	mysql_close($con);
?>
