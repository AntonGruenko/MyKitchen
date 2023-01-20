package com.example.uniorproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.uniorproject.R;
import com.example.uniorproject.databinding.FragmentSetRecipeNutritionalValueBinding;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;

public class SetRecipeNutritionalValueFragment extends Fragment {

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

        binding.progressBar.setVisibility(View.GONE);

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String kcal = binding.editKcal.getText().toString();
                String proteins = binding.editProteins.getText().toString();
                String fats = binding.editFats.getText().toString();
                String carbohydrates = binding.editCarbohydrates.getText().toString();
                String sugar = binding.editSugar.getText().toString();

                if(kcal != "" &&
                        proteins != "" &&
                        fats != "" &&
                        carbohydrates != "" &&
                        sugar != "") {

                    SharedPreferences recipeSharedPreferences = getActivity().getSharedPreferences("recipeSharedPreferences", Context.MODE_PRIVATE);
                    int recipeKcal = Integer.parseInt(kcal);
                    int recipeProteins = Integer.parseInt(proteins);
                    int recipeFats = Integer.parseInt(fats);
                    int recipeCarbohydrates = Integer.parseInt(carbohydrates);
                    int recipeSugar = Integer.parseInt(sugar);

                    binding.progressBar.setVisibility(View.VISIBLE);
                    String recipeName = recipeSharedPreferences.getString("recipeName", "");
                    String recommendations = recipeSharedPreferences.getString("recipeRecommendations", "");
                    int recipeTime = recipeSharedPreferences.getInt("recipeTime", 0);
                    int recipeComplexity = recipeSharedPreferences.getInt("recipeComplexity", 0);

                    String recipeIngredients = NoDb.INGREDIENTS_LIST.stream().map(Object::toString).reduce((t, u) -> t + ";;" + u).orElse("");
                    String recipeGuide = NoDb.GUIDE_LIST.stream().map(Object::toString).reduce((t, u) -> t + ";;" + u).orElse("");
                    String recipeTags = NoDb.SELECTED_TAG_LIST.stream().map(Object::toString).reduce((t, u) -> t + ";;" + u).orElse("");

                    new VolleyAPI(getActivity()).fillUser();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

                    final User[] author = new User[1];
                    new VolleyAPI(getContext()).findUserByEmail(sharedPreferences.getString("userEmail", ""), new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            author[0] = UserMapper.userFromJson(response);

                            Recipe recipe = new Recipe(
                                    recipeName,
                                    author[0],
                                    recipeIngredients,
                                    recipeGuide,
                                    recommendations,
                                    recipeTime,
                                    recipeKcal,
                                    recipeProteins,
                                    recipeFats,
                                    recipeCarbohydrates,
                                    recipeSugar,
                                    recipeComplexity,
                                    recipeTags);

                            new VolleyAPI(getActivity()).addRecipe(recipe, new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    new VolleyAPI(getActivity()).addPicture(new Picture(recipeSharedPreferences.getString("mainPicture", ""), NoDb.RECIPE_LIST.get(NoDb.RECIPE_LIST.size() - 1), 0));
                                    for (int i = 0; i < NoDb.PICTURE_LINK_LIST.size(); i++) {
                                        new VolleyAPI(getActivity()).addPicture(new Picture(NoDb.PICTURE_LINK_LIST.get(i), NoDb.RECIPE_LIST.get(NoDb.RECIPE_LIST.size() - 1), i + 1));
                                    }

                                    NoDb.SELECTED_TAG_LIST.clear();
                                    NoDb.GUIDE_LIST.clear();
                                    NoDb.INGREDIENTS_LIST.clear();
                                    NoDb.PICTURE_LINK_LIST.clear();

                                    binding.progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Опубликовано!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(VolleyError error) {

                                }
                            });
                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });
                }
            }
        });
        return view;
    }
}