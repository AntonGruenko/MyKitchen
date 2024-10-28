package com.example.uniorproject.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.PostAdapter;
import com.example.uniorproject.databinding.FragmentPostBinding;
import com.example.uniorproject.databinding.FragmentPostsBinding;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;


public class PostsFragment extends Fragment {

    private FragmentPostsBinding binding;
    private PostAdapter adapter;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostsBinding.inflate(inflater);
        context = getContext();

        binding.postsRv.setLayoutManager(new LinearLayoutManager(context));
        new VolleyAPI(context).fillPost(1, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                adapter = new PostAdapter(context, NoDb.POST_LIST);
                binding.postsRv.setAdapter(adapter);
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        });
        return binding.getRoot();
    }
}