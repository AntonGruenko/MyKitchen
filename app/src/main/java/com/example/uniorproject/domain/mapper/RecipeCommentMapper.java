package com.example.uniorproject.domain.mapper;

import com.example.uniorproject.domain.RecipeComment;
import com.example.uniorproject.domain.RecipeLike;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeCommentMapper {
    public static RecipeComment recipeCommentFromJson(JSONObject jsonObject){
        RecipeComment recipeComment = null;

        try {
            recipeComment = new RecipeComment(
                    jsonObject.getInt("id"),
                    UserMapper.userFromRecipeCommentJson(jsonObject),
                    RecipeMapper.recipeFromRecipeCommentJson(jsonObject),
                    jsonObject.getString("text"),
                    jsonObject.getInt("likes")

            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeComment;
    }
}
