package com.example.uniorproject.domain.mapper;

import static com.example.uniorproject.domain.mapper.PostMapper.postFromJson;

import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.User;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeMapper {

    public static Recipe recipeFromJson(JSONObject jsonObject){
        Recipe recipe = null;

        try {
            recipe = new Recipe(
                    jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    UserMapper.userFromRecipeJson(jsonObject),
                    jsonObject.getString("ingredients"),
                    jsonObject.getString("guide"),
                    jsonObject.getString("reccomendations"),
                    jsonObject.getInt("time"),
                    jsonObject.getInt("kcal"),
                    jsonObject.getInt("proteins"),
                    jsonObject.getInt("fats"),
                    jsonObject.getInt("carbohydrates"),
                    jsonObject.getInt("sugar"),
                    jsonObject.getInt("complexity"),
                    jsonObject.getString("tags"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipe;
    }

    public static Recipe recipeFromMealJson(JSONObject jsonObject){
        Recipe recipe = null;

        try {
            recipe = recipeFromJson(jsonObject.getJSONObject("recipe"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return recipe;
    }

    public static Recipe recipeFromPictureJson(JSONObject jsonObject){
        Recipe recipe = null;

        try {
            recipe = recipeFromJson(jsonObject.getJSONObject("recipe"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return recipe;
    }

    public static Recipe recipeFromRecipeLikeJson(JSONObject jsonObject){
        Recipe recipe = null;

        try {
            recipe = recipeFromJson(jsonObject.getJSONObject("recipeDto"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return recipe;
    }

    public static Post postFromPostLikeJson(JSONObject jsonObject){
        Post post = null;

        try {
            post = postFromJson(jsonObject.getJSONObject("postDto"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return post;
    }

    public static Recipe recipeFromRecipeCommentJson(JSONObject jsonObject){
        Recipe recipe = null;

        try {
            recipe = recipeFromJson(jsonObject.getJSONObject("recipe"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return recipe;
    }
}
