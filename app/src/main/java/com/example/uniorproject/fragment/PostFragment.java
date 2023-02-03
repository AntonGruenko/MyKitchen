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
import com.example.uniorproject.databinding.FragmentPostBinding;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;

public class PostFragment extends Fragment {
    private PostAdapter postAdapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPostBinding binding = FragmentPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        changeFragment(new PostsFragment());

        binding.postsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new PostsFragment());
            }
        });
        binding.videosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new VideoFragment());
            }
        });

        return view;
    }

    private boolean changeFragment(Fragment fragment) {
        if (fragment == null) {
            return false;
        } else {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.posts_container, fragment)
                    .commit();
            return true;
        }
    }

}