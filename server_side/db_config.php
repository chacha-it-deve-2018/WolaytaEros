<?php
$host = "localhost";
$user = "root";
$pass = "";
$db = "wolayta_db";

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
// ለአማርኛ ጽሁፍ እንዳይበላሽ
$conn->set_charset("utf8");
?>