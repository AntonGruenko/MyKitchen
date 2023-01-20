package com.example.uniorproject.domain;

public class RecipeComment {
    private int id;
    private User author;
    private Recipe recipe;
    private String text;
    private int likes;

    public RecipeComment(int id, User author, Recipe recipe, String text, int likes) {
        this.id = id;
        this.author = author;
        this.recipe = recipe;
        this.text = text;
        this.likes = likes;
    }

    public RecipeComment(User author, Recipe recipe, String text, int likes) {
        this.author = author;
        this.recipe = recipe;
        this.text = text;
        this.likes = likes;
    }

    public RecipeComment() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
