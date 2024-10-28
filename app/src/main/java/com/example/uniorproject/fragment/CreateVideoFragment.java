package com.example.uniorproject.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.uniorproject.R;
import com.example.uniorproject.databinding.FragmentCreateVideoBinding;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.json.JSONObject;

public class CreateVideoFragment extends Fragment {

    private FragmentCreateVideoBinding binding;
    private SharedPreferences sharedPreferences;
    private Uri videoUri;
    private StorageReference storageReference;
    private final Post post = new Post();
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCreateVideoBinding.inflate(inflater);
        sharedPreferences = getActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        context = getContext();

        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = sharedPreferences.getString("userEmail", "");

                if(post.getPicture() != null && !binding.editText.getText().toString().isEmpty() && getActivity() != null) {
                    new VolleyAPI(context).findUserByEmail(email, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            User author = UserMapper.userFromJson(response);
                            post.setAuthor(author);
                            post.setText(binding.editText.getText().toString());
                            new VolleyAPI(context).addPost(post, new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    Toast.makeText(getContext(), "Опубликовано!", Toast.LENGTH_LONG).show();
                                    getParentFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_container, new PostFragment())
                                            .commit();
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
                }
                else{
                    Toast.makeText(context, "Введите данные", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.postVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        return binding.getRoot();
    }


    private void openFileChooser(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null){
            videoUri = data.getData();
            binding.postVideo.setVideoURI(videoUri);
            binding.postVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();

                    float videoRatio = mediaPlayer.getVideoWidth() / (float) mediaPlayer.getVideoHeight();
                    float screenRatio = 0.5f;

                    float scale = videoRatio / screenRatio;

                    if(scale >= 1f){
                        binding.postVideo.setScaleX(scale);
                    }
                    else{
                        binding.postVideo.setScaleY(1f / scale);
                    }
                }
            });
            binding.postVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            uploadPicture();

        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadPicture(){
        storageReference = FirebaseStorage.getInstance().getReference("videos");
        if(videoUri != null) {
            String fileName = System.currentTimeMillis() + "." + getFileExtension(videoUri);

            StorageReference fileReference = storageReference.child(fileName);
            fileReference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            post.setPicture(uri.toString());
                        }
                    });
                }
            });
        }
    }
}