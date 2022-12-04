package com.example.uniorproject.rest;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

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
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.PictureMapper;
import com.example.uniorproject.domain.mapper.PostMapper;
import com.example.uniorproject.domain.mapper.RecipeMapper;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.noDb.NoDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyAPI implements AppAPI{

    public static final String API = "API";
    private final Context context;
    public static final String BASE_URL = "https://98c1-176-109-46-57.eu.ngrok.io";
    private Response.ErrorListener errorListener;

    public VolleyAPI(Context context) {
        this.context = context;
        errorListener = error -> {
            Log.d(API, error.toString());

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
                                Log.d(API, user.getName());
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
    public void fillRecipe() {
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

                                Log.d(API, recipe.getName());

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
    public void fillPost() {
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
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Post post = PostMapper.postFromJson(jsonObject);
                                NoDb.POST_LIST.add(post);

                                Log.d(API, post.getText());

                                try {
                                    ((MainActivity) context).updatePostAdapter();
                                }
                                catch (NullPointerException e){

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
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                callback.onSuccess(jsonObject);
                                try {
                                    ((MainActivity) context).updateCurrentRecipeAdapter();
                                }
                                catch (NullPointerException e){}

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                        Log.d(API, response);
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
                        fillRecipe();
                        Log.d(API, response);
                        callback.onSuccess(null);
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
                params.put("likes", String.valueOf(recipe.getLikes()));
                params.put("complexity", String.valueOf(recipe.getComplexity()));
                params.put("tags", recipe.getTags());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void addPost(Post post) {
        String url = BASE_URL + "/post";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        fillPost();
                        Log.d(API, response);
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
                params.put("likes", String.valueOf(post.getLikes()));

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
    public void deleteRecipe(int id){

    }
}
