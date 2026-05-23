<?php
include 'db_config.php';

// አንድሮይድ አፑ ፍቃድ እንዲያገኝ
header("Access-Control-Allow-Origin: *");

if(isset($_POST['email']) && isset($_POST['password'])) {
    $email = mysqli_real_escape_string($conn, $_POST['email']);
    $password = $_POST['password'];

    // 1. መጀመሪያ ተጠቃሚውን በኢሜይል ብቻ መፈለግ
    $sql = "SELECT * FROM users WHERE email='$email'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $db_password_hash = $row['password']; // ዳታቤዝ ውስጥ ያለው ሃሽ

        // 2. ተጠቃሚው የጻፈው ፓስወርድ ከሃሹ ጋር መገጣጠሙን ማረጋገጥ
        if (password_verify($password, $db_password_hash)) {
            echo "success";
        } else {
            echo "failure"; // ፓስወርድ ስህተት ነው
        }
    } else {
        echo "failure"; // ኢሜይሉ አልተገኘም
    }
} else {
    echo "No data received";
}
$conn->close();
?>