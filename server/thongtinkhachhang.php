<?php 
	include "connect.php";
	$tenkhachhang = $_POST['tenkhachhang'];
	$sodienthoai = $_POST['sodienthoai'];
	$email = $_POST['email'];
	if (strlen($tenkhachhang) >0 && strlen($sodienthoai)>0 && strlen($email)>0 ) {
		$query = "INSERT INTO donhang(id, tenkhachhang, sodienthoai, email) VALUES (null, '$tenkhachhang', '$sodienthoai', '$email')";
		if (mysqli_query($conn, $query)) {
			$iddonhang = $conn->insert_id;
			echo $iddonhang;
		}else{
			echo "Thất bại";
		}
	}else{
		echo "Bạn hãy kiểm tra lại các dữ liệu";
	}
?>