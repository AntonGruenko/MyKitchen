package com.example.uniorproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.uniorproject.R;
import com.example.uniorproject.databinding.FragmentSetRecipeNutritionalValueBinding;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;

public class SetRecipeNutritionalValue extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSetRecipeNutritionalValueBinding binding = FragmentSetRecipeNutritionalValueBinding.inflate(inflater, null, false);
        View view = binding.getRoot();

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.create_container, new SetRecipeTagsFragment(), "setTags")
                        .commit();
            }
        });

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences recipeSharedPreferences = getActivity().getSharedPreferences("recipeSharedPreferences", Context.MODE_PRIVATE);
                int recipeKcal = Integer.parseInt(binding.editKcal.getText().toString());
                int recipeProteins = Integer.parseInt(binding.editProteins.getText().toString());
                int recipeFats = Integer.parseInt(binding.editFats.getText().toString());
                int recipeCarbohydrates = Integer.parseInt(binding.editCarbohydrates.getText().toString());
                int recipeSugar = Integer.parseInt(binding.editSugar.getText().toString());


                String recipeName = recipeSharedPreferences.getString("recipeName", "");
                String recommendations = recipeSharedPreferences.getString("recipeRecommendations", "");
                int recipeTime = recipeSharedPreferences.getInt("recipeTime", 0);
                int recipeComplexity = recipeSharedPreferences.getInt("recipeComplexity", 0);

                String recipeIngredients = NoDb.INGREDIENTS_LIST.stream().map(Object::toString).reduce((t, u) -> t + ";;" + u).orElse("");
                String recipeGuide = NoDb.GUIDE_LIST.stream().map(Object::toString).reduce((t, u) -> t + ";;" + u).orElse("");
                String recipeTags= NoDb.SELECTED_TAG_LIST.stream().map(Object::toString).reduce((t, u) -> t + ";;" + u).orElse("");

                new VolleyAPI(getActivity()).fillUser();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

                Recipe recipe = new Recipe(
                        recipeName,
                        NoDb.USER_LIST.get(sharedPreferences.getInt("userId", 1)),
                        recipeIngredients,
                        recipeGuide,
                        recommendations,
                        recipeTime,
                        recipeKcal,
                        recipeProteins,
                        recipeFats,
                        recipeCarbohydrates,
                        recipeSugar,
                        0,
                        recipeComplexity,
                        recipeTags);

                new VolleyAPI(getActivity()).addRecipe(recipe, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        new VolleyAPI(getActivity()).addPicture(new Picture(recipeSharedPreferences.getString("mainPicture", ""), NoDb.RECIPE_LIST.get(NoDb.RECIPE_LIST.size() - 1), 0));
                        for (int i = 0; i < NoDb.PICTURE_LINK_LIST.size(); i++) {

                            new VolleyAPI(getActivity()).addPicture(new Picture(NoDb.PICTURE_LINK_LIST.get(i), NoDb.RECIPE_LIST.get(NoDb.RECIPE_LIST.size() - 1), i+1));
                        }

                        NoDb.SELECTED_TAG_LIST.clear();
                        NoDb.GUIDE_LIST.clear();
                        NoDb.INGREDIENTS_LIST.clear();
                        NoDb.PICTURE_LINK_LIST.clear();
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            }
        });
        return view;
    }
}