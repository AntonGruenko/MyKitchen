package com.example.uniorproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uniorproject.R;
import com.example.uniorproject.adapter.PostAdapter;
import com.example.uniorproject.databinding.FragmentPostBinding;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.LibraryAPIVolley;

public class PostFragment extends Fragment {
    private PostAdapter postAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPostBinding binding = FragmentPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        new LibraryAPIVolley(getActivity()).fillPost();

        postAdapter = new PostAdapter(getActivity(), NoDb.POST_LIST);
        binding.postsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.postsRv.setAdapter(postAdapter);
        return view;
    }

    public void updateAdapter(){
        postAdapter.notifyDataSetChanged();
    }
}