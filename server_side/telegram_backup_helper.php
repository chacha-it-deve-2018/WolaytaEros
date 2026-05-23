<?php
function sendFullDatabaseBackupToTelegram() {
    $db_host = "localhost";
    $db_user = "root";
    $db_pass = "";
    $db_name = "wolayta_db";

    $conn = new mysqli($db_host, $db_user, $db_pass, $db_name);
    if ($conn->connect_error) { return; }

    // 🔥 ያንተ ትክክለኛ የቦት መረጃዎች እዚህ አሉ
    $botToken = "8788311052:AAFJAmsT-i1v18WjnunurSb5AXbx7ljguUE"; 
    $chatId = "@wolayta_backup_test"; 

    $backup_file_name = "wolayta_eros_complete_backup.sql";
    $file_path = __DIR__ . '/' . $backup_file_name;

    $file_handle = fopen($file_path, 'w+');
    fwrite($file_handle, "-- Wolayta Eros - COMPLETE DATABASE BACKUP (All Past & New Data) --\n");
    fwrite($file_handle, "-- Backup Date: " . date("Y-m-d H:i:s") . " --\n\n");

    $tables = ['users', 'posts'];
    foreach ($tables as $table) {
        // 1. የሰንጠረዡን መዋቅር (Structure) መቅዳት
        $result = $conn->query("SHOW CREATE TABLE $table");
        if ($result) {
            $row = $result->fetch_row();
            fwrite($file_handle, "\n\n" . $row[1] . ";\n\n");
        }
        
        // 2. የድሮውንም የአዲሱንም ዳታ በሙሉ መቅዳት
        $result = $conn->query("SELECT * FROM $table");
        if ($result) {
            $num_fields = $result->field_count;
            while ($row = $result->fetch_row()) {
                $sql_insert = "INSERT INTO $table VALUES(";
                for ($j = 0; $j < $num_fields; $j++) {
                    if (isset($row[$j])) {
                        $sql_insert .= '"' . $conn->real_escape_string($row[$j]) . '"';
                    } else {
                        $sql_insert .= 'NULL';
                    }
                    if ($j < ($num_fields - 1)) { $sql_insert .= ','; }
                }
                $sql_insert .= ");\n";
                fwrite($file_handle, $sql_insert);
            }
        }
    }
    fclose($file_handle);

    // 3. 🔥 ለአስተማማኝ መላኪያ የተስተካከለው የ cURL እና CURLFile ሲስተም
    $telegramUrl = "https://api.telegram.org/bot" . $botToken . "/sendDocument";
    
    // PHP 5.5+ እስከ PHP 8+ ድረስ 100% የሚሰራው ይሄኛው አዲስ አጻጻፍ ነው፡
    $cFile = curl_file_create($file_path, 'application/octet-stream', $backup_file_name);

    $post_fields = [
        'chat_id'   => $chatId,
        'caption'   => "🔄 *Database Updated!*\n📊 Status: All previous & new data backed up.\n📂 Tables: users, posts\n📅 " . date("Y-m-d H:i:s"),
        'parse_mode'=> 'Markdown',
        'document'  => $cFile
    ];

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $telegramUrl);
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $post_fields);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    
    // 🔴 ይህ መስመር ለ XAMPP (Localhost) በጣም ወሳኝ ነው (የ SSL ሰርተፍኬት ቼክ እንዳያደርግ ይከለክላል)
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); 
    
    curl_exec($ch);
    curl_close($ch);

    // ከሰርቨሩ ላይ ጊዜያዊ ፋይሉን ማጥፋት
    if (file_exists($file_path)) { unlink($file_path); }
    $conn->close();
}
?>