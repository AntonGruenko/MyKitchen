package com.example.uniorproject.domain;

public class Subscription {
    private int id;
    private User leader;
    private User follower;

    public Subscription(){

    }

    public Subscription(int id, User leader, User follower) {
        this.id = id;
        this.leader = leader;
        this.follower = follower;
    }

    public Subscription(User leader, User follower) {
        this.leader = leader;
        this.follower = follower;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }
}
