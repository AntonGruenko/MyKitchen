package com.example.uniorproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.uniorproject.ImageRotationDetectionHelper;
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.PostAdapter;
import com.example.uniorproject.adapter.RecipeFeedAdapter;
import com.example.uniorproject.databinding.FragmentAnotherProfileBinding;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.Subscription;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.PictureMapper;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.google.protobuf.GeneratedMessageLite;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnotherProfileFragment extends Fragment {

    private FragmentAnotherProfileBinding binding;
    private final int userId;
    private User user;
    private RecipeFeedAdapter recipeFeedAdapter;
    private PostAdapter postAdapter;
    private SharedPreferences sharedPreferences;
    private User currentUser;

    private RecipeFeedAdapter.OnRecipeClickListener listener;
    private VolleyCallback recipesCallback;
    private VolleyCallback picturesCallback;

    private Context context;
    private int recipeId;
    private int profileId;

    public static final short FROM_RECIPE = 1;
    public static final short FROM_POST = 2;
    public static final short FROM_SEARCH = 3;
    public static final short FROM_CHAT = 4;

    private short fromFragment;

    public AnotherProfileFragment(int userId, int recipeId){
        this.userId = userId;
        this.recipeId = recipeId;
        this.fromFragment = FROM_RECIPE;
    }

    public AnotherProfileFragment(int userId, short fromFragment){
        this.userId = userId;
        this.fromFragment = fromFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAnotherProfileBinding.inflate(inflater);
        View view = binding.getRoot();
        context = getContext();

        sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        new VolleyAPI(context).findUserById(userId, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                user = UserMapper.userFromJson(response);
                binding.name.setText(user.getName());
                binding.status.setText(user.getStatus());
                binding.profileRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                if (user.getProfilePic().isEmpty()) {
                    binding.avatarImage.setImageResource(R.drawable.ic_baseline_person_24);
                } else {
                    Glide.
                            with(context).
                            load(user.getProfilePic()).
                            centerCrop().
                            into(binding.avatarImage);
                }

                picturesCallback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        recipeFeedAdapter = new RecipeFeedAdapter(context, NoDb.RECIPE_LIST, listener, NoDb.PICTURE_LIST);
                        binding.profileRecyclerView.setAdapter(recipeFeedAdapter);
                    }
                    @Override
                    public void onError(VolleyError error) {
                    }
                };

                recipesCallback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        new VolleyAPI(context).findPreviewsByRecipeAuthor(picturesCallback, user.getId());
                    }
                    @Override
                    public void onError(@Nullable VolleyError error) {
                    }
                };

                new VolleyAPI(context).findRecipesByAuthor(user.getId(), recipesCallback);
                listener = new RecipeFeedAdapter.OnRecipeClickListener() {
                    @Override
                    public void onClick(Recipe recipe, int position) {
                        ((MainActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new RecipeFragment(recipe.getId(), user.getId(), RecipeFragment.FROM_ANOTHER_PROFILE), "RecipeFragment")
                                .commit();
                    }
                };

                binding.recipesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new VolleyAPI(context).findRecipesByAuthor(user.getId(), recipesCallback);

                    }
                });

                binding.postsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        VolleyCallback postCallback = new VolleyCallback() {
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
                                binding.profileRecyclerView.setAdapter(postAdapter);
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        };

                        new VolleyAPI(context).findPostByAuthor(user.getId(), postCallback);
                    }
                });

                VolleyCallback deleteCallback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        setSubscribersText(response);
                        binding.subscribeButton.setText("Подписаться");
                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                };

                VolleyCallback subscribeCallback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        setSubscribersText(response);
                        binding.subscribeButton.setText("Подписка оформлена");
                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                };

                VolleyCallback subscribersNumCallback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                            setSubscribersText(response);
                        }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                };

                VolleyCallback checkCallback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        boolean flag = false;
                        try {
                            flag = response.getBoolean("res");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (flag) {
                            new VolleyAPI(context).deleteSubscription(user.getId(), currentUser.getId(), deleteCallback);
                        } else {
                            Subscription subscription = new Subscription(user, currentUser);
                            new VolleyAPI(context).addSubscription(subscription, subscribeCallback);
                        }
                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                };

                VolleyCallback emailCallback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        currentUser = UserMapper.userFromJson(response);
                        new VolleyAPI(context).checkSubscription(userId, currentUser.getId(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                boolean flag = false;
                                try {
                                    flag = response.getBoolean("res");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (flag) {
                                    binding.subscribeButton.setText("Подписка оформлена");
                                }
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });

                        binding.messageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new VolleyAPI(context).findMessagesBySenderAndReceiver(currentUser.getId(), user.getId(), new VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        ((MainActivity) context).getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container, new ChatFragment(currentUser, user))
                                                .commit();
                                    }

                                    @Override
                                    public void onError(@Nullable VolleyError error) {

                                    }
                                });
                            }
                        });

                        binding.subscribeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new VolleyAPI(context).checkSubscription(user.getId(), currentUser.getId(), checkCallback);
                            }
                        });
                        new VolleyAPI(context).findSubscriptionsByLeaderId(user.getId(), subscribersNumCallback);
                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                };

                new VolleyAPI(context).findUserByEmail(sharedPreferences.getString("userEmail", ""), emailCallback);
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }

        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(fromFragment == FROM_RECIPE) {
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new RecipeFragment(recipeId, RecipeFragment.FROM_ANOTHER_PROFILE))
                            .commit();
                }
                else if(fromFragment == FROM_SEARCH){
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new SearchFragment())
                            .commit();
                }
                else if(fromFragment == FROM_POST){
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new PostFragment())
                            .commit();
                }
                else {
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new ChatsListFragment())
                            .commit();
                }
            }
        });

        return view;
    }

    private void setSubscribersText(JSONObject response){
        String text = "";
        try {
            if(response == null){
                return;
            }
            int num = response.getInt("num");
            if(num == 1){
                text = num + " Подписчик";
            }
            else if(num > 1 && num < 5){
                text = num + " Подписчика";
            }
            else if(num > 4){
                text = num + " Подписчиков";
            }
            binding.subscribersText.setText(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}