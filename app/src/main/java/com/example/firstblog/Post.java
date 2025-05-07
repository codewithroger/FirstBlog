package com.example.firstblog;

public class Post {
    private String title;
    private String content;
    private String imageUrl;

    // Default constructor required for Firebase
    public Post() {}

    public Post(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
