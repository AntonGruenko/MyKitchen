package com.example.uniorproject.domain;

public class Meal {
    private int id;
    private User user;
    private Recipe recipe;
    private Day day;

    public Meal(int id, User user, Recipe recipe, Day day) {
        this.id = id;
        this.user = user;
        this.recipe = recipe;
        this.day = day;
    }

    public Meal(User user, Recipe recipe, Day day) {
        this.user = user;
        this.recipe = recipe;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
