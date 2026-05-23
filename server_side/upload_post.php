<?php
// 1. መላውን ዳታቤዝ ወደ ቴሌግራም የሚልከውን ረዳት ፋይል እዚህ ጋ ማገናኘት
include_once 'db_config.php';
include_once 'telegram_backup_helper.php';

// አንድሮይድ አፑ ፍቃድ እንዲያገኝ
header("Access-Control-Allow-Origin: *");

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $title = mysqli_real_escape_string($conn, $_POST['title']);
    $desc = mysqli_real_escape_string($conn, $_POST['description']);
    $category = mysqli_real_escape_string($conn, $_POST['category']);
    $image = $_POST['image']; // Base64 string

    $filename = "img_" . time() . ".jpg";
    $path = "uploads/" . $filename;

    if(file_put_contents($path, base64_decode($image))) {
        
        $sql = "INSERT INTO posts (title, description, image_path, category) 
                VALUES ('$title', '$desc', '$filename', '$category')";
        
        if($conn->query($sql) === TRUE) {
            
            // 🔥 2. አስማቱ እዚህ ጋ ነው! አዲስ ፖስት በተሳካ ሁኔታ ሲጫን 
            // የድሮውንም የአዲሱንም ዳታ (Users + Posts) ይዞ ቴሌግራም ላይ አውቶማቲክ አፕዴት ያደርጋል!
            sendFullDatabaseBackupToTelegram();
            
            echo "Success"; // አንድሮይድ ላይ በ .equalsIgnoreCase("Success") ስለሚፈትሽ
        } else {
            echo "Database Error: " . $conn->error;
        }
    } else {
        echo "Failed to upload image to server folder. Check uploads folder permissions.";
    }
} else {
    echo "Invalid Request Method";
}

$conn->close();
?>