package com.example.uniorproject.noDb;

import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.User;

import java.util.ArrayList;
import java.util.List;

public class NoDb {
    private NoDb noDb;

    public static final List<User> USER_LIST = new ArrayList<>();
    public static final List<Recipe> RECIPE_LIST = new ArrayList<>();
    public static final List<Post> POST_LIST = new ArrayList<>();
    public static final List<String> INGREDIENTS_LIST = new ArrayList<>();
    public static final List<String> GUIDE_LIST = new ArrayList<>();
    public static final List<String> TAG_LIST = new ArrayList<>();
    public static final List<String> SELECTED_TAG_LIST = new ArrayList<>();

}
