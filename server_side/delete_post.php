<?php
// 1. መላውን ዳታቤዝ ወደ ቴሌግራም የሚልከውን ረዳት ፋይል እዚህ ጋ ማገናኘት
include_once 'db_config.php';
include_once 'telegram_backup_helper.php';

// አንድሮይድ አፑ ፍቃድ እንዲያገኝ
header("Access-Control-Allow-Origin: *");

if(isset($_POST['id'])) {
    // ID-ውን ወደ ቁጥር መቀየር (Security)
    $id = (int)$_POST['id'];

    $sql = "DELETE FROM posts WHERE id = $id";

    if($conn->query($sql) === TRUE) {
        
        // 🔥 2. አስማቱ እዚህ ጋ ነው! ፖስቱ ከዳታቤዙ ላይ ሲጠፋ 
        // የቀሩትን ሙሉ ዳታዎች (የድሮ ተጠቃሚዎች + የቀሩ ፖስቶች) ይዞ ቴሌግራም ላይ አውቶማቲክ አፕዴት ያደርጋል!
        sendFullDatabaseBackupToTelegram();
        
        echo "success";
    } else {
        echo "error";
    }
} else {
    echo "no id provided";
}

$conn->close();
?>