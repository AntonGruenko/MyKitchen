package com.example.uniorproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.ChatsListAdapter;
import com.example.uniorproject.databinding.FragmentChatsListBinding;
import com.example.uniorproject.domain.Message;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;

public class ChatsListFragment extends Fragment {
    private FragmentChatsListBinding binding;
    private SharedPreferences sharedPreferences;
    private User currentUser;
    private ChatsListAdapter adapter;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatsListBinding.inflate(inflater);
        context = getContext();
        sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        binding.chatsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        new VolleyAPI(context).findUserByEmail(sharedPreferences.getString("userEmail", ""), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                currentUser = UserMapper.userFromJson(response);
                new VolleyAPI(context).findMessagesByUser(currentUser.getId(), new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        adapter = new ChatsListAdapter(context, NoDb.MESSAGES_FEED_LIST, new ChatsListAdapter.ClickListener() {
                            @Override
                            public void onClick(int position) {
                                Message message = NoDb.MESSAGES_FEED_LIST.get(position);
                                User receiver = (message.getReceiver().getId() == currentUser.getId() ? message.getSender() : message.getReceiver());
                                new VolleyAPI(context).findMessagesBySenderAndReceiver(currentUser.getId(), receiver.getId(), new VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        ((MainActivity) context).getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container, new ChatFragment(currentUser, receiver))
                                                .commit();
                                    }

                                    @Override
                                    public void onError(@Nullable VolleyError error) {

                                    }
                                });
                            }
                        }, currentUser);
                        binding.chatsRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                });
            }

            @Override
            public void onError(@Nullable VolleyError error) {

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