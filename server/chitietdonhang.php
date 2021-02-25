<?php
	include "connect.php";
	$json = $_POST['json'];
	$data = json_decode($json,true);
	if (is_array($data) || is_object($data)){
		foreach ($data as $value) {
			$madonhang = $value['madonhang'];
			$masanpham = $value['masanpham'];
			$tensanpham = $value['tensanpham'];
			$giasanpham = $value['giasanpham'];
			$soluongsanpham = $value['soluongsanpham'];
			$query = "INSERT INTO chitietdonhang (id, madonhang, masanpham, tensanpham, giasanpham, soluongsanpham) VALUES (null, '$madonhang', '$masanpham','$tensanpham', '$giasanpham', '$soluongsanpham')" ;
		$Data = mysqli_query($conn, $query);
		}
	}
	if ($Data) {
		echo"1";
	}else{
		echo"0";
	}

?>