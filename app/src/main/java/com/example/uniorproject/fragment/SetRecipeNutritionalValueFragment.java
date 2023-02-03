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
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.databinding.FragmentSetRecipeNutritionalValueBinding;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.RecipeMapper;
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
    private Context context;
    private SharedPreferences recipeSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSetRecipeNutritionalValueBinding binding = FragmentSetRecipeNutritionalValueBinding.inflate(inflater, null, false);
        View view = binding.getRoot();
        context = getContext();
        recipeSharedPreferences = context.getSharedPreferences("recipeSharedPreferences", Context.MODE_PRIVATE);

        float kcal, proteins, fats, carbohydrates, sugar;
        kcal = recipeSharedPreferences.getFloat("recipeKcal", 0);
        proteins = recipeSharedPreferences.getFloat("recipeProteins", 0);
        fats = recipeSharedPreferences.getFloat("recipeFats", 0);
        carbohydrates = recipeSharedPreferences.getFloat("recipeCarbohydrates", 0);
        sugar = recipeSharedPreferences.getFloat("recipeSugar", 0);

        binding.editKcal.setText(String.valueOf(kcal));
        binding.editProteins.setText(String.valueOf(proteins));
        binding.editFats.setText(String.valueOf(fats));
        binding.editCarbohydrates.setText(String.valueOf(carbohydrates));
        binding.editSugar.setText(String.valueOf(sugar));

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).getSupportFragmentManager()
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

                    int recipeKcal = (int) Float.parseFloat(kcal);
                    int recipeProteins = (int) Float.parseFloat(proteins);
                    int recipeFats = (int) Float.parseFloat(fats);
                    int recipeCarbohydrates = (int) Float.parseFloat(carbohydrates);
                    int recipeSugar = (int) Float.parseFloat(sugar);

                    binding.progressBar.setVisibility(View.VISIBLE);
                    String recipeName = recipeSharedPreferences.getString("recipeName", "");
                    String recommendations = recipeSharedPreferences.getString("recipeRecommendations", "");
                    int recipeTime = recipeSharedPreferences.getInt("recipeTime", 0);
                    int recipeComplexity = recipeSharedPreferences.getInt("recipeComplexity", 0);

                    String recipeIngredients = NoDb.INGREDIENTS_LIST.stream().map(Object::toString).reduce((t, u) -> t + ";;" + u).orElse("");
                    String recipeGuide = NoDb.GUIDE_LIST.stream().map(Object::toString).reduce((t, u) -> t + ";;" + u).orElse("");
                    String recipeTags = NoDb.SELECTED_TAG_LIST.stream().map(Object::toString).reduce((t, u) -> t + ";;" + u).orElse("");

                    new VolleyAPI(context).fillUser();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

                    final User[] author = new User[1];
                    new VolleyAPI(context).findUserByEmail(sharedPreferences.getString("userEmail", ""), new VolleyCallback() {
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

                            new VolleyAPI(context).addRecipe(recipe, new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    Recipe resultRecipe = RecipeMapper.recipeFromJson(response);

                                    new VolleyAPI(context).addPicture(new Picture(recipeSharedPreferences.getString("mainPicture", ""), resultRecipe, 0));
                                    for (int i = 0; i < NoDb.PICTURE_LINK_LIST.size(); i++) {
                                        new VolleyAPI(context).addPicture(new Picture(NoDb.PICTURE_LINK_LIST.get(i), resultRecipe, i + 1));
                                    }

                                    NoDb.SELECTED_TAG_LIST.clear();
                                    NoDb.GUIDE_LIST.clear();
                                    NoDb.INGREDIENTS_LIST.clear();
                                    NoDb.PICTURE_LINK_LIST.clear();

                                    binding.progressBar.setVisibility(View.GONE);
                                    Toast.makeText(context, "Опубликовано!", Toast.LENGTH_SHORT).show();
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