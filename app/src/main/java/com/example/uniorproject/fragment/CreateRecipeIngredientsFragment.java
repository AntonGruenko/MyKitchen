package com.example.uniorproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uniorproject.R;
import com.example.uniorproject.adapter.RecipeCreatorAdapter;
import com.example.uniorproject.databinding.FragmentCreateRecipeIngredientsBinding;
import com.example.uniorproject.databinding.FragmentFeedBinding;
import com.example.uniorproject.noDb.NoDb;

public class CreateRecipeIngredientsFragment extends Fragment {

    private RecipeCreatorAdapter recipeCreatorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentCreateRecipeIngredientsBinding binding = FragmentCreateRecipeIngredientsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        if(NoDb.INGREDIENTS_LIST.size() == 0) {
            NoDb.INGREDIENTS_LIST.add("");
        }

        recipeCreatorAdapter = new RecipeCreatorAdapter(getActivity(), NoDb.INGREDIENTS_LIST, 1);
        binding.ingredientsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.ingredientsRv.setAdapter(recipeCreatorAdapter);

        binding.ingredientsRv.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoDb.INGREDIENTS_LIST.add("");
                recipeCreatorAdapter.notifyItemInserted(NoDb.INGREDIENTS_LIST.size());
            }
        });

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < NoDb.INGREDIENTS_LIST.size(); i++) {
                    Log.d("APP", NoDb.INGREDIENTS_LIST.get(i));
                }
                if(NoDb.INGREDIENTS_LIST.size() > 0 && !(NoDb.INGREDIENTS_LIST.size() == 1 && NoDb.INGREDIENTS_LIST.get(0).equals(""))) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.create_container, new CreateRecipeGuideFragment(), "setGuide")
                            .commit();
                }
                else{
                    Toast.makeText(getContext(), "Введите ингредиенты!", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.create_container, new CreateRecipeFragment(), "createRecipe")
                        .commit();
            }
        });
        return view;
    }
}