package com.example.uniorproject.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

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

    public int getComplexity() {
        return complexity;
    }

    public String getTags() {
        return tags;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public void setReccomendations(String reccomendations) {
        this.reccomendations = reccomendations;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
