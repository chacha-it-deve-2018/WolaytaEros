<?php
include 'db_config.php';

// ሁሉንም ፖስቶች እንዲያመጣ (አዳዲሶቹ ከላይ እንዲሆኑ በ ID DESC)
$sql = "SELECT * FROM posts ORDER BY id DESC";
$result = $conn->query($sql);

$posts = array();

if ($result && $result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $posts[] = $row;
    }
}

// ለአንድሮይድ በ JSON መልክ ይልካል
header('Content-Type: application/json');
echo json_encode($posts);

$conn->close();
?>