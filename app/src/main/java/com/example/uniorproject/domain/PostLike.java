package com.example.uniorproject.domain;

public class PostLike {
    private int id;
    private Post post;
    private User liker;

    public PostLike(Post post, User liker) {
        this.post = post;
        this.liker = liker;
    }

    public PostLike(int id, Post post, User liker) {
        this.id = id;
        this.post = post;
        this.liker = liker;
    }

    public PostLike() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getLiker() {
        return liker;
    }

    public void setLiker(User liker) {
        this.liker = liker;
    }
}
