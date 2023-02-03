package com.example.uniorproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import com.example.uniorproject.MainActivity;
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
    private final int recipeId;
    private int minutes, hours;
    private List<String> ingredients, guide, tags;
    private RecipeDataAdapter ingredientsAdapter, guideAdapter, tagsAdapter;
    private ShoppingListDatabase database;
    private User currentUser;
    private Recipe recipe;
    private SharedPreferences sharedPreferences;
    private int likeId;
    private CommentAdapter recipeCommentAdapter;
    private String newCommentContent;
    private RecipeComment newComment;
    private Context context;

    public static final short FROM_FEED = 1;
    public static final short FROM_PROFILE = 2;
    public static final short FROM_ANOTHER_PROFILE = 3;
    public static final short FROM_SEARCH = 4;

    private int fromFragment;
    private short userId;

    public RecipeFragment(int recipeId, short fromFragment) {
        this.recipeId = recipeId;
        this.fromFragment = fromFragment;
    }

    public RecipeFragment(int recipeId, int fromFragment, short userId) {
        this.recipeId = recipeId;
        this.fromFragment = fromFragment;
        this.userId = userId;
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
        context = getContext();
        for (Recipe i : NoDb.RECIPE_LIST) {
            if(i.getId() == recipeId){
                recipe = i;
                break;
            }
        }
        sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        new VolleyAPI(context).findPicturesByRecipe(recipe.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                guideAdapter = new RecipeDataAdapter(context, guide,2, NoDb.PICTURE_LIST);
                binding.recipeGuide.setAdapter(guideAdapter);
                binding.recipeGuide.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        });
        new VolleyAPI(context).findUserByEmail(sharedPreferences.getString("userEmail", ""), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                currentUser = UserMapper.userFromJson(response);
                new VolleyAPI(context).checkRecipeLikeByUser(recipe.getId(), currentUser.getId(), new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            boolean res = response.getBoolean("res");
                            if (res) {
                                binding.recipeLikes.setChecked(true);
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
        binding.recipeProteins.setText(String.format("Белки: %d", recipe.getProteins()));
        binding.recipeFats.setText(String.format("Жиры: %d", recipe.getFats()));
        binding.recipeCarbohydrates.setText(String.format("Углеводы: %d", recipe.getCarbohydrates()));
        binding.recipeSugar.setText(String.format("Сахар: %d", recipe.getSugar()));
        binding.recipeRecommendations.setText("Рекомендации: " + recipe.getReccomendations());

        if(recipe.getComplexity() == 1){
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage1);
            Picasso.with(context).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage2);
            Picasso.with(context).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage3);
            Picasso.with(context).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage4);
            Picasso.with(context).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage5);
        }
        else if(recipe.getComplexity() == 2){
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage1);
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage2);
            Picasso.with(context).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage3);
            Picasso.with(context).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage4);
            Picasso.with(context).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage5);
        }
        else if(recipe.getComplexity() == 3){
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage1);
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage2);
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage3);
            Picasso.with(context).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage4);
            Picasso.with(context).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage5);
        }

        else if(recipe.getComplexity() == 4){
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage1);
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage2);
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage3);
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage4);
            Picasso.with(context).load(R.drawable.chef_hat_32).into(binding.recipeComplexityImage5);
        }

        else if(recipe.getComplexity() == 5){
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage1);
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage2);
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage3);
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage4);
            Picasso.with(context).load(R.drawable.chef_hat_32_filled).into(binding.recipeComplexityImage5);
        }

        ingredients = convertStringToList(recipe.getIngredients());
        guide = convertStringToList(recipe.getGuide());
        tags = convertStringToList(recipe.getTags());

        ingredientsAdapter = new RecipeDataAdapter(context, ingredients, 1);
        binding.recipeIngredients.setAdapter(ingredientsAdapter);
        binding.recipeIngredients.setLayoutManager(new LinearLayoutManager(context));

        tagsAdapter = new RecipeDataAdapter(context, tags, 1);
        binding.recipeTags.setAdapter(tagsAdapter);
        binding.recipeTags.setLayoutManager(new GridLayoutManager(context, 3));


        new VolleyAPI(context).findRecipeLikesByRecipe(recipe.getId(), new VolleyCallback() {
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
                        new VolleyAPI(context).checkRecipeLikeByUser(recipe.getId(), currentUser.getId(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    boolean res = response.getBoolean("res");
                                    if(res){
                                        new VolleyAPI(context).deleteRecipeLike(recipe, currentUser, new VolleyCallback() {
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
                                        new VolleyAPI(context).addRecipeLike(new RecipeLike(recipe, currentUser), new VolleyCallback() {
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
                        new VolleyAPI(context).checkRecipeLikeByUser(recipe.getId(), currentUser.getId(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    boolean res = response.getBoolean("res");
                                    if(res){
                                        new VolleyAPI(context).deleteRecipeLike(recipe, currentUser, new VolleyCallback() {
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
                                        new VolleyAPI(context).addRecipeLike(new RecipeLike(recipe, currentUser), new VolleyCallback() {
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

        new VolleyAPI(context).findRecipeCommentsByRecipe(recipe.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                recipeCommentAdapter = new CommentAdapter(context, NoDb.RECIPE_COMMENTS_LIST);
                binding.recipeComments.setAdapter(recipeCommentAdapter);
                binding.recipeComments.setLayoutManager(new LinearLayoutManager(context));
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
                binding.addToMealsButton.setClickable(false);
                new VolleyAPI(context).findUserByEmail(sharedPreferences.getString("userEmail", ""), new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        User user = UserMapper.userFromJson(response);
                        Calendar currentCalendar = Calendar.getInstance();
                        Calendar userCalendar = Calendar.getInstance();
                        userCalendar.setTimeInMillis(user.getRegistrationDate());
                        Calendar comparisonCalendar = Calendar.getInstance();
                        comparisonCalendar.setTimeInMillis(user.getRegistrationDate());

                        int daysFromRegistration = currentCalendar.get(Calendar.DAY_OF_YEAR) - userCalendar.get(Calendar.DAY_OF_YEAR);

                        new VolleyAPI(context).findDaysByUserAndDay(user.getId(), daysFromRegistration, new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                if (!response.toString().equals("[]")){
                                    Day day = DayMapper.dayFromJson(response);
                                    new VolleyAPI(context).updateDay(
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
                                                    binding.addToMealsButton.setClickable(true);
                                                }

                                                @Override
                                                public void onError(@Nullable VolleyError error) {

                                                }
                                            });

                                    Meal meal = new Meal(currentUser, recipe, day);
                                    new VolleyAPI(context).addMeal(meal);
                                }
                            }

                            @Override
                            public void onError(VolleyError error){
                                binding.addToMealsButton.setClickable(true);
                                new VolleyAPI(context).addDay(new Day(
                                            user,
                                            daysFromRegistration,
                                            recipe.getKcal(),
                                            recipe.getProteins(),
                                            recipe.getFats(),
                                            recipe.getCarbohydrates(),
                                            false));
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
                    new VolleyAPI(context).addRecipeComment(newComment, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            recipeCommentAdapter = new CommentAdapter(context, NoDb.RECIPE_COMMENTS_LIST);
                            binding.recipeComments.setAdapter(recipeCommentAdapter);
                        }

                        @Override
                        public void onError(@Nullable VolleyError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(context, "Введите комментарий!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.recipeAuthorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AnotherProfileFragment(recipe.getAuthor().getId(), recipe.getId()), "RecipeFeed")
                        .commit();
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(fromFragment == FROM_FEED) {
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new FeedFragment())
                            .commit();
                }
                else if(fromFragment == FROM_ANOTHER_PROFILE){
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new AnotherProfileFragment(userId, recipe.getId()))
                            .commit();
                }
                else if(fromFragment == FROM_PROFILE){
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new ProfileFragment())
                            .commit();
                }
                else {
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new SearchFragment())
                            .commit();
                }
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