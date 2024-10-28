package com.example.uniorproject.domain;

public class Picture {
    private int id;
    private String link;
    private Recipe recipe;
    private int number;
    private int width;
    private int height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Picture(int id, String link, Recipe recipe, int number) {
        this.id = id;
        this.link = link;
        this.recipe = recipe;
        this.number = number;
    }

    public Picture(String link, Recipe recipe, int number) {
        this.link = link;
        this.recipe = recipe;
        this.number = number;
    }

    public Picture() {
    }
}
