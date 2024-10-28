package com.example.uniorproject.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.VideosAdapter;
import com.example.uniorproject.databinding.FragmentVideoBinding;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;

public class VideoFragment extends Fragment {

    private FragmentVideoBinding binding;
    private VideosAdapter adapter;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater);
        context = getContext();

        if(getActivity() != null) {
            new VolleyAPI(context).fillPost(0, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    adapter = new VideosAdapter(context, NoDb.POST_LIST);
                    binding.postsVp.setAdapter(adapter);
                }

                @Override
                public void onError(@Nullable VolleyError error) {

                }
            });
        }
        return binding.getRoot();
    }
}