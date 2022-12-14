package com.example.uniorproject.domain.mapper;

import com.example.uniorproject.domain.Recipe;

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
                    jsonObject.getInt("likes"),
                    jsonObject.getInt("complexity"),
                    jsonObject.getString("tags"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipe;
    }
}
