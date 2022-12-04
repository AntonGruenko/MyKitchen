package com.example.uniorproject.noDb;

import com.example.uniorproject.database.Product;
import com.example.uniorproject.domain.Picture;
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
    public static final List<Picture> PICTURE_LIST = new ArrayList<>();
    public static final List<String> PICTURE_LINK_LIST = new ArrayList<>();
    public static final List<String> INGREDIENTS_LIST = new ArrayList<>();
    public static final List<String> GUIDE_LIST = new ArrayList<>();
    public static final List<String> TAG_LIST = new ArrayList<>();
    public static final List<String> SELECTED_TAG_LIST = new ArrayList<>();
    public static final List<Product> PRODUCT_LIST = new ArrayList<>();
    public static final List<Picture> RECIPE_PICTURE_LIST = new ArrayList<>();

}
