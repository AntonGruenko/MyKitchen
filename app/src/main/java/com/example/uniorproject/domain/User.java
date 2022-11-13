package com.example.uniorproject.domain;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String status;
    private String profilePic;

    public User(String name, String email, String password, String status, String profilePic) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
        this.profilePic = profilePic;
    }

    public User(int id, String name, String email, String password, String status, String profilePic) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
        this.profilePic = profilePic;
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
}
