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

import com.example.uniorproject.R;
import com.example.uniorproject.databinding.FragmentCreateRecipeBinding;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.LibraryAPIVolley;

public class CreateRecipeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCreateRecipeBinding binding = FragmentCreateRecipeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.editTitle.getText().toString().equals("")
                        || binding.editTimeHours.getText().toString().equals("")
                        || binding.editTimeMinutes.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Введите данные!", Toast.LENGTH_LONG).show();
                }
                else{
                    String recipeName = binding.editTitle.getText().toString();
                    int recipeTime = Integer.parseInt(binding.editTimeHours.getText().toString()) * 60
                            + Integer.parseInt(binding.editTimeMinutes.getText().toString());
                    String recipeRecommendations = binding.editRecommendations.getText().toString();
                    int recipeComplexity = (int) binding.complexity.getRating();

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("recipeSharedPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("recipeName", recipeName);
                    editor.putInt("recipeTime", recipeTime);
                    editor.putString("recipeRecommendations", recipeRecommendations);
                    editor.putInt("recipeComplexity", recipeComplexity);
                    editor.apply();

                    CreateRecipeIngredientsFragment fragment = new CreateRecipeIngredientsFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.create_container, fragment, "setIngredients")
                            .commit();
                }
            }
        });

        return view;
    }
}