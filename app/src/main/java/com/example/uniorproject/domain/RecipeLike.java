package com.example.uniorproject.domain;

public class RecipeLike {
    private int id;
    private Recipe recipe;
    private User liker;

    public RecipeLike(Recipe recipe, User liker) {
        this.recipe = recipe;
        this.liker = liker;
    }

    public RecipeLike(int id, Recipe recipe, User liker) {
        this.id = id;
        this.recipe = recipe;
        this.liker = liker;
    }

    public RecipeLike() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public User getLiker() {
        return liker;
    }

    public void setLiker(User liker) {
        this.liker = liker;
    }
}
