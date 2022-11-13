package com.example.uniorproject.domain;

public class Recipe {
    private int id;
    private String name;
    private User author;
    private String ingredients;
    private String guide;
    private String reccomendations;
    private int time;
    private int kcal;
    private int proteins;
    private int fats;
    private int carbohydrates;
    private int sugar;
    private int likes;
    private int complexity;
    private String tags;

    public Recipe(
            int id,
            String name,
            User author,
            String ingredients,
            String guide,
            String reccomendations,
            int time,
            int kcal,
            int proteins,
            int fats,
            int carbohydrates,
            int sugar,
            int likes,
            int complexity,
            String tags) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.ingredients = ingredients;
        this.guide = guide;
        this.reccomendations = reccomendations;
        this.time = time;
        this.kcal = kcal;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.sugar = sugar;
        this.likes = likes;
        this.complexity = complexity;
        this.tags = tags;
    }

    public Recipe(
            String name,
            User author,
            String ingredients,
            String guide,
            String reccomendations,
            int time,
            int kcal,
            int proteins,
            int fats,
            int carbohydrates,
            int sugar,
            int likes,
            int complexity,
            String tags) {
        this.name = name;
        this.author = author;
        this.ingredients = ingredients;
        this.guide = guide;
        this.reccomendations = reccomendations;
        this.time = time;
        this.kcal = kcal;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.sugar = sugar;
        this.likes = likes;
        this.complexity = complexity;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getAuthor() {
        return author;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getGuide() {
        return guide;
    }

    public String getReccomendations() {
        return reccomendations;
    }

    public int getTime() {
        return time;
    }

    public int getKcal() {
        return kcal;
    }

    public int getProteins() {
        return proteins;
    }

    public int getFats() {
        return fats;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public int getSugar() {
        return sugar;
    }

    public int getLikes() {
        return likes;
    }

    public int getComplexity() {
        return complexity;
    }

    public String getTags() {
        return tags;
    }
}
