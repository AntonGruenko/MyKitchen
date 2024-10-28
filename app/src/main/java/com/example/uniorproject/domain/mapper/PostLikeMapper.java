package com.example.uniorproject.domain.mapper;

import com.example.uniorproject.domain.PostLike;

import org.json.JSONException;
import org.json.JSONObject;

public class PostLikeMapper {
    public static PostLike postLikeFromJson(JSONObject jsonObject){
        PostLike postLike = null;

        try {
            postLike = new PostLike(
                    jsonObject.getInt("id"),
                    RecipeMapper.postFromPostLikeJson(jsonObject),
                    UserMapper.userFromRecipeLikeJson(jsonObject)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postLike;
    }
}
