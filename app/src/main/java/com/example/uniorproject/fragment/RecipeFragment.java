package com.example.uniorproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uniorproject.R;
import com.example.uniorproject.adapter.RecipeDataAdapter;
import com.example.uniorproject.database.ShoppingListDatabase;
import com.example.uniorproject.databinding.FragmentRecipeBinding;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.noDb.NoDb;

import java.util.Arrays;
import java.util.List;

public class RecipeFragment extends Fragment {
    private int recipeId;
    private int minutes, hours;
    private List<String> ingredients, guide, tags;
    private RecipeDataAdapter ingredientsAdapter, guideAdapter, tagsAdapter;
    private ShoppingListDatabase database;

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

        hours = recipe.getTime()/60;
        minutes = recipe.getTime() - hours * 60;
        database = new ShoppingListDatabase(getActivity());

        binding.recipeTitle.setText(recipe.getName());
        binding.recipeAuthorName.setText(String.format("Автор: %s", recipe.getAuthor().getName()));
        binding.recipeTime.setText(String.format("%d:%d", hours, minutes));
        binding.recipeKcal.setText(String.valueOf(recipe.getKcal()));
        binding.recipeProteins.setText(String.format("Белки на 100г: %d", recipe.getProteins()));
        binding.recipeFats.setText(String.format("Жиры на 100г: %d", recipe.getProteins()));
        binding.recipeCarbohydrates.setText(String.format("Углеводы на 100г: %d", recipe.getProteins()));
        binding.recipeSugar.setText(String.format("Сахар на 100г: %d", recipe.getProteins()));
        binding.recipeComplexity.setNumStars((int) (recipe.getComplexity() * 1.2));
        binding.recipeRecommendations.setText(recipe.getReccomendations());
        binding.recipeLikes.setText(String.valueOf(recipe.getLikes()));

        ingredients = convertStringToList(recipe.getIngredients());
        guide = convertStringToList(recipe.getGuide());
        tags = convertStringToList(recipe.getTags());

        ingredientsAdapter = new RecipeDataAdapter(getActivity(), ingredients);
        binding.recipeIngredients.setAdapter(ingredientsAdapter);
        binding.recipeIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));

        guideAdapter = new RecipeDataAdapter(getActivity(), guide);
        binding.recipeGuide.setAdapter(guideAdapter);
        binding.recipeGuide.setLayoutManager(new LinearLayoutManager(getActivity()));

        tagsAdapter = new RecipeDataAdapter(getActivity(), tags);
        binding.recipeTags.setAdapter(tagsAdapter);
        binding.recipeTags.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        binding.copyIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < ingredients.size(); i++) {
                    database.insert(database.length(), ingredients.get(i));
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
}