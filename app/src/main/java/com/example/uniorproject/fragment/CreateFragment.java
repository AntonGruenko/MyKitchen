package com.example.uniorproject.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.databinding.FragmentCreateBinding;
import com.example.uniorproject.databinding.FragmentFeedBinding;

public class CreateFragment extends Fragment {
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        FragmentCreateBinding binding = FragmentCreateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        changeFragment(new CreateRecipeFragment());

        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new CreatePostFragment());
            }
        });

        binding.recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new CreateRecipeFragment());
            }
        });

        binding.videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new CreateVideoFragment());
            }
        });
        return view;


    }

    private boolean changeFragment(Fragment fragment) {
        if (fragment == null) {
            return false;
        } else {
            ((MainActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.create_container, fragment)
                    .commit();
            return true;
        }
    }
}