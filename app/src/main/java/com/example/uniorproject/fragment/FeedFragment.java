package com.example.uniorproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uniorproject.R;
import com.example.uniorproject.adapter.RecipeFeedAdapter;
import com.example.uniorproject.databinding.FragmentFeedBinding;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;

public class FeedFragment extends Fragment {

    private RecipeFeedAdapter recipeFeedAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentFeedBinding binding = FragmentFeedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        new VolleyAPI(getContext()).fillRecipe();
        recipeFeedAdapter = new RecipeFeedAdapter(this.getContext(), NoDb.RECIPE_LIST, new RecipeFeedAdapter.OnRecipeClickListener() {
            @Override
            public void onClick(Recipe recipe, int position) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new RecipeFragment(position))
                        .commit();
            }
        });
        binding.recipesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recipesRv.setAdapter(recipeFeedAdapter);



        return view;
    }

    public void updateAdapter(){
        recipeFeedAdapter.notifyDataSetChanged();
    }
}