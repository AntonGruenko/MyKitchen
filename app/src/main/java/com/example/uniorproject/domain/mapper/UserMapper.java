package com.example.uniorproject.domain.mapper;

import com.example.uniorproject.domain.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class UserMapper {

    public static User userFromJson(JSONObject jsonObject)  {
        User user = null;

        try {
            user = new User(
                    jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("email"),
                    jsonObject.getString("password"),
                    jsonObject.getString("status"),
                    jsonObject.getString("profilePic"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static User userFromRecipeJson(JSONObject jsonObject)  {
        User user = null;

        try {
            user = userFromJson(jsonObject.getJSONObject("authorDto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static User userFromPostJson(JSONObject jsonObject)  {
        User user = null;

        try {
            user = userFromJson(jsonObject.getJSONObject("authorDto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
