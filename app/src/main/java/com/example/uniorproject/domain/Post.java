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

    public void setLikes(int likes) {
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
