package com.example.uniorproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.uniorproject.databinding.ActivityRegisterBinding;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.json.JSONObject;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private String emailString;
    private String nameString;
    private String passwordString;
    private String secondPasswordString;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final User newUser = new User();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference("avatars");
    private Uri imageUri;
    private ImageView avatarImage;
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Glide.with(this);
        avatarImage = findViewById(R.id.avatar_image);

        binding.buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailString = binding.emailEditText.getText().toString();
                nameString = binding.nameEditText.getText().toString();
                passwordString = binding.passwordEditText.getText().toString();
                secondPasswordString = binding.secondPasswordEditText.getText().toString();
                sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                if(!Objects.equals(emailString, "") && !Objects.equals(nameString, "") && !passwordString.equals("")){
                    if(passwordString.length() < 8){
                        Toast.makeText(RegisterActivity.this, "Пароль должен быть длиннее 8 символов!", Toast.LENGTH_SHORT).show();
                    }
                    else if(!passwordString.equals(secondPasswordString)){
                        Toast.makeText(RegisterActivity.this, "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                    }
                    else if(!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
                        Toast.makeText(RegisterActivity.this, "Некорректный email!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        new VolleyAPI(RegisterActivity.this).findUserByEmail(emailString, new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                User user = UserMapper.userFromJson(response);
                                if (user.getEmail().equals(emailString)) {
                                    Toast.makeText(RegisterActivity.this, "Такой пользователь уже существует!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                if (error.networkResponse.statusCode == 500) {

                                    Intent intent = new Intent(RegisterActivity.this, UserStatsActivity.class);

                                    intent.putExtra("emailString", emailString);
                                    intent.putExtra("nameString", nameString);
                                    intent.putExtra("passwordString", passwordString);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }
            }
        });

        binding.avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, UserStatsActivity.class);
                startActivity(loginIntent);
            }
        });
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
            avatarImage = findViewById(R.id.avatar_image);
            Glide.with(this).load(imageUri).centerCrop().into(avatarImage);
            uploadPicture();

        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
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
                            sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("userProfilePic", uri.toString()).apply();
                        }
                    });
                }
            });
        }
    }

}