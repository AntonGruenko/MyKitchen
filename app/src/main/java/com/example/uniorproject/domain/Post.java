package com.example.uniorproject.domain;

public class Post {
    private int id;
    private User author;
    private String text;
    private String picture;

    public Post(int id, User author, String text, String picture) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.picture = picture;
    }

    public Post(User author, String text, String picture) {
        this.author = author;
        this.text = text;
        this.picture = picture;
    }

    public Post() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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
}
