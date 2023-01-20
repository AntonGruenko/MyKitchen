package com.example.uniorproject.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

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

import com.example.uniorproject.R;
import com.example.uniorproject.databinding.FragmentCreateRecipeBinding;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CreateRecipeFragment extends Fragment {
    private Uri imageUri;
    private ImageView recipeImage;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("recipePictures");
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCreateRecipeBinding binding = FragmentCreateRecipeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        NoDb.PICTURE_LINK_LIST.add("");
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.editTitle.getText().toString().equals("")
                        || binding.editTimeHours.getText().toString().equals("")
                        || binding.editTimeMinutes.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Введите данные!", Toast.LENGTH_LONG).show();
                }
                else{
                    String recipeName = binding.editTitle.getText().toString();
                    int recipeTime = Integer.parseInt(binding.editTimeHours.getText().toString()) * 60
                            + Integer.parseInt(binding.editTimeMinutes.getText().toString());
                    String recipeRecommendations = binding.editRecommendations.getText().toString();
                    int recipeComplexity = (int) binding.complexity.getRating();

                    sharedPreferences = getActivity().getSharedPreferences("recipeSharedPreferences", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("recipeName", recipeName);
                    editor.putInt("recipeTime", recipeTime);
                    editor.putString("recipeRecommendations", recipeRecommendations);
                    editor.putInt("recipeComplexity", recipeComplexity);
                    editor.apply();

                    CreateRecipeIngredientsFragment fragment = new CreateRecipeIngredientsFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.create_container, fragment, "setIngredients")
                            .commit();
                }
            }
        });

        binding.recipeImage.setOnClickListener(new View.OnClickListener() {
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
            recipeImage = getActivity().findViewById(R.id.recipe_image);
            Picasso.with(getContext()).load(imageUri).fit().into(recipeImage);
            recipeImage.setImageURI(imageUri);
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
            sharedPreferences = getActivity().getSharedPreferences("recipeSharedPreferences", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            StorageReference fileReference = storageReference.child(fileName);
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            editor.putString("mainPicture", uri.toString());
                            editor.apply();
                        }
                    });
                }
            });
        }
    }
}