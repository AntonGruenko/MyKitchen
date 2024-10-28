package com.example.uniorproject.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.os.FileUtils.copy;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.ChatAdapter;
import com.example.uniorproject.databinding.FragmentChatBinding;
import com.example.uniorproject.domain.Message;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class ChatFragment extends Fragment {

    private final User sender;
    private final User receiver;
    private FragmentChatBinding binding;
    private ChatAdapter adapter;
    private Context context;
    private Uri imageUri;
    private ImageView messagePicture;
    private final StorageReference storageReference;
    private Message newMessage;

    public ChatFragment(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.storageReference = FirebaseStorage.getInstance().getReference("messagePictures");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater);
        context = getContext();
        newMessage = new Message();
        newMessage.setPicture("");
        binding.receiverName.setText(receiver.getName());
        adapter = new ChatAdapter(context, NoDb.MESSAGE_LIST, sender);
        binding.chatRecyclerView.setItemAnimator(null);
        binding.chatRecyclerView.setAdapter(adapter);
        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((MainActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ChatsListFragment())
                        .commit();
            }
        });

        binding.addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.messageEditText.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(context, "Введите текст!", Toast.LENGTH_SHORT).show();
                }
                else{
                    newMessage.setSender(sender);
                    newMessage.setReceiver(receiver);
                    newMessage.setText(message);
                        new VolleyAPI(context).addMessage(newMessage, new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                adapter.notifyDataSetChanged();
                                binding.messageEditText.setText("");
                                newMessage = new Message();
                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });
                    }
                }
        });

        binding.receiverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AnotherProfileFragment(receiver.getId(), AnotherProfileFragment.FROM_CHAT), "AnotherProfileFragment")
                        .commit();
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new VolleyAPI(context).findMessagesBySenderAndReceiver(sender.getId(), receiver.getId(), new VolleyCallback() {

                    @Override
                    public void onSuccess(JSONObject response) {
                        adapter.notifyDataSetChanged();
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                });
            }
        });
        return binding.getRoot();
    }
}