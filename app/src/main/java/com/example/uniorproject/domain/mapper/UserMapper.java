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
                    jsonObject.getString("profilePic"),
                    jsonObject.getInt("kcal"),
                    jsonObject.getInt("proteins"),
                    jsonObject.getInt("fats"),
                    jsonObject.getInt("carbohydrates"),
                    jsonObject.getLong("registrationDate"));
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

    public static User userFromMealJson(JSONObject jsonObject){
        User user = null;

        try {
            user = userFromJson(jsonObject.getJSONObject("user"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return user;
    }

    public static User userFromDayJson(JSONObject jsonObject){
        User user = null;

        try {
            user = userFromJson(jsonObject.getJSONObject("user"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return user;
    }

    public static User userFromRecipeLikeJson(JSONObject jsonObject){
        User user = null;

        try {
            user = userFromJson(jsonObject.getJSONObject("likerDto"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return user;
    }

    public static User userFromRecipeCommentJson(JSONObject jsonObject){
        User user = null;

        try {
            user = userFromJson(jsonObject.getJSONObject("author"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return user;
    }

    public static User userFromLeaderJson(JSONObject jsonObject){
        User user = null;

        try {
            user = userFromJson(jsonObject.getJSONObject("leader"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return user;
    }

    public static User userFromFollowerJson(JSONObject jsonObject){
        User user = null;

        try {
            user = userFromJson(jsonObject.getJSONObject("follower"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return user;
    }
}
