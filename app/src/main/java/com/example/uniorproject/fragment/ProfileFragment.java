 package com.example.uniorproject.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.android.volley.VolleyError;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.CommentAdapter;
import com.example.uniorproject.adapter.MealsAdapter;
import com.example.uniorproject.adapter.PostAdapter;
import com.example.uniorproject.adapter.RecipeFeedAdapter;
import com.example.uniorproject.databinding.FragmentProfileBinding;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.PictureMapper;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String userEmail;
    private User currentUser;
    private RecipeFeedAdapter recipeAdapter;
    private PostAdapter postAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, null, false);
        View view = binding.getRoot();

        sharedPreferences = getContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("userEmail", "");
        new VolleyAPI(getContext()).findUserByEmail(userEmail, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                currentUser = UserMapper.userFromJson(response);
                new VolleyAPI(getContext()).findRecipesByAuthor(currentUser.getId(), new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                });
                new VolleyAPI(getContext()).findPostByAuthor(currentUser.getId(), new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                });
                if (!currentUser.getProfilePic().isEmpty()) {
                    Picasso.with(getContext()).load(currentUser.getProfilePic()).into(binding.avatarImage);
                }
                binding.status.setText(currentUser.getStatus());
                binding.name.setText(currentUser.getName());

                new VolleyAPI(getContext()).findMealsByUser(currentUser.getId(), new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });
                binding.recipesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeFragment(new UsersPublicationsFragment(UsersPublicationsFragment.RECIPE_FRAGMENT, currentUser.getId()));
                    }
                });
                binding.postsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeFragment(new UsersPublicationsFragment(UsersPublicationsFragment.POST_FRAGMENT, currentUser.getId()));
                    }
                });

                binding.status.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        new VolleyAPI(getContext()).updateUser(currentUser.getId(),
                                currentUser.getName(),
                                currentUser.getEmail(),
                                currentUser.getPassword(),
                                charSequence.toString(),
                                currentUser.getProfilePic(),
                                currentUser.getKcal(),
                                currentUser.getProteins(),
                                currentUser.getFats(),
                                currentUser.getCarbohydrates(),
                                currentUser.getRegistrationDate());
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
            }

            @Override
            public void onError(VolleyError error) {

            }
        });


        binding.avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(getActivity(), currentUser);
            }
        });

        binding.statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new StatisticsFragment());
            }
        });

        return view;
    }

    private void openFileChooser(Activity activity, User user){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("userId", user.getId());
        editor.putString("userName", user.getName());
        editor.putString("userEmail", user.getEmail());
        editor.putString("userPassword", user.getPassword());
        editor.putString("userStatus", user.getStatus());
        editor.putInt("userKcal", user.getKcal());
        editor.putInt("userProteins", user.getProteins());
        editor.putInt("userFats", user.getFats());
        editor.putInt("userCarbohydrates", user.getCarbohydrates());
        editor.putLong("registrationDate", user.getRegistrationDate());
        editor.apply();
        activity.startActivityForResult(intent, 8);

    }



    public void updateRecipeAdapter(){
        recipeAdapter.notifyDataSetChanged();
    }

    public void updatePostAdapter(){
        postAdapter.notifyDataSetChanged();
    }


    public void updateProfilePicture(Uri uri){
        Picasso.with(getContext()).load(uri).into(binding.avatarImage);
    }

    private boolean changeFragment(Fragment fragment) {
        if (fragment == null) {
            return false;
        } else {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.bottom_container, fragment)
                    .commit();
            return true;
        }
    }



}