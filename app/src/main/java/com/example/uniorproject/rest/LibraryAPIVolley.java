package com.example.uniorproject.rest;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.PostMapper;
import com.example.uniorproject.domain.mapper.RecipeMapper;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.fragment.FeedFragment;
import com.example.uniorproject.noDb.NoDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LibraryAPIVolley implements AppAPI{

    public static final String API = "API";
    private final Context context;
    private static final String BASE_URL = "https://f27a-109-94-26-0.eu.ngrok.io";
    private Response.ErrorListener errorListener;



    public LibraryAPIVolley(Context context) {
        this.context = context;
        errorListener = error -> Log.d(API, error.toString());
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

                                ((MainActivity)context).updateRecipeAdapter();
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

                                ((MainActivity) context).updatePostAdapter();

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
    public void addRecipe(Recipe recipe) {
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
    public void deleteRecipe(int id){

    }
}
