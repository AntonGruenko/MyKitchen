package com.example.uniorproject.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.databinding.FragmentCreatePostBinding;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.firebase.Uploader;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import org.json.JSONObject;

public class CreatePostFragment extends Fragment {

    private static final int IMAGE_REQUEST = 1;
    private ImageView postImage;
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads");
    private Uri imageUri;
    private Post post;
    private SharedPreferences sharedPreferences;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCreatePostBinding binding = FragmentCreatePostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();

        sharedPreferences = getActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        post = new Post();
        post.setPicture("");
        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.postButton.setClickable(false);
                new VolleyAPI(context).findUserByEmail(sharedPreferences.getString("userEmail", ""), new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        post.setAuthor(UserMapper.userFromJson(response));
                        post.setText(binding.editText.getText().toString());
                        new VolleyAPI(getActivity()).addPost(post, new VolleyCallback() {
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
        });

        binding.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        return view;
    }

    private void openFileChooser(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            postImage = getActivity().findViewById(R.id.post_image);
            Glide.with(context).load(imageUri).centerCrop().into(postImage);
            postImage.setImageURI(imageUri);
            uploadPicture();

        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadPicture(){
        if(imageUri != null) {
            String fileName = System.currentTimeMillis() + "." + getFileExtension(imageUri);

            StorageReference fileReference = storageReference.child(fileName);
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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