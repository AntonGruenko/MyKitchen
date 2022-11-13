package com.example.uniorproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uniorproject.R;
import com.example.uniorproject.databinding.FragmentCreatePostBinding;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.LibraryAPIVolley;

public class CreatePostFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCreatePostBinding binding = FragmentCreatePostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post(
                        NoDb.USER_LIST.get(0),
                        binding.editText.getText().toString(),
                        "0",
                        0);
                    new LibraryAPIVolley(getActivity()).addPost(post);
            }
        });
        return view;
    }
}