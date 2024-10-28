package com.example.uniorproject.rest;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.domain.Day;
import com.example.uniorproject.domain.Meal;
import com.example.uniorproject.domain.Message;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.PostLike;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.RecipeComment;
import com.example.uniorproject.domain.RecipeLike;
import com.example.uniorproject.domain.Subscription;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.DayMapper;
import com.example.uniorproject.domain.mapper.MealMapper;
import com.example.uniorproject.domain.mapper.MessageMapper;
import com.example.uniorproject.domain.mapper.PictureMapper;
import com.example.uniorproject.domain.mapper.PostMapper;
import com.example.uniorproject.domain.mapper.RecipeCommentMapper;
import com.example.uniorproject.domain.mapper.RecipeMapper;
import com.example.uniorproject.domain.mapper.SubscriptionMapper;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.noDb.NoDb;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolleyAPI implements AppAPI{

    public static final String API = "API";
    private final Context context;
    public static final String BASE_URL = "https://1bc3-91-227-189-27.ngrok-free.app";
    private final Response.ErrorListener errorListener;
    
    public VolleyAPI(Context context) {
        this.context = context;
        errorListener = error -> {

        };
    }

    @Override
    public void fillUser() {
        String url = BASE_URL + "/user";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.USER_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                User user = UserMapper.userFromJson(jsonObject);
                                NoDb.USER_LIST.add(user);
                            }

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findUserById(int id, VolleyCallback callback) {
        String url = BASE_URL + "/user/" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },

                errorListener
        );

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void findUsersByName(String name, VolleyCallback callback) {
        String url = BASE_URL + "/user/name/" + name;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.FOUND_USERS_LIST.clear();
                        for(int i = 0; i < response.length(); ++i){
                            User user = new User();
                            try {
                                user = UserMapper.userFromJson(response.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            NoDb.FOUND_USERS_LIST.add(user);
                        }
                        callback.onSuccess(null);
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void fillRecipe(VolleyCallback callback) {
        String url = BASE_URL + "/recipe";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_LIST.add(recipe);
                                try {
                                    ((MainActivity) context).updateRecipeAdapter();
                                } catch(NullPointerException e){

                                }
                            }
                            Collections.reverse(NoDb.RECIPE_LIST);

                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findRecipeById(int id, VolleyCallback callback) {
        String url = BASE_URL + "/recipe/" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },

                errorListener
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void findRecipesByAuthor(int authorId, VolleyCallback callback){
        String url = BASE_URL + "/recipe/author/" + authorId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_LIST.add(recipe);
                            }

                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    public void findRecipesByTitle(String title, VolleyCallback callback){
        String url = BASE_URL + "/recipe/name/" + title;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_LIST.add(recipe);
                            }

                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findRecipesByAuthors(List<Integer> authors, VolleyCallback callback) {
        String url = BASE_URL + "/recipe/authors/";
        for (int i = 0; i < authors.size(); ++i){
            if(i == 0){
                url += authors.get(i);
            }
            else {
                url = url + "," + authors.get(i);
            }
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_LIST.add(recipe);
                            }
                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );
        if(authors.size() > 0) {
            requestQueue.add(jsonArrayRequest);
        }
        else {
            NoDb.RECIPE_LIST.clear();
            requestQueue.add(jsonArrayRequest);
        }
    }

    @Override
    public void findRecipesByComplexity(int complexity, VolleyCallback callback){
        String url = BASE_URL + "/recipe/complexity/" + complexity;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_BY_COMPLEXITY_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_BY_COMPLEXITY_LIST.add(recipe);
                            }

                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findRecipesByKcalBetween(int kcalBottom, int kcalTop, VolleyCallback callback){
        String url = BASE_URL + "/recipe/kcal/" + kcalBottom + "/"+ kcalTop;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_BY_KCAL_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_BY_KCAL_LIST.add(recipe);
                            }

                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findRecipesByProteinsBetween(int proteinsBottom, int proteinsTop, VolleyCallback callback){
        String url = BASE_URL + "/recipe/proteins/" + proteinsBottom + "/"+ proteinsTop;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_BY_PROTEINS_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_BY_PROTEINS_LIST.add(recipe);
                            }

                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findRecipesByFatsBetween(int fatsBottom, int fatsTop, VolleyCallback callback){
        String url = BASE_URL + "/recipe/fats/" + fatsBottom + "/"+ fatsTop;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_BY_FATS_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_BY_FATS_LIST.add(recipe);
                            }

                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findRecipesByCarbohydratesBetween(int carbohydratesBottom, int carbohydratesTop, VolleyCallback callback){
        String url = BASE_URL + "/recipe/carbohydrates/" + carbohydratesBottom + "/"+ carbohydratesTop;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_BY_CARBOHYDRATES_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_BY_CARBOHYDRATES_LIST.add(recipe);
                            }

                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findRecipesByTimeBetween(int timeBottom, int timeTop, VolleyCallback callback){
        String url = BASE_URL + "/recipe/time/" + timeBottom + "/"+ timeTop;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_BY_TIME_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_BY_TIME_LIST.add(recipe);
                            }

                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findRecipesByIngredients(List<String> ingredients, VolleyCallback callback) {
        String url = BASE_URL + "/recipe/ingredients/";
        for (int i = 0; i < ingredients.size(); ++i){
            if(i == 0){
                url += ingredients.get(i);
            }
            else {
                url = url + "," + ingredients.get(i);
            }
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_BY_INGREDIENTS_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_BY_INGREDIENTS_LIST.add(recipe);
                            }
                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );
        if(ingredients.size() > 0) {
            requestQueue.add(jsonArrayRequest);
        }
        else {
            NoDb.RECIPE_BY_INGREDIENTS_LIST.clear();
            requestQueue.add(jsonArrayRequest);
        }
    }

    @Override
    public void findRecipesByIngredientsNot(List<String > ingredients, VolleyCallback callback) {
        String url = BASE_URL + "/recipe/ingredients/not/";
        for (int i = 0; i < ingredients.size(); ++i){
            if(i == 0){
                url += ingredients.get(i);
            }
            else {
                url = url + "," + ingredients.get(i);
            }
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_BY_INGREDIENTS_NOT_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_BY_INGREDIENTS_NOT_LIST.add(recipe);
                            }
                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );
        if(ingredients.size() > 0) {
            requestQueue.add(jsonArrayRequest);
        }
        else {
            NoDb.RECIPE_BY_INGREDIENTS_NOT_LIST.clear();
            requestQueue.add(jsonArrayRequest);
        }
    }

    @Override
    public void findRecipesByTags(List<String> tags, VolleyCallback callback) {
        String url = BASE_URL + "/recipe/tags/";
        for (int i = 0; i < tags.size(); ++i){
            if(i == 0){
                url += tags.get(i);
            }
            else {
                url = url + "," + tags.get(i);
            }
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_BY_TAGS_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_BY_TAGS_LIST.add(recipe);
                            }
                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );
        if(tags.size() > 0) {
            requestQueue.add(jsonArrayRequest);
        }
        else {
            NoDb.RECIPE_BY_TAGS_LIST.clear();
            requestQueue.add(jsonArrayRequest);
        }


    }

    @Override
    public void findRecipesByTagsNot(List<String> tags, VolleyCallback callback) {
        String url = BASE_URL + "/recipe/tags/not/";
        for (int i = 0; i < tags.size(); ++i){
            if(i == 0){
                url += tags.get(i);
            }
            else {
                url = url + "," + tags.get(i);
            }
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_BY_TAGS_NOT_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Recipe recipe = RecipeMapper.recipeFromJson(jsonObject);
                                NoDb.RECIPE_BY_TAGS_NOT_LIST.add(recipe);
                            }
                            callback.onSuccess(null);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );
        if(tags.size() > 0) {
            requestQueue.add(jsonArrayRequest);
        }
        else {
            NoDb.RECIPE_BY_TAGS_NOT_LIST.clear();
            requestQueue.add(jsonArrayRequest);
        }
    }

    @Override
    public void findUserByEmail(String email, VolleyCallback callback){

        String url = BASE_URL + "/user/email/" + email;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);


    }

    @Override
    public void fillPost(int postType, VolleyCallback callback) {
        String url = BASE_URL + "/post";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.POST_LIST.clear();
                        if(postType == 1) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    Post post = PostMapper.postFromJson(jsonObject);
                                    if(!post.getPicture().contains("videos")) {
                                        NoDb.POST_LIST.add(post);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    Post post = PostMapper.postFromJson(jsonObject);
                                    if(post.getPicture().contains("videos")) {
                                        NoDb.POST_LIST.add(post);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Collections.reverse(NoDb.POST_LIST);
                        callback.onSuccess(null);
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findPostByAuthor(int authorId, VolleyCallback callback) {

        String url = BASE_URL + "/post/author/" + authorId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.POST_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Post post = PostMapper.postFromJson(jsonObject);
                                NoDb.POST_LIST.add(post);

                                callback.onSuccess(null);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void fillPicture() {
        String url = BASE_URL + "/picture";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.PICTURE_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Picture picture = PictureMapper.pictureFromJson(jsonObject);
                                NoDb.PICTURE_LIST.add(picture);

                                try {
                                    ((MainActivity) context).updateRecipeAdapter();
                                } catch(NullPointerException e){

                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void fillMeals(){
        String url = BASE_URL + "/meals";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.MEALS_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Meal meal = MealMapper.mealFromJson(jsonObject);
                                NoDb.MEALS_LIST.add(meal);
                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findMealsByUser(int userId, VolleyCallback callback){
        String url = BASE_URL + "/meals/user/" + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.MEALS_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Meal meal = MealMapper.mealFromJson(jsonObject);
                                NoDb.MEALS_LIST.add(meal);
                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findMealsByUserAndDay(int userId, int dayId, VolleyCallback callback){
        String url = BASE_URL + "/meals/user/" + userId + "/day/" + dayId;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.MEALS_LIST.clear();
                        try {
                            if(response.toString().equals("[]")){
                                callback.onError(null);
                            }
                            else {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    Meal meal = MealMapper.mealFromJson(jsonObject);
                                    NoDb.MEALS_LIST.add(meal);

                                }
                            }

                            callback.onSuccess(null);

                        }
                        catch (JSONException e) {

                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void fillDays(){
        String url = BASE_URL + "/days";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.DAY_LIST.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Day day = DayMapper.dayFromJson(jsonObject);
                                NoDb.DAY_LIST.add(day);
                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findDaysByUserAndDay(int userId, int day, VolleyCallback callback){
        String url = BASE_URL + "/days/user/" + userId + "/day/" + day;
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void findDaysByUser(int userId, VolleyCallback callback) {
        String url = BASE_URL + "/days/user/" + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.DAYS_BY_USER_LIST.clear(); 
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                NoDb.DAYS_BY_USER_LIST.add(DayMapper.dayFromJson(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onSuccess(null);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findDayById(int id, VolleyCallback callback) {
        String url = BASE_URL + "/days/" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void findPicturesByRecipe(int id, VolleyCallback callback){
        String url = BASE_URL + "/picture/recipe/" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.PICTURE_LIST.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                NoDb.PICTURE_LIST.add(PictureMapper.pictureFromJson(jsonObject));
                                try {
                                    ((MainActivity) context).updateCurrentRecipeAdapter();
                                }
                                catch (NullPointerException e){}

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onSuccess(null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);


    }

    public void findPreviews(VolleyCallback callback){
        String url = BASE_URL + "/picture/previews";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.PICTURE_LIST.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Picture picture = PictureMapper.pictureFromJson(response.getJSONObject(i));
                                NoDb.PICTURE_LIST.add(picture);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onSuccess(null);
                    }
                },
                errorListener
        );

        requestQueue.add(jsonArrayRequest);

    }

    public void findPreviewsByRecipeAuthor(VolleyCallback callback, int userId){
        String url = BASE_URL + "/picture/previews/" + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.PICTURE_LIST.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Picture picture = PictureMapper.pictureFromJson(response.getJSONObject(i));
                                NoDb.PICTURE_LIST.add(picture);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onSuccess(null);
                    }
                },
                errorListener
        );

        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public void findRecipeCommentsByRecipe(int recipeId, VolleyCallback callback) {
        String url = BASE_URL + "/recipeComment/recipe/" + recipeId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.RECIPE_COMMENTS_LIST.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                RecipeComment recipeComment = RecipeCommentMapper.recipeCommentFromJson(jsonObject);
                                NoDb.RECIPE_COMMENTS_LIST.add(recipeComment);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onSuccess(null);
                    }
                },
                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findSubscriptionsByLeaderId(int leaderId, VolleyCallback callback) {
        String url = BASE_URL + "/subscription/leader/" + leaderId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("num", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.onSuccess(jsonObject);
                    }
                },
                errorListener
        );

        requestQueue.add(stringRequest);
    }

    @Override
    public void findSubscriptionByFollowerId(int followerId, VolleyCallback callback) {
        String url = BASE_URL + "/subscription/follower/" + followerId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); ++i) {
                            try {
                                NoDb.SUBSCRIPTION_LIST.add(SubscriptionMapper.subscriptionFromJson(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onSuccess(null);
                    }
                },
                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void checkSubscription(int leaderId, int followerId, VolleyCallback callback) {
        String url = BASE_URL + "/subscription/check/" + leaderId + "/" + followerId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("res", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.onSuccess(jsonObject);
                    }
                },
                errorListener
        );

        requestQueue.add(stringRequest);
    }

    @Override
    public void findMessagesBySenderAndReceiver(int senderId, int receiverId, VolleyCallback callback) {
        String url = BASE_URL + "/message/" + senderId + "/" + receiverId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.MESSAGE_LIST.clear();
                        for(int i = 0; i < response.length(); ++i) {
                            try {
                                NoDb.MESSAGE_LIST.add(MessageMapper.messageFromJson(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onSuccess(null);
                    }
                },
                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void findMessagesByUser(int userId, VolleyCallback callback) {
        String url = BASE_URL + "/message/user/" + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        NoDb.MESSAGES_FEED_LIST.clear();
                        for(int i = 0; i < response.length(); ++i) {
                            try {
                                NoDb.MESSAGES_FEED_LIST.add(MessageMapper.messageFromJson(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        List<Message> messagesToDelete = new ArrayList<>();
                        for (int i = NoDb.MESSAGES_FEED_LIST.size() - 1; i >= 0; --i) {
                            Message message = NoDb.MESSAGES_FEED_LIST.get(i);
                            for (int j = i - 1; j >= 0; --j) {
                                if((NoDb.MESSAGES_FEED_LIST.get(j).getSender().getId() == message.getSender().getId() && NoDb.MESSAGES_FEED_LIST.get(j).getReceiver().getId() == message.getReceiver().getId()) ||
                                        (NoDb.MESSAGES_FEED_LIST.get(j).getSender().getId() == message.getReceiver().getId() && NoDb.MESSAGES_FEED_LIST.get(j).getReceiver().getId() == message.getSender().getId())){
                                    messagesToDelete.add(NoDb.MESSAGES_FEED_LIST.get(j));
                                }
                            }

                        }

                        NoDb.MESSAGES_FEED_LIST.removeAll(messagesToDelete);
                        callback.onSuccess(null);
                    }
                },
                errorListener
        );

        requestQueue.add(jsonArrayRequest);
    }

    public void findRecipeLikesByRecipe(int recipeId, VolleyCallback callback) {
        String url = BASE_URL + "/recipeLikes/" + recipeId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("num", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.onSuccess(jsonObject);
                    }
                },
                errorListener
        );

        requestQueue.add(stringRequest);
    }

    @Override
    public void findPostLikesByPost(int postId, VolleyCallback callback) {
        String url = BASE_URL + "/postLikes/" + postId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("num", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.onSuccess(jsonObject);
                    }
                },
                errorListener
        );

        requestQueue.add(stringRequest);
    }

    public void checkRecipeLikeByUser(int recipeId, int userId, VolleyCallback callback) {
        String url = BASE_URL + "/recipeLikes/checkLike?recipeId=" + recipeId + "&userId=" + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("res", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.onSuccess(jsonObject);
                    }
                },
                errorListener
        );

        requestQueue.add(stringRequest);
    }

    public void checkPostLikeByUser(int postId, int userId, VolleyCallback callback) {
        String url = BASE_URL + "/postLikes/checkLike?postId=" + postId + "&userId=" + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("res", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.onSuccess(jsonObject);
                    }
                },
                errorListener
        );

        requestQueue.add(stringRequest);
    }

    @Override
    public void addUser(User user) {
        String url = BASE_URL + "/user";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        fillUser();
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("name", user.getName());
                params.put("email", user.getEmail());
                params.put("password", user.getPassword());
                params.put("status", user.getStatus());
                params.put("profilePic", user.getProfilePic());
                params.put("kcal", String.valueOf(user.getKcal()));
                params.put("proteins", String.valueOf(user.getProteins()));
                params.put("fats", String.valueOf(user.getFats()));
                params.put("carbohydrates", String.valueOf(user.getCarbohydrates()));
                params.put("registrationDate", String.valueOf(user.getRegistrationDate()));

                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

    @Override
    public void updateUser(int id,
                           String name,
                           String email,
                           String password,
                           String status,
                           String profilePic,
                           int kcal,
                           int proteins,
                           int fats,
                           int carbohydrates,
                           long registrationDate) {
        String url = BASE_URL + "/user/" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        fillUser();
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("status", status);
                params.put("profilePic", profilePic);
                params.put("kcal", String.valueOf(kcal));
                params.put("proteins", String.valueOf(proteins));
                params.put("fats", String.valueOf(fats));
                params.put("carbohydrates", String.valueOf(carbohydrates));
                params.put("registrationDate", String.valueOf(registrationDate));

                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

    @Override
    public void updateUser(int id,
                           String name,
                           String email,
                           String password,
                           String status,
                           String profilePic,
                           int kcal,
                           int proteins,
                           int fats,
                           int carbohydrates,
                           long registrationDate,
                           VolleyCallback callback) {
        String url = BASE_URL + "/user/" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        fillUser();
                        callback.onSuccess(null);
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("status", status);
                params.put("profilePic", profilePic);
                params.put("kcal", String.valueOf(kcal));
                params.put("proteins", String.valueOf(proteins));
                params.put("fats", String.valueOf(fats));
                params.put("carbohydrates", String.valueOf(carbohydrates));
                params.put("registrationDate", String.valueOf(registrationDate));

                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

    @Override
    public void addRecipe(Recipe recipe, VolleyCallback callback) {
        String url = BASE_URL + "/recipe";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                        findRecipeById(Integer.parseInt(jsonObject.get("id").getAsString()), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                callback.onSuccess(response);
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });

                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("name", recipe.getName());
                params.put("authorId", String.valueOf(recipe.getAuthor().getId()));
                params.put("ingredients", recipe.getIngredients());
                params.put("guide", recipe.getGuide());
                params.put("recommendations", recipe.getReccomendations());
                params.put("time", String.valueOf(recipe.getTime()));
                params.put("kcal", String.valueOf(recipe.getKcal()));
                params.put("proteins", String.valueOf(recipe.getProteins()));
                params.put("fats", String.valueOf(recipe.getFats()));
                params.put("carbohydrates", String.valueOf(recipe.getCarbohydrates()));
                params.put("sugar", String.valueOf(recipe.getSugar()));
                params.put("complexity", String.valueOf(recipe.getComplexity()));
                params.put("tags", recipe.getTags());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void addPost(Post post, VolleyCallback callback) {
        String url = BASE_URL + "/post";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(null);
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("authorId", String.valueOf(post.getAuthor().getId()));
                params.put("text", post.getText());
                params.put("picture", post.getPicture());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void addPicture(Picture picture) {
        String url = BASE_URL + "/picture";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        fillPicture();
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("link", picture.getLink());
                params.put("recipeId", String.valueOf(picture.getRecipe().getId()));
                params.put("number", String.valueOf(picture.getNumber()));

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void addMeal(Meal meal) {
        String url = BASE_URL + "/meals";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        fillMeals();
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("userId", String.valueOf(meal.getUser().getId()));
                params.put("recipeId", String.valueOf(meal.getRecipe().getId()));
                params.put("dayId", String.valueOf(meal.getDay().getId()));

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void addRecipeLike(RecipeLike recipeLike, VolleyCallback callback) {
        String url = BASE_URL + "/recipeLikes";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        findRecipeLikesByRecipe(recipeLike.getRecipe().getId(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                callback.onSuccess(response);
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("recipeId", String.valueOf(recipeLike.getRecipe().getId()));
                params.put("likerId", String.valueOf(recipeLike.getLiker().getId()));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void addPostLike(PostLike postLike, VolleyCallback callback) {
        String url = BASE_URL + "/postLikes";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        findPostLikesByPost(postLike.getPost().getId(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                callback.onSuccess(response);
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("postId", String.valueOf(postLike.getPost().getId()));
                params.put("likerId", String.valueOf(postLike.getLiker().getId()));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void deleteMeal(Meal meal, VolleyCallback callback) {
        String url = BASE_URL + "/meals/" + meal.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fillMeals();
                        callback.onSuccess(null);
                    }
                },
                errorListener);

        requestQueue.add(stringRequest);
    }

    @Override
    public void addDay(Day day, VolleyCallback callback) {
        String url = BASE_URL + "/days";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        fillDays();
                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                        findDayById(Integer.parseInt(jsonObject.get("id").getAsString()), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                callback.onSuccess(response);
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("userId", String.valueOf(day.getUser().getId()));
                params.put("day", String.valueOf(day.getDay()));
                params.put("kcal", String.valueOf(day.getKcal()));
                params.put("proteins", String.valueOf(day.getProteins()));
                params.put("fats", String.valueOf(day.getFats()));
                params.put("carbohydrates", String.valueOf(day.getCarbohydrates()));
                params.put("isSuccessful", String.valueOf(day.isSuccessful()));


                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void updateDay(int id,
                          int userId,
                          int day,
                          int kcal,
                          int proteins,
                          int fats,
                          int carbohydrates,
                          boolean isSuccessful,
                          VolleyCallback callback) {
        String url = BASE_URL + "/days/" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        findDayById(id, new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                callback.onSuccess(response);
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("id", String.valueOf(id));
                params.put("userId", String.valueOf(userId));
                params.put("day", String.valueOf(day));
                params.put("kcal", String.valueOf(kcal));
                params.put("proteins", String.valueOf(proteins));
                params.put("fats", String.valueOf(fats));
                params.put("carbohydrates", String.valueOf(carbohydrates));
                params.put("isSuccessful", String.valueOf(isSuccessful));

                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

    @Override
    public void addRecipeComment(RecipeComment recipeComment, VolleyCallback callback) {
        String url = BASE_URL + "/recipeComment";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        findRecipeCommentsByRecipe(recipeComment.getRecipe().getId(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                callback.onSuccess(null);
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("authorId", String.valueOf(recipeComment.getAuthor().getId()));
                params.put("recipeId", String.valueOf(recipeComment.getRecipe().getId()));
                params.put("text", recipeComment.getText());
                params.put("likes", String.valueOf(recipeComment.getLikes()));

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void  addSubscription(Subscription subscription, VolleyCallback callback) {
        String url = BASE_URL + "/subscription";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        findSubscriptionsByLeaderId(subscription.getLeader().getId(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                callback.onSuccess(response);
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("leaderId", String.valueOf(subscription.getLeader().getId()));
                params.put("followerId", String.valueOf(subscription.getFollower().getId()));

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void addMessage(Message message, VolleyCallback callback) {
        String url = BASE_URL + "/message";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        findMessagesBySenderAndReceiver(message.getSender().getId(), message.getReceiver().getId(), new VolleyCallback() {
                        @Override
                            public void onSuccess(JSONObject response) {
                                callback.onSuccess(response);
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });
                    }
                },
                errorListener) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("senderId", String.valueOf(message.getSender().getId()));
                params.put("receiverId", String.valueOf(message.getReceiver().getId()));
                params.put("picture", String.valueOf(message.getPicture()));
                params.put("text", message.getText());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void deleteRecipe(int id){

    }

    @Override
    public void deleteRecipeLike(Recipe recipe, User user, VolleyCallback callback) {
        String url = BASE_URL + "/recipeLikes/" + recipe.getId() + "/" + user.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        findRecipeLikesByRecipe(recipe.getId(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                               callback.onSuccess(response);
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });
                    }
                },
                errorListener);

        requestQueue.add(stringRequest);
    }

    @Override
    public void deletePostLike(Post post, User user, VolleyCallback callback) {
        String url = BASE_URL + "/postLikes/" + post.getId() + "/" + user.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        findPostLikesByPost(post.getId(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                callback.onSuccess(response);
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });
                    }
                },
                errorListener);

        requestQueue.add(stringRequest);
    }

    @Override
    public void deleteSubscription(int leaderId, int followerId, VolleyCallback callback) {
        String url = BASE_URL + "/subscription/" + leaderId + "/" + followerId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        findSubscriptionsByLeaderId(leaderId, new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                callback.onSuccess(response);
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });
                    }
                },
                errorListener);

        requestQueue.add(stringRequest);
    }
}
