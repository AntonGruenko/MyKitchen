package com.example.uniorproject.domain.mapper;

import com.example.uniorproject.domain.Picture;

import org.json.JSONException;
import org.json.JSONObject;

public class PictureMapper {
    public static Picture pictureFromJson(JSONObject jsonObject){
        Picture picture = null;

        try {
            picture = new Picture(
                    jsonObject.getInt("id"),
                    jsonObject.getString("link"),
                    RecipeMapper.recipeFromPictureJson(jsonObject),
                    jsonObject.getInt("number")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return picture;
    }
}
