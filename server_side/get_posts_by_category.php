<?php
// 1. የዳታቤዝ ግንኙነቱን ፋይል አካትት
include 'db_config.php';

// ለአንድሮይድ አፑ መረጃው በ JSON መልክ መሆኑን ለመንገር
header('Content-Type: application/json');

// አንድሮይድ አፑ የሚልከው 'category' በሚል ስም ስለሆነ እሱን ቼክ እናደርጋለን
if(isset($_GET['category'])) {
    $category = $_GET['category'];

    // SQL Injection ለመከላከል
    $category = mysqli_real_escape_string($conn, $category);
    
    // በካቴጎሪው ስም ብቻ ፈልግ
    $sql = "SELECT * FROM posts WHERE category='$category' ORDER BY id DESC";
    $result = $conn->query($sql);

    $posts = array();

    if ($result && $result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            $posts[] = $row;
        }
        echo json_encode($posts);
    } else {
        // መረጃ ከሌለ ባዶ [] ይልካል
        echo json_encode([]);
    }
} else {
    echo json_encode(["error" => "No category specified."]);
}

$conn->close();
?>