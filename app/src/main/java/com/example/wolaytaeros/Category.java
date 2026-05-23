package com.example.wolaytaeros;

public class Category {
    private String name;        // በየቋንቋው የሚቀያየረው (ለሰው የሚታየው ስም)
    private int imageRes;       // የካቴጎሪው አይኮን/ምስል አድራሻ
    private String databaseKey; // 🔥 ሁልጊዜ በእንግሊዝኛ የሚቀረው (ለXAMPP ዳታቤዝ የሚላከው ቁልፍ)

    // የተስተካከለው አዲሱ ኮንስትራክተር (databaseKey-ን እንዲቀበል ተደርጓል)
    public Category(String name, int imageRes, String databaseKey) {
        this.name = name;
        this.imageRes = imageRes;
        this.databaseKey = databaseKey;
    }

    public String getName() {
        return name;
    }

    public int getImageRes() {
        return imageRes;
    }

    // 🔥 አዲሱ ጌተር ፈንክሽን (ይህንን በ CategoriesFragment ላይ እንጠቀመዋለን)
    public String getDatabaseKey() {
        return databaseKey;
    }
}