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
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.uniorproject.databinding.ActivityRegisterBinding;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private String emailString;
    private String nameString;
    private String passwordString;
    private SharedPreferences sharedPreferences;
    private User newUser = new User();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("avatars");
    private Uri imageUri;
    private ImageView avatarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Picasso.with(this).setLoggingEnabled(true);
        avatarImage = findViewById(R.id.avatar_image);

        binding.buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailString = binding.emailEditText.getText().toString();
                nameString = binding.nameEditText.getText().toString();
                passwordString = binding.passwordEditText.getText().toString();
                sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

                if(!Objects.equals(emailString, "") && !Objects.equals(nameString, "") && !passwordString.equals("")){
                    new VolleyAPI(RegisterActivity.this).findUserByEmail(emailString, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            User user = UserMapper.userFromJson(response);
                            if(user.getEmail().equals(emailString)){
                                Toast.makeText(RegisterActivity.this, "Такой пользователь уже существует!", Toast.LENGTH_SHORT).show();
                            }
                            else if(passwordString.length() < 8){
                                Toast.makeText(RegisterActivity.this, "Пароль должен быть длиннее 8 символов!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            if(error.networkResponse.statusCode == 500){
                                newUser.setEmail(emailString);
                                newUser.setName(nameString);
                                newUser.setPassword(passwordString);

                                new VolleyAPI(RegisterActivity.this).addUser(newUser);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear().commit();
                                editor.putBoolean("unlogined", false);
                                editor.putInt("userId", newUser.getId());
                                editor.apply();

                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

        binding.avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
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
            Picasso.with(this).load(imageUri).fit().into(avatarImage);
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
                            newUser.setProfilePic(imageUri.toString());
                        }
                    });
                }
            });
        }
    }

}