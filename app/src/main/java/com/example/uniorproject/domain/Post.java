package com.example.uniorproject.domain;

public class Post {
    private int id;
    private User author;
    private String text;
    private String picture;
    private int likes;

    public Post(int id, User author, String text, String picture, int likes) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.picture = picture;
        this.likes = likes;
    }

    public Post(User author, String text, String picture, int likes) {
        this.author = author;
        this.text = text;
        this.picture = picture;
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getPicture() {
        return picture;
    }

    public int getLikes() {
        return likes;
    }
}
