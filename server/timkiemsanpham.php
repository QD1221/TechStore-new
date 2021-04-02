<?php
	include "connect.php";
	$mangsanpham = array();
	if (isset($_POST['tukhoa'])) {
		$tukhoa = $_POST['tukhoa'];
		$query = "SELECT * FROM sanpham WHERE lower(tensanpham) LIKE '%$tukhoa%'";
		$data = mysqli_query($conn, $query);
		while ($row = mysqli_fetch_assoc($data)) {
			array_push($mangsanpham, new Sanpham(
				$row['id'], 
				$row['tensanpham'], 
				$row['giasanpham'], 
				$row['hinhanhsanpham'], 
				$row['motasanpham'], 
				$row['idsanpham']));
		}
		echo json_encode($mangsanpham);
	}
	
	class Sanpham {
		
		function Sanpham($ID,$Tensanpham,$Giasanpham,$Hinhanhsanpham,$Motasanpham,$IDsanpham)
		{
			$this->ID = $ID;
			$this->Tensanpham = $Tensanpham;
			$this->Giasanpham = $Giasanpham;
			$this->Hinhanhsanpham=$Hinhanhsanpham;
			$this->Motasanpham=$Motasanpham;
			$this->IDsanpham=$IDsanpham;
		}
	}
	
?>