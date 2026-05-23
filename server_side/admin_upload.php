<?php
// ዳታቤዝ ግንኙነት
$conn = new mysqli("localhost", "root", "", "wolayta_db");

if(isset($_POST['submit'])){
    $title = $conn->real_escape_string($_POST['title']);
    $desc = $conn->real_escape_string($_POST['description']);
    $cat = $_POST['category']; // ከ Dropdown የመጣ
    
    $image_name = $_FILES['image']['name'];
    $target = "uploads/" . basename($image_name);

    if (move_uploaded_file($_FILES['image']['tmp_name'], $target)) {
        $sql = "INSERT INTO posts (title, description, category, image_path) VALUES ('$title', '$desc', '$cat', '$image_name')";
        if($conn->query($sql)){ 
            echo "<div style='padding:10px; background: #d4edda; color: #155724; margin-bottom:20px; border-radius:5px;'>✅ መረጃው በተሳካ ሁኔታ ተጭኗል!</div>"; 
        } else {
            echo "<div style='color:red;'>Error: " . $conn->error . "</div>";
        }
    }
}
?>

<!DOCTYPE html>
<html lang="am">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Wolayta Eros - Admin Panel</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f0f2f5; padding: 40px; display: flex; justify-content: center; }
        .container { background: white; padding: 30px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); width: 100%; max-width: 600px; }
        h2 { color: #1a73e8; text-align: center; margin-bottom: 25px; }
        label { font-weight: bold; display: block; margin-bottom: 5px; color: #555; }
        input, textarea, select { width: 100%; padding: 12px; margin-bottom: 20px; border: 1px solid #ddd; border-radius: 6px; box-sizing: border-box; font-size: 16px; }
        textarea { resize: vertical; }
        button { background: #1a73e8; color: white; padding: 14px; border: none; border-radius: 6px; cursor: pointer; width: 100%; font-size: 18px; font-weight: bold; transition: 0.3s; }
        button:hover { background: #1557b0; }
    </style>
</head>
<body>

<div class="container">
    <h2>የዎላይታ ኤሮስ መረጃ መጫኛ (Admin)</h2>
    
    <form method="POST" action="" enctype="multipart/form-data">
        <label>የመረጃው ርዕስ (Title):</label>
        <input type="text" name="title" placeholder="ርዕስ ያስገቡ..." required>

        <label>ዝርዝር መግለጫ (Description):</label>
        <textarea name="description" rows="6" placeholder="ሙሉ መግለጫውን እዚህ ይጻፉ..." required></textarea>

        <label>ካቴጎሪ ይምረጡ (Category):</label>
        <select name="category" required>
            <option value="">-- ካቴጎሪ ይምረጡ --</option>
            <option value="Politics">Politics (ፖለቲካ)</option>
            <option value="Cultural Food">Cultural Food (ባህላዊ ምግቦች)</option>
            <option value="Culture">Culture (ጭፈራና አልባሳት)</option>
            <option value="Advertisement">Advertisement (ማስታወቂያ)</option>
            <option value="Education">Education (ትምህርት)</option>
            <option value="Infrastructure">Infrastructure (መሰረተ ልማት)</option>
            <option value="Nature">Nature (ተፈጥሮ)</option>
            <option value="Leader of Wolayta">Leader of Wolayta (የዎላይታ መሪዎች)</option>
            <option value="Wild and Domestic Animal">Wild and Domestic Animal (እንስሳት)</option>
            <option value="Job Work">Job Work (ስራና ሰራተኛ)</option>
        </select>

        <label>ምስል ይምረጡ:</label>
        <input type="file" name="image" accept="image/*" required>

        <button type="submit" name="submit">መረጃውን ጫን</button>
    </form>
</div>

</body>
</html>