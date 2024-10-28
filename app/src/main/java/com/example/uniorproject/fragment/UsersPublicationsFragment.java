package com.example.uniorproject.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.PostAdapter;
import com.example.uniorproject.adapter.RecipeFeedAdapter;
import com.example.uniorproject.databinding.FragmentUsersPublicationsBinding;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.mapper.PictureMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UsersPublicationsFragment extends Fragment {
    public static final int RECIPE_FRAGMENT = 1;
    public static final int POST_FRAGMENT = 2;
    private final int fragmentId;
    private final int userId;
    private FragmentUsersPublicationsBinding binding;
    private RecipeFeedAdapter recipeAdapter;
    private PostAdapter postAdapter;

    private VolleyCallback picturesCallback;
    private RecipeFeedAdapter.OnRecipeClickListener listener;
    private VolleyCallback postsCallback;

    private Context context;

    public UsersPublicationsFragment(int fragmentId, int userId) {
        this.fragmentId = fragmentId;
        this.userId = userId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsersPublicationsBinding.inflate(inflater);
        context = getContext();
        binding.contentRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        if(fragmentId == RECIPE_FRAGMENT && getActivity() != null){
            listener = new RecipeFeedAdapter.OnRecipeClickListener() {
                @Override
                public void onClick(Recipe recipe, int position) {
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new RecipeFragment(recipe.getId(), recipe.getAuthor().getId(), RecipeFragment.FROM_PROFILE), "RecipeFragment")
                            .commit();
                }
            };

            picturesCallback = new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    recipeAdapter = new RecipeFeedAdapter(context, NoDb.RECIPE_LIST, listener, NoDb.PICTURE_LIST);
                    binding.contentRecyclerView.setAdapter(recipeAdapter);
                }

                @Override
                public void onError(VolleyError error) {

                }
            };

            new VolleyAPI(getContext()).findRecipesByAuthor(userId, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    new VolleyAPI(context).findPreviewsByRecipeAuthor(picturesCallback, userId);
                }

                @Override
                public void onError(@Nullable VolleyError error) {

                }
            });
        }
        else if(getActivity() != null){
            postsCallback = new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    List<Post> postToDelete = new ArrayList<>();
                    for (Post post :
                            NoDb.POST_LIST) {
                        if(post.getPicture().contains("videos")){
                            postToDelete.add(post);
                        }
                    }
                    NoDb.POST_LIST.removeAll(postToDelete);
                    postAdapter = new PostAdapter(context, NoDb.POST_LIST);
                    binding.contentRecyclerView.setAdapter(postAdapter);
                }

                @Override
                public void onError(@Nullable VolleyError error) {

                }
            };
            new VolleyAPI(context).findPostByAuthor(userId, postsCallback);
        }
        return binding.getRoot();
    }
}