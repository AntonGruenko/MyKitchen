package com.example.uniorproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uniorproject.R;
import com.example.uniorproject.adapter.RecipeAdapter;
import com.example.uniorproject.databinding.ActivityMainBinding;
import com.example.uniorproject.databinding.FragmentFeedBinding;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.LibraryAPIVolley;

public class FeedFragment extends Fragment {

    private RecipeAdapter recipeAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentFeedBinding binding = FragmentFeedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        new LibraryAPIVolley(getContext()).fillRecipe();
        recipeAdapter = new RecipeAdapter(this.getContext(), NoDb.RECIPE_LIST);
        binding.recipesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recipesRv.setAdapter(recipeAdapter);

        return view;
    }

    public void updateAdapter(){
        recipeAdapter.notifyDataSetChanged();
    }
}