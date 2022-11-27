package com.example.uniorproject.firebase;

public class Uploader {
    String name;
    String url;

    public Uploader(){

    }

    public Uploader(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
