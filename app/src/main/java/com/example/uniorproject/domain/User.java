package com.example.uniorproject.domain;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String status;
    private String profilePic;
    private int kcal;
    private int proteins;
    private int fats;
    private int carbohydrates;
    private long registrationDate;

    public User(String name, String email, String password, String status, String profilePic, int kcal, int proteins, int fats, int carbohydrates, long registrationDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
        this.profilePic = profilePic;
        this.kcal = kcal;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.registrationDate = registrationDate;
    }

    public User() {
    }

    public User(int id, String name, String email, String password, String status, String profilePic, int kcal, int proteins, int fats, int carbohydrates, long registrationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
        this.profilePic = profilePic;
        this.kcal = kcal;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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

    public long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(long registrationDate) {
        this.registrationDate = registrationDate;
    }
}
