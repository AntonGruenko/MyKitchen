package com.example.uniorproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.CommentAdapter;
import com.example.uniorproject.adapter.RecipeDataAdapter;
import com.example.uniorproject.database.Product;
import com.example.uniorproject.database.ShoppingListDatabase;
import com.example.uniorproject.databinding.FragmentRecipeBinding;
import com.example.uniorproject.domain.Day;
import com.example.uniorproject.domain.Meal;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.RecipeComment;
import com.example.uniorproject.domain.RecipeLike;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.DayMapper;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class RecipeFragment extends Fragment {
    private int recipeId;
    private int minutes, hours;
    private List<String> ingredients, guide, tags;
    private RecipeDataAdapter ingredientsAdapter, guideAdapter, tagsAdapter;
    private ShoppingListDatabase database;
    private User currentUser;
    private SharedPreferences sharedPreferences;
    private int likeId;
    private CommentAdapter recipeCommentAdapter;
    private String newCommentContent;
    private RecipeComment newComment;

    public RecipeFragment(int recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRecipeBinding binding = FragmentRecipeBinding.inflate(inflater);
        View view = binding.getRoot();
        Recipe recipe = NoDb.RECIPE_LIST.get(recipeId);
        sharedPreferences = getContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        new VolleyAPI(getContext()).findUserByEmail(sharedPreferences.getString("userEmail", ""), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                currentUser = UserMapper.userFromJson(response);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

        hours = recipe.getTime()/60;
        minutes = recipe.getTime() - hours * 60;

        binding.recipeTitle.setText(recipe.getName());
        binding.recipeAuthorName.setText(String.format("Автор: %s", recipe.getAuthor().getName()));
        binding.recipeTime.setText(String.format("%d:%d", hours, minutes));
        binding.recipeKcal.setText(String.valueOf(recipe.getKcal()));
        binding.recipeProteins.setText(String.format("Белки на 100г: %d", recipe.getProteins()));
        binding.recipeFats.setText(String.format("Жиры на 100г: %d", recipe.getFats()));
        binding.recipeCarbohydrates.setText(String.format("Углеводы на 100г: %d", recipe.getCarbohydrates()));
        binding.recipeSugar.setText(String.format("Сахар на 100г: %d", recipe.getSugar()));
        binding.recipeRecommendations.setText("Рекомендации: " + recipe.getReccomendations());

        if(recipe.getComplexity() == 1){
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage1);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage2);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage3);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage4);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage5);
        }
        else if(recipe.getComplexity() == 2){
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage1);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage2);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage3);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage4);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage5);
        }
        else if(recipe.getComplexity() == 3){
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage1);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage2);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage3);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage4);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage5);
        }

        else if(recipe.getComplexity() == 4){
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage1);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage2);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage3);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage4);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage5);
        }

        else if(recipe.getComplexity() == 5){
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage1);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage2);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage3);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage4);
            Picasso.with(getContext()).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage5);
        }

        ingredients = convertStringToList(recipe.getIngredients());
        guide = convertStringToList(recipe.getGuide());
        tags = convertStringToList(recipe.getTags());

        ingredientsAdapter = new RecipeDataAdapter(getActivity(), ingredients, 1);
        binding.recipeIngredients.setAdapter(ingredientsAdapter);
        binding.recipeIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));

        guideAdapter = new RecipeDataAdapter(getActivity(), guide,2, NoDb.PICTURE_LIST);
        binding.recipeGuide.setAdapter(guideAdapter);
        binding.recipeGuide.setLayoutManager(new LinearLayoutManager(getActivity()));

        tagsAdapter = new RecipeDataAdapter(getActivity(), tags, 1);
        binding.recipeTags.setAdapter(tagsAdapter);
        binding.recipeTags.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        new VolleyAPI(getContext()).findRecipeLikesByRecipe(recipe.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    binding.recipeLikes.setText(response.getString("num"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i < NoDb.RECIPE_LIKES_LIST.size(); i++) {
                    if (NoDb.RECIPE_LIKES_LIST.get(i).getLiker().getId() == currentUser .getId()) {
                        binding.recipeLikes.setChecked(true);
                        likeId = i;
                    }
                }

                binding.recipeLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.recipeLikes.setClickable(false);
                        new VolleyAPI(getContext()).checkRecipeLikeByUser(recipe.getId(), currentUser.getId(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    boolean res = response.getBoolean("res");
                                    if(res){
                                        new VolleyAPI(getContext()).deleteRecipeLike(recipe, currentUser, new VolleyCallback() {
                                            @Override
                                            public void onSuccess(JSONObject response) {
                                                binding.recipeLikes.setClickable(true);
                                                binding.recipeLikes.setChecked(false);
                                                try {
                                                    binding.recipeLikes.setText(response.getString("num"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(@Nullable VolleyError error) {

                                            }
                                        });
                                    }
                                    else {
                                        new VolleyAPI(getContext()).addRecipeLike(new RecipeLike(recipe, currentUser), new VolleyCallback() {
                                            @Override
                                            public void onSuccess(JSONObject response) {
                                                binding.recipeLikes.setClickable(true);
                                                binding.recipeLikes.setChecked(true);
                                                try {
                                                    binding.recipeLikes.setText(String.valueOf(response.getInt("num")));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(@Nullable VolleyError error) {

                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });

                    }
                });
            }

            @Override
            public void onError(VolleyError error) {
                binding.recipeLikes.setText(String.valueOf(0));
                binding.recipeLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.recipeLikes.setClickable(false);
                        new VolleyAPI(getContext()).checkRecipeLikeByUser(recipe.getId(), currentUser.getId(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    boolean res = response.getBoolean("res");
                                    if(res){
                                        new VolleyAPI(getContext()).deleteRecipeLike(recipe, currentUser, new VolleyCallback() {
                                            @Override
                                            public void onSuccess(JSONObject response) {
                                                binding.recipeLikes.setClickable(true);
                                                binding.recipeLikes.setChecked(false);
                                                try {
                                                    binding.recipeLikes.setText(response.getString("num"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(@Nullable VolleyError error) {

                                            }
                                        });
                                    }
                                    else {
                                        new VolleyAPI(getContext()).addRecipeLike(new RecipeLike(recipe, currentUser), new VolleyCallback() {
                                            @Override
                                            public void onSuccess(JSONObject response) {
                                                binding.recipeLikes.setClickable(true);
                                                binding.recipeLikes.setChecked(true);
                                                try {
                                                    binding.recipeLikes.setText(String.valueOf(response.getInt("num")));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(@Nullable VolleyError error) {

                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });

                    }
                });
            }
        });

        new VolleyAPI(getContext()).findRecipeCommentsByRecipe(recipe.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                recipeCommentAdapter = new CommentAdapter(getContext(), NoDb.RECIPE_COMMENTS_LIST);
                binding.recipeComments.setAdapter(recipeCommentAdapter);
                binding.recipeComments.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        });

        binding.copyIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < ingredients.size(); i++) {
                    insertToShoppingList(ingredients.get(i));
                }
            }
        });

        binding.addToMealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new VolleyAPI(getContext()).findUserByEmail(sharedPreferences.getString("userEmail", ""), new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        User user = UserMapper.userFromJson(response);
                        Calendar currentCalendar = Calendar.getInstance();
                        long registrationPeriod = currentCalendar.getTimeInMillis() - user.getRegistrationDate();
                        int daysFromRegistration;
                        Calendar comparisonCalendar = Calendar.getInstance();
                        comparisonCalendar.setTimeInMillis(user.getRegistrationDate());

                        if(comparisonCalendar.get(Calendar.DAY_OF_YEAR) < currentCalendar.get(Calendar.DAY_OF_YEAR)){
                            daysFromRegistration = (int) (registrationPeriod / 86400000) + 1;
                        }
                        else {
                            daysFromRegistration = (int) registrationPeriod / 86400000;
                        }
                        new VolleyAPI(getContext()).findDaysByUserAndDay(user.getId(), daysFromRegistration, new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                if (!response.toString().equals("[]")){
                                    Day day = DayMapper.dayFromJson(response);
                                    new VolleyAPI(getContext()).updateDay(
                                            day.getId(), user.getId(),
                                            day.getDay(),
                                            day.getKcal() + recipe.getKcal(),
                                            day.getProteins() + recipe.getProteins(),
                                            day.getFats() + recipe.getFats(),
                                            day.getCarbohydrates() + recipe.getCarbohydrates(),
                                            day.isSuccessful(),
                                            new VolleyCallback() {
                                                @Override
                                                public void onSuccess(JSONObject response) {

                                                }

                                                @Override
                                                public void onError(@Nullable VolleyError error) {

                                                }
                                            });

                                    Meal meal = new Meal(currentUser, recipe, day);
                                    new VolleyAPI(getContext()).addMeal(meal);
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                if(error.networkResponse.statusCode == 500){
                                    new VolleyAPI(getContext()).addDay(new Day(
                                            user,
                                            daysFromRegistration,
                                            recipe.getKcal(),
                                            recipe.getProteins(),
                                            recipe.getFats(),
                                            recipe.getCarbohydrates(),
                                            false));
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });

            }
        });

        binding.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCommentContent = binding.commentEditText.getText().toString();
                binding.commentEditText.setText("");
                if(!newCommentContent.isEmpty()){
                    newComment = new RecipeComment(currentUser, recipe, newCommentContent, 0);
                    new VolleyAPI(getContext()).addRecipeComment(newComment, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            recipeCommentAdapter = new CommentAdapter(getContext(), NoDb.RECIPE_COMMENTS_LIST);
                            binding.recipeComments.setAdapter(recipeCommentAdapter);
                        }

                        @Override
                        public void onError(@Nullable VolleyError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "Введите комментарий!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.recipeAuthorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AnotherProfileFragment(recipe.getAuthor().getId()), "RecipeFeed")
                        .commit();
            }
        });

        return view;
    }
    


    private List<String> convertStringToList(String data){
        List<String> elements;
        elements = Arrays.asList(data.split(";;"));
        return elements;
    }

    public void updateAdapter(){
        guideAdapter.notifyDataSetChanged();
    }

    private void insertToShoppingList(String title){
        database = ShoppingListDatabase.getInstance(getContext());

        Product product = new Product();
        product.setTitle(title);

        database.productDao().insertProduct(product);
    }

}