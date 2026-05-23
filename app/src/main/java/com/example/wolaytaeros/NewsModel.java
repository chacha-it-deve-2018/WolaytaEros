package com.example.wolaytaeros;

public class NewsModel {
    private int id;
    private String title;
    private String description;
    private String image_path;

    public NewsModel() {}

    public NewsModel(int id, String title, String description, String image_path) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image_path = image_path;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // ለ 'NewsAdapter' እንዲመች
    public String getImagePath() { return image_path; }
    public void setImagePath(String image_path) { this.image_path = image_path; }
}