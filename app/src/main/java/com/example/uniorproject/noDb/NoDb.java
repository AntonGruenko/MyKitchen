package com.example.uniorproject.noDb;

import com.example.uniorproject.adapter.RecipeSearchAdapter;
import com.example.uniorproject.database.Product;
import com.example.uniorproject.domain.Day;
import com.example.uniorproject.domain.Meal;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.RecipeComment;
import com.example.uniorproject.domain.RecipeLike;
import com.example.uniorproject.domain.Subscription;
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
    public static final List<Meal> MEALS_LIST = new ArrayList<>();
    public static final List<Day> DAY_LIST = new ArrayList<>();
    public static final List<RecipeLike> RECIPE_LIKES_LIST = new ArrayList<>();
    public static final List<RecipeComment> RECIPE_COMMENTS_LIST = new ArrayList<>();
    public static final List<User> FOUND_USERS_LIST = new ArrayList<>();
    public static final List<Subscription> SUBSCRIPTION_LIST = new ArrayList<>();
    public static final List<RecipeSearchAdapter.RecipeSearchText> SEARCH_INGREDIENTS_LIST = new ArrayList<>();
    public static final List<RecipeSearchAdapter.RecipeSearchText> SEARCH_TAGS_LIST = new ArrayList<>();
    public static final List<Recipe> RECIPE_BY_COMPLEXITY_LIST = new ArrayList<>();
    public static final List<Recipe> RECIPE_BY_KCAL_LIST = new ArrayList<>();
    public static final List<Recipe> RECIPE_BY_PROTEINS_LIST = new ArrayList<>();
    public static final List<Recipe> RECIPE_BY_FATS_LIST = new ArrayList<>();
    public static final List<Recipe> RECIPE_BY_CARBOHYDRATES_LIST = new ArrayList<>();
    public static final List<Recipe> RECIPE_BY_TIME_LIST = new ArrayList<>();
    public static final List<Recipe> RECIPE_BY_INGREDIENTS_LIST = new ArrayList<>();
    public static final List<Recipe> RECIPE_BY_INGREDIENTS_NOT_LIST = new ArrayList<>();
    public static final List<Recipe> RECIPE_BY_TAGS_LIST = new ArrayList<>();
    public static final List<Recipe> RECIPE_BY_TAGS_NOT_LIST = new ArrayList<>();

}
