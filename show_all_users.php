<?php
$servername = "localhost";
$username = "u939969079_test2";
$password = "K4T8Zr5zsc";
$dbname = "u939969079_test2";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$Search_Name = strip_tags(trim($_GET["Search_Name"]));

$NumSub = 50;
$start = (int)$_GET['limit'];

$sql = "SELECT * FROM users  WHERE name LIKE '%$Search_Name%' ORDER BY id DESC LIMIT $start , $NumSub";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
	$All_users = array();
    while($row = $result->fetch_assoc()) {
        // echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. "<br>";
		$All_users[] = $row;
    }
} else {
    echo "0 results";
}
	$json_re=array();
	array_push($json_re,array("All_users"=>$All_users));
	echo json_encode($json_re);
$conn->close();
?>