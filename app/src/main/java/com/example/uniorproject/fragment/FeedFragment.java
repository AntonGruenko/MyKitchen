package com.example.uniorproject.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.uniorproject.databinding.FragmentFeedBinding;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.Subscription;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.PictureMapper;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FeedFragment extends Fragment {

    private RecipeFeedAdapter recipeFeedAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FragmentFeedBinding binding = FragmentFeedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.recipesRv.setItemAnimator(null);
        new VolleyAPI(getContext()).fillRecipe(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                binding.recipesRv.setVisibility(View.VISIBLE);
                getAllRecipes(binding);
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        });

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new CreateFragment())
                        .commit();
            }
        });

        VolleyCallback authorsCallback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                binding.recipesRv.setVisibility(View.VISIBLE);
                new VolleyAPI(getContext()).findPreviews(new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {;
                        recipeFeedAdapter = new RecipeFeedAdapter(getContext(), NoDb.RECIPE_LIST, new RecipeFeedAdapter.OnRecipeClickListener() {
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

                        recipeFeedAdapter.notifyDataSetChanged();
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
        VolleyCallback followerCallback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                List<Integer> leaders = new ArrayList<>();
                for(Subscription subscription: NoDb.SUBSCRIPTION_LIST){
                    leaders.add(subscription.getLeader().getId());
                }
                new VolleyAPI(getContext()).findRecipesByAuthors(leaders, authorsCallback);
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        };

        VolleyCallback emailCallback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                User currentUser = UserMapper.userFromJson(response);
                new VolleyAPI(getContext()).findSubscriptionByFollowerId(currentUser.getId(), followerCallback);
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        };

        binding.subscribersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    new VolleyAPI(getContext()).findUserByEmail(sharedPreferences.getString("userEmail", ""), emailCallback);
                }
                else{
                    new VolleyAPI(getContext()).fillRecipe(new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            getAllRecipes(binding);
                        }

                        @Override
                        public void onError(@Nullable VolleyError error) {

                        }
                    });
                }
            }
        });
        return view;
    }

    public void updateAdapter(){
        recipeFeedAdapter.notifyDataSetChanged();
    }

    private void getAllRecipes(FragmentFeedBinding binding){
        new VolleyAPI(getContext()).findPreviews(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                recipeFeedAdapter = new RecipeFeedAdapter(getContext(), NoDb.RECIPE_LIST, new RecipeFeedAdapter.OnRecipeClickListener() {
                    @Override
                    public void onClick(Recipe recipe, int position) {
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

                recipeFeedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}