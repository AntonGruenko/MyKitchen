package com.example.uniorproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.PostAdapter;
import com.example.uniorproject.adapter.RecipeFeedAdapter;
import com.example.uniorproject.databinding.FragmentEditProfileBinding;
import com.example.uniorproject.databinding.FragmentUsersPublicationsBinding;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;


public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private Context context;
    private SharedPreferences sharedPreferences;

    private String userName;
    private String userPassword;
    private String userEmail;
    private User currentUser;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater);
        context = getContext();
        sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("userEmail", "");

        new VolleyAPI(context).findUserByEmail(userEmail, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                currentUser = UserMapper.userFromJson(response);
                binding.nameEditText.setText(currentUser.getName());
                binding.passwordEditText.setText(currentUser.getPassword());
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = binding.nameEditText.getText().toString();
                userPassword = binding.passwordEditText.getText().toString();


                new VolleyAPI(context).updateUser(currentUser.getId(),
                        userName,
                        currentUser.getEmail(),
                        userPassword,
                        currentUser.getStatus(),
                        currentUser.getProfilePic(),
                        currentUser.getKcal(),
                        currentUser.getProteins(),
                        currentUser.getFats(),
                        currentUser.getCarbohydrates(),
                        currentUser.getRegistrationDate(),
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new ProfileFragment(), "UserProfile")
                                        .commit();
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((MainActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment())
                        .commit();
            }
        });


        return binding.getRoot();
    }

}