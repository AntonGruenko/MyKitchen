package com.example.uniorproject.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.RecipeFeedAdapter;
import com.example.uniorproject.databinding.FragmentFeedBinding;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.mapper.PictureMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;

public class FeedFragment extends Fragment {

    private RecipeFeedAdapter recipeFeedAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new VolleyAPI(getContext()).fillRecipe();
        new VolleyAPI(getContext()).fillPicture();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentFeedBinding binding = FragmentFeedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recipeFeedAdapter = new RecipeFeedAdapter(this.getContext(), NoDb.RECIPE_LIST, new RecipeFeedAdapter.OnRecipeClickListener() {
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
        binding.recipesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recipesRv.setAdapter(recipeFeedAdapter);



        return view;
    }

    public void updateAdapter(){
        recipeFeedAdapter.notifyDataSetChanged();
    }
}