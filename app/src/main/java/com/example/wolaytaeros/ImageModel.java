package com.example.wolaytaeros;

public class ImageModel {
    private String imageUrl;
    private String title;       // ይህ መኖሩን አረጋግጥ
    private String description;

    public ImageModel() {} // ለ Firebase አስፈላጊ ነው

    public ImageModel(String imageUrl, String title, String description) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
    }

    public String getImageUrl() { return imageUrl; }

    // ኤረሩን የሚያጠፋው ሜተድ ይህ ነው
    public String getTitle() { return title; }

    public String getDescription() { return description; }
}