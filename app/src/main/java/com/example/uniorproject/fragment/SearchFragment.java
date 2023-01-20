package com.example.uniorproject.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.android.volley.VolleyError;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.RecipeFeedAdapter;
import com.example.uniorproject.adapter.RecipeSearchAdapter;
import com.example.uniorproject.adapter.UserAdapter;
import com.example.uniorproject.databinding.FragmentSearchBinding;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.mapper.PictureMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private UserAdapter userAdapter;
    private int searchType = 1; //Поиск рецептов - 1, поиск пользователей - 2

    private RecipeSearchAdapter ingredientsAdapter;
    private RecipeSearchAdapter tagsAdapter;
    private RecipeFeedAdapter recipeAdapter;

    private List<String> includedIngredients = new ArrayList<>();
    private List<String> notIncludedIngredients = new ArrayList<>();
    private List<String> includedTags = new ArrayList<>();
    private List<String> notIncludedTags = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater);
        View view = binding.getRoot();

        binding.recipeSearchContainer.setVisibility(View.GONE);

        ingredientsAdapter = new RecipeSearchAdapter(getContext(), NoDb.SEARCH_INGREDIENTS_LIST, RecipeSearchAdapter.INGREDIENTS_SEARCH);
        tagsAdapter = new RecipeSearchAdapter(getContext(), NoDb.SEARCH_TAGS_LIST, RecipeSearchAdapter.TAGS_SEARCH);

        binding.ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.tagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        binding.tagsRecyclerView.setAdapter(tagsAdapter);

        binding.buttonAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoDb.SEARCH_INGREDIENTS_LIST.add(new RecipeSearchAdapter.RecipeSearchText("", false));
                ingredientsAdapter.notifyDataSetChanged();
            }
        });

        binding.buttonAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoDb.SEARCH_TAGS_LIST.add(new RecipeSearchAdapter.RecipeSearchText("", false));
                tagsAdapter.notifyDataSetChanged();
            }
        });

        binding.searchProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.recipeSearchContainer.setVisibility(View.GONE);
                binding.searchSwitch.setVisibility(View.GONE);
                searchType = 2;
            }
        });

        binding.searchRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.searchSwitch.setVisibility(View.VISIBLE);
                if(binding.searchSwitch.isChecked()){
                    binding.recipeSearchContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (searchType){
                    case(1):
                        if(binding.searchSwitch.isChecked()){
                            String query = binding.searchEditText.getText().toString();

                            VolleyCallback notIncludedTagsCallback = new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    joinFoundRecipes();
                                    binding.searchSwitch.setChecked(false);
                                    new VolleyAPI(getContext()).findPreviews(new VolleyCallback() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            recipeAdapter = new RecipeFeedAdapter(getContext(), NoDb.RECIPE_LIST, new RecipeFeedAdapter.OnRecipeClickListener() {
                                                @Override
                                                public void onClick(Recipe recipe, int position) {
                                                    NoDb.PICTURE_LIST.clear();
                                                    new VolleyAPI(getContext()).findPicturesByRecipe(recipe.getId(), new VolleyCallback() {
                                                        @Override
                                                        public void onSuccess(JSONObject response) {
                                                            Picture picture = PictureMapper.pictureFromJson(response);
                                                            NoDb.PICTURE_LIST.add(picture);
                                                        }

                                                        @Override
                                                        public void onError(VolleyError error) {

                                                        }
                                                    });
                                                    getActivity().getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .replace(R.id.fragment_container, new RecipeFragment(position), "RecipeFragment")
                                                            .commit();
                                                }
                                            }, NoDb.PICTURE_LIST);
                                            binding.resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                            binding.resultsRecyclerView.setAdapter(recipeAdapter);

                                        }

                                        @Override
                                        public void onError(@Nullable VolleyError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            };

                            VolleyCallback includedTagsCallback = new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    if(!notIncludedTags.isEmpty()) {
                                        new VolleyAPI(getContext()).findRecipesByTagsNot(notIncludedTags, notIncludedTagsCallback);
                                    }
                                    else {
                                        notIncludedTagsCallback.onSuccess(null);
                                    }
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            };

                            VolleyCallback notIncludedIngredientsCallback = new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    if(!includedTags.isEmpty()) {
                                        new VolleyAPI(getContext()).findRecipesByTags(includedTags, includedTagsCallback);
                                    }
                                    else {
                                        includedTagsCallback.onSuccess(null);
                                    }
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            };

                            VolleyCallback includedIngredientsCallback = new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    if(!notIncludedIngredients.isEmpty()) {
                                        new VolleyAPI(getContext()).findRecipesByIngredientsNot(notIncludedIngredients, notIncludedIngredientsCallback);
                                    }
                                    else{
                                        notIncludedIngredientsCallback.onSuccess(null);
                                    }
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            };
                            
                            VolleyCallback timeCallback = new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    getLists();
                                    if(!includedIngredients.isEmpty()) {
                                        new VolleyAPI(getContext()).findRecipesByIngredients(includedIngredients, includedIngredientsCallback);
                                    }
                                    else {
                                        includedIngredientsCallback.onSuccess(null);
                                    }
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            };

                            VolleyCallback carbohydratesCallback = new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    String bottomMinutes = binding.minutesBottomEditText.getText().toString();
                                    String topMinutes = binding.minutesTopEditText.getText().toString();
                                    String bottomHours = binding.minutesBottomEditText.getText().toString();
                                    String topHours = binding.minutesTopEditText.getText().toString();
                                    int bottomTime = (!bottomHours.isEmpty()? Integer.parseInt(bottomHours): 0) * 60 + (!bottomMinutes.isEmpty()? Integer.parseInt(bottomMinutes): 0);
                                    int topTime = (!topHours.isEmpty()? Integer.parseInt(topHours): 0) * 60 + (!topMinutes.isEmpty()? Integer.parseInt(topMinutes): 0);

                                    if(bottomTime != 0 && topTime != 0){
                                        new VolleyAPI(getContext()).findRecipesByTimeBetween(bottomTime, topTime, timeCallback);
                                    }
                                    else if(bottomTime == 0 && topTime != 0){
                                        new VolleyAPI(getContext()).findRecipesByTimeBetween(0, topTime, timeCallback);
                                    }
                                    else if(bottomTime != 0){
                                        new VolleyAPI(getContext()).findRecipesByTimeBetween(bottomTime, Integer.MAX_VALUE, timeCallback);
                                    }
                                    else{
                                        new VolleyAPI(getContext()).findRecipesByTimeBetween(0, Integer.MAX_VALUE, timeCallback);
                                    }
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            };

                            VolleyCallback fatsCallback = new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    String bottomCarbohydrates = binding.carbohydratesBottomEditText.getText().toString();
                                    String topCarbohydrates = binding.carbohydratesTopEditText.getText().toString();
                                    if(!bottomCarbohydrates.isEmpty() && !topCarbohydrates.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByCarbohydratesBetween(Integer.parseInt(bottomCarbohydrates), Integer.parseInt(topCarbohydrates), carbohydratesCallback);
                                    }
                                    else if(bottomCarbohydrates.isEmpty() && !topCarbohydrates.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByFatsBetween(0, Integer.parseInt(topCarbohydrates), carbohydratesCallback);
                                    }
                                    else if(!bottomCarbohydrates.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByFatsBetween(Integer.parseInt(bottomCarbohydrates), Integer.MAX_VALUE, carbohydratesCallback);
                                    }
                                    else{
                                        new VolleyAPI(getContext()).findRecipesByFatsBetween(0, Integer.MAX_VALUE, carbohydratesCallback);
                                    }
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            };

                            VolleyCallback proteinsCallback = new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    String bottomFats = binding.fatsBottomEditText.getText().toString();
                                    String topFats = binding.fatsTopEditText.getText().toString();
                                    if(!bottomFats.isEmpty() && !topFats.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByFatsBetween(Integer.parseInt(bottomFats), Integer.parseInt(topFats), fatsCallback);
                                    }
                                    else if(bottomFats.isEmpty() && !topFats.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByFatsBetween(0, Integer.parseInt(topFats), fatsCallback);
                                    }
                                    else if(!bottomFats.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByFatsBetween(Integer.parseInt(bottomFats), Integer.MAX_VALUE, fatsCallback);
                                    }
                                    else{
                                        new VolleyAPI(getContext()).findRecipesByFatsBetween(0, Integer.MAX_VALUE, fatsCallback);
                                    }
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            };

                            VolleyCallback kcalCallback = new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    String bottomProteins = binding.proteinsBottomEditText.getText().toString();
                                    String topProteins = binding.proteinsTopEditText.getText().toString();
                                    if(!bottomProteins.isEmpty() && !topProteins.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByProteinsBetween(Integer.parseInt(bottomProteins), Integer.parseInt(topProteins), proteinsCallback);
                                    }
                                    else if(bottomProteins.isEmpty() && !topProteins.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByProteinsBetween(0, Integer.parseInt(topProteins), proteinsCallback);
                                    }
                                    else if(!bottomProteins.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByProteinsBetween(Integer.parseInt(bottomProteins), Integer.MAX_VALUE, proteinsCallback);
                                    }
                                    else{
                                        new VolleyAPI(getContext()).findRecipesByProteinsBetween(0, Integer.MAX_VALUE, proteinsCallback);
                                    }
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            };
                            VolleyCallback complexityCallback = new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    String bottomKcal = binding.kcalBottomEditText.getText().toString();
                                    String topKcal = binding.kcalTopEditText.getText().toString();
                                    if(!bottomKcal.isEmpty() && !topKcal.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByKcalBetween(Integer.parseInt(bottomKcal), Integer.parseInt(topKcal), kcalCallback);
                                    }
                                    else if(bottomKcal.isEmpty() && !topKcal.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByKcalBetween(0, Integer.parseInt(topKcal), kcalCallback);
                                    }
                                    else if(!bottomKcal.isEmpty()){
                                        new VolleyAPI(getContext()).findRecipesByKcalBetween(Integer.parseInt(bottomKcal), Integer.MAX_VALUE, kcalCallback);
                                    }
                                    else{
                                        new VolleyAPI(getContext()).findRecipesByKcalBetween(0, Integer.MAX_VALUE, kcalCallback);
                                    }
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            };

                            VolleyCallback searchCallback = new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    int complexity = (int) binding.complexity.getRating();
                                    new VolleyAPI(getContext()).findRecipesByComplexity(complexity, complexityCallback);
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            };
                            if(!query.isEmpty()) {
                                new VolleyAPI(getContext()).findRecipesByTitle(query, searchCallback);
                            }
                            else {
                                new VolleyAPI(getContext()).fillRecipe(new VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        searchCallback.onSuccess(null);
                                    }

                                    @Override
                                    public void onError(@Nullable VolleyError error) {

                                    }
                                });
                            }
                        }

                        else{
                            String query = binding.searchEditText.getText().toString();
                            new VolleyAPI(getContext()).findRecipesByTitle(query, new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    new VolleyAPI(getContext()).findPreviews(new VolleyCallback() {
                                        @Override
                                        public void onSuccess(JSONObject response) {;
                                            recipeAdapter = new RecipeFeedAdapter(getContext(), NoDb.RECIPE_LIST, new RecipeFeedAdapter.OnRecipeClickListener() {
                                                @Override
                                                public void onClick(Recipe recipe, int position) {
                                                    NoDb.PICTURE_LIST.clear();
                                                    new VolleyAPI(getContext()).findPicturesByRecipe(recipe.getId(), new VolleyCallback() {
                                                        @Override
                                                        public void onSuccess(JSONObject response) {
                                                            Picture picture = PictureMapper.pictureFromJson(response);
                                                            NoDb.PICTURE_LIST.add(picture);
                                                        }

                                                        @Override
                                                        public void onError(VolleyError error) {

                                                        }
                                                    });
                                                    getActivity().getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .replace(R.id.fragment_container, new RecipeFragment(position), "RecipeFragment")
                                                            .commit();
                                                }
                                            }, NoDb.PICTURE_LIST);
                                            binding.resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                            binding.resultsRecyclerView.setAdapter(recipeAdapter);

                                        }

                                        @Override
                                        public void onError(@Nullable VolleyError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onError(@Nullable VolleyError error) {

                                }
                            });
                        }
                        break;
                    case(2):
                        String query = binding.searchEditText.getText().toString();
                        new VolleyAPI(getContext()).findUsersByName(query, new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                UserAdapter.UserClickListener listener = new UserAdapter.UserClickListener(){

                                    @Override
                                    public void onClick(int position) {
                                        getParentFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container, new AnotherProfileFragment(position), "AnotherProfileFragment")
                                                .commit();
                                    }
                                };

                                userAdapter = new UserAdapter(getContext(), NoDb.FOUND_USERS_LIST, listener);
                                binding.resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                binding.resultsRecyclerView.setAdapter(userAdapter);
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });
                }
            }
        });

        binding.searchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    binding.recipeSearchContainer.setVisibility(View.VISIBLE);
                }
                else{
                    binding.recipeSearchContainer.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    private void getLists(){
        for(RecipeSearchAdapter.RecipeSearchText text: NoDb.SEARCH_INGREDIENTS_LIST){
            if(text.isContains()){
                includedIngredients.add(text.getQuery());
            }
            else{
                notIncludedIngredients.add(text.getQuery());
            }
        }

        for(RecipeSearchAdapter.RecipeSearchText text: NoDb.SEARCH_TAGS_LIST){
            if(text.isContains()){
                includedTags.add(text.getQuery());
            }
            else{
                notIncludedTags.add(text.getQuery());
            }
        }
    }

    private void joinFoundRecipes(){
        NoDb.RECIPE_LIST.addAll(NoDb.RECIPE_BY_COMPLEXITY_LIST);
        NoDb.RECIPE_LIST.addAll(NoDb.RECIPE_BY_KCAL_LIST);
        NoDb.RECIPE_LIST.addAll(NoDb.RECIPE_BY_PROTEINS_LIST);
        NoDb.RECIPE_LIST.addAll(NoDb.RECIPE_BY_FATS_LIST);
        NoDb.RECIPE_LIST.addAll(NoDb.RECIPE_BY_CARBOHYDRATES_LIST);
        NoDb.RECIPE_LIST.addAll(NoDb.RECIPE_BY_TIME_LIST);
        NoDb.RECIPE_LIST.addAll(NoDb.RECIPE_BY_INGREDIENTS_LIST);
        NoDb.RECIPE_LIST.addAll(NoDb.RECIPE_BY_INGREDIENTS_NOT_LIST);
        NoDb.RECIPE_LIST.addAll(NoDb.RECIPE_BY_TAGS_LIST);
        NoDb.RECIPE_LIST.addAll(NoDb.RECIPE_BY_TAGS_NOT_LIST);

        Set setItems = new LinkedHashSet(NoDb.RECIPE_LIST);
        NoDb.RECIPE_LIST.clear();
        NoDb.RECIPE_LIST.addAll(setItems);

        List<Recipe> recipesToDelete = new ArrayList<>();

        for (int i = 0; i < NoDb.RECIPE_LIST.size(); i++) {

            if(!checkIsFoundRecipeCorrect(NoDb.RECIPE_LIST.get(i))){
                recipesToDelete.add(NoDb.RECIPE_LIST.get(i));
            }
        }

        NoDb.RECIPE_LIST.removeAll(recipesToDelete);

    }

    private boolean checkIsFoundRecipeCorrect(Recipe recipe){
        String bottomKcal = binding.kcalBottomEditText.getText().toString();
        String topKcal = binding.kcalTopEditText.getText().toString();
        String bottomProteins = binding.proteinsBottomEditText.getText().toString();
        String topProteins = binding.proteinsTopEditText.getText().toString();
        String bottomFats = binding.fatsBottomEditText.getText().toString();
        String topFats = binding.fatsTopEditText.getText().toString();
        String bottomCarbohydrates = binding.carbohydratesBottomEditText.getText().toString();
        String topCarbohydrates = binding.carbohydratesTopEditText.getText().toString();
        String bottomHours = binding.hoursBottomEditText.getText().toString();
        String topHours = binding.hoursTopEditText.getText().toString();
        String bottomMinutes = binding.minutesBottomEditText.getText().toString();
        String topMinutes = binding.minutesTopEditText.getText().toString();

        int bottomTime = (!bottomHours.isEmpty()? Integer.parseInt(bottomHours): 0) * 60 + (!bottomMinutes.isEmpty()? Integer.parseInt(bottomMinutes): 0);
        int topTime = Integer.MAX_VALUE;
        if(!topHours.isEmpty() || !topMinutes.isEmpty()) {
            topTime = (!topHours.isEmpty() ? Integer.parseInt(topHours) : 0) * 60 + (!topMinutes.isEmpty() ? Integer.parseInt(topMinutes) : 0);
        }

        if(!(recipe.getKcal() >= (bottomKcal.isEmpty()? 0: Integer.parseInt(bottomKcal))
                && recipe.getKcal() <= (topKcal.isEmpty()? Integer.MAX_VALUE: Integer.parseInt(topKcal)))){
            Log.d("Sas", "1");
            return false;
        }
        else if(!(recipe.getProteins() >= (bottomProteins.isEmpty()? 0: Integer.parseInt(bottomProteins))
                && recipe.getProteins() <= (topProteins.isEmpty()? Integer.MAX_VALUE: Integer.parseInt(topProteins)))){
            Log.d("Sas", "2");
            return false;
        }
        else if(!(recipe.getFats() >= (bottomFats.isEmpty()? 0: Integer.parseInt(bottomFats))
                && recipe.getFats() <= (topFats.isEmpty()? Integer.MAX_VALUE: Integer.parseInt(topFats)))){
            Log.d("Sas", "3");
            return false;
        }
        else if(!(recipe.getCarbohydrates() >= (bottomCarbohydrates.isEmpty()? 0: Integer.parseInt(bottomCarbohydrates))
                && recipe.getCarbohydrates() <= (topCarbohydrates.isEmpty()? Integer.MAX_VALUE: Integer.parseInt(topCarbohydrates)))){
            Log.d("Sas", "4");
            return false;
        }
        else if(!(recipe.getTime() >= bottomTime
                && recipe.getTime() <= topTime)){
            Log.d("Sas", "5");
            return false;
        }
        else if(recipe.getComplexity() != (int) binding.complexity.getRating() && (int) binding.complexity.getRating() != 0){
            Log.d("sas", binding.complexity.getRating() + "");
            Log.d("sas", recipe.getComplexity() + "");
            Log.d("Sas", "6");
            return false;
        }
        else{
            for (int i = 0; i < includedIngredients.size(); i++) {
                if (!(recipe.getIngredients().toLowerCase(Locale.ROOT).contains(includedIngredients.get(i).toLowerCase(Locale.ROOT)))){
                    Log.d("Sas", "7");
                    return false;
                }
            }
            for (int i = 0; i < notIncludedIngredients.size(); i++) {
                if (recipe.getIngredients().toLowerCase(Locale.ROOT).contains(notIncludedIngredients.get(i).toLowerCase(Locale.ROOT))){
                    Log.d("Sas", "8");
                    return false;
                }
            }
            for (int i = 0; i < includedTags.size(); i++) {
                if (!(recipe.getTags().toLowerCase(Locale.ROOT).contains(includedTags.get(i).toLowerCase(Locale.ROOT)))){
                    Log.d("Sas", "9");
                    return false;
                }
            }
            for (int i = 0; i < notIncludedIngredients.size(); i++) {
                if (recipe.getTags().toLowerCase(Locale.ROOT).contains(notIncludedTags.get(i).toLowerCase(Locale.ROOT))){
                    Log.d("Sas", "10");
                    return false;
                }
            }
        }
        return true;
    }
}