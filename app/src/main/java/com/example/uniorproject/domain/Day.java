package com.example.uniorproject.domain;

public class Day {
    private int id;
    private User user;
    private int day;
    private int kcal;
    private int proteins;
    private int fats;
    private int carbohydrates;
    private boolean isSuccessful;

    public Day(int id, User user, int day, int kcal, int proteins, int fats, int carbohydrates, boolean isSuccessful) {
        this.id = id;
        this.user = user;
        this.day = day;
        this.kcal = kcal;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.isSuccessful = isSuccessful;
    }

    public Day(User user, int day, int kcal, int proteins, int fats, int carbohydrates, boolean isSuccessful) {
        this.user = user;
        this.day = day;
        this.kcal = kcal;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.isSuccessful = isSuccessful;
    }

    public Day() {

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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getProteins() {
        return proteins;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}
