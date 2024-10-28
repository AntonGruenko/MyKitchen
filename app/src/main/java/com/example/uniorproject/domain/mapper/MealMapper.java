package com.example.uniorproject.domain.mapper;

import static com.example.uniorproject.domain.mapper.UserMapper.userFromJson;

import com.example.uniorproject.domain.Day;
import com.example.uniorproject.domain.Meal;
import com.example.uniorproject.domain.User;

import org.json.JSONException;
import org.json.JSONObject;

public class MealMapper {

    public static Meal mealFromJson(JSONObject jsonObject) {
        Meal meal = null;

        try{
            meal = new Meal(
                    jsonObject.getInt("id"),
                    UserMapper.userFromMealJson(jsonObject),
                    RecipeMapper.recipeFromMealJson(jsonObject),
                    DayMapper.dayFromMealJson(jsonObject));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return meal;
    }

}
