package com.example.uniorproject.domain.mapper;

import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.User;

import org.json.JSONException;
import org.json.JSONObject;

public class PostMapper {
    public static Post postFromJson(JSONObject jsonObject)  {
        Post post = null;

        try {
            post = new Post(
                    jsonObject.getInt("id"),
                    UserMapper.userFromPostJson(jsonObject),
                    jsonObject.getString("text"),
                    jsonObject.getString("picture"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return post;
    }
}
