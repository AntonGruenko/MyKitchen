package com.example.uniorproject.domain;

public class Message{
    private int id;
    private User sender;
    private User receiver;
    private String text;
    private String picture;

    public Message(int id, User sender, User receiver, String text, String picture) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.picture = picture;
    }

    public Message(User sender, User receiver, String text, String picture) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.picture = picture;
    }

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
