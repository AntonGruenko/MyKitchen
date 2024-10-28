package com.example.uniorproject.domain.mapper;

import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.RecipeLike;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeLikeMapper {
    public static RecipeLike recipeLikeFromJson(JSONObject jsonObject){
        RecipeLike recipeLike = null;

        try {
            recipeLike = new RecipeLike(
                    jsonObject.getInt("id"),
                    RecipeMapper.recipeFromRecipeLikeJson(jsonObject),
                    UserMapper.userFromRecipeLikeJson(jsonObject)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeLike;
    }
}
