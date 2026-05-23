<?php
// 1. መላውን ዳታቤዝ ወደ ቴሌግራም የሚልከውን ረዳት ፋይል እዚህ ጋ ማገናኘት
include_once 'db_config.php';
include_once 'telegram_backup_helper.php';

// አንድሮይድ አፑ ፍቃድ እንዲያገኝ
header("Access-Control-Allow-Origin: *");

if(isset($_POST['email']) && isset($_POST['password'])) {
    $email = mysqli_real_escape_string($conn, $_POST['email']);
    $password = $_POST['password'];

    // 1. መጀመሪያ ኢሜይሉ መኖሩን ቼክ ማድረግ
    $checkEmail = "SELECT * FROM users WHERE email='$email'";
    $checkResult = $conn->query($checkEmail);

    if($checkResult->num_rows > 0) {
        echo "Email already exists";
    } else {
        // 2. ፓስወርዱን በሃሽ መልክ መቀየር
        $hashed_password = password_hash($password, PASSWORD_DEFAULT);

        $sql = "INSERT INTO users (email, password) VALUES ('$email', '$hashed_password')";
        
        if ($conn->query($sql) === TRUE) {
            
            // 🔥 3. አዲስ ሰው ሲመዘገብ የድሮውንና አዲሱን ዳታ ጠቅልሎ ወደ ቴሌግራም የሚልከውን ፈንክሽን መጥራት
            sendFullDatabaseBackupToTelegram();
            
            echo "success";
        } else {
            echo "error";
        }
    }
} else {
    echo "No data received";
}
$conn->close();
?>