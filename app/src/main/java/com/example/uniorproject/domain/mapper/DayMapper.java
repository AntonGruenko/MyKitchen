package com.example.uniorproject.domain.mapper;

import com.example.uniorproject.domain.Day;
import com.example.uniorproject.domain.Meal;

import org.json.JSONException;
import org.json.JSONObject;

public class DayMapper {

    public static Day dayFromJson(JSONObject jsonObject){

        Day day = null;

        try {
            day = new Day(
                jsonObject.getInt("id"),
                UserMapper.userFromDayJson(jsonObject),
                jsonObject.getInt("day"),
                jsonObject.getInt("kcal"),
                jsonObject.getInt("proteins"),
                jsonObject.getInt("fats"),
                jsonObject.getInt("carbohydrates"),
                jsonObject.getBoolean("successful"));
            RecipeMapper.recipeFromMealJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return day;
    }

    public static Day dayFromMealJson(JSONObject jsonObject){

        Day day = null;

        try {
            day = dayFromJson(jsonObject.getJSONObject("days"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return day;
    }
}
