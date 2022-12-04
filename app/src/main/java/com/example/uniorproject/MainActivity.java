package com.example.uniorproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.example.uniorproject.adapter.RecipeCreatorAdapter;
import com.example.uniorproject.databinding.ActivityMainBinding;
import com.example.uniorproject.fragment.CreateFragment;
import com.example.uniorproject.fragment.CreateRecipeGuideFragment;
import com.example.uniorproject.fragment.FeedFragment;
import com.example.uniorproject.fragment.PostFragment;
import com.example.uniorproject.fragment.ProfileFragment;
import com.example.uniorproject.fragment.RecipeFragment;
import com.example.uniorproject.fragment.ShoppingListFragment;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Uri imageUri;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("recipePictures");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        boolean firstLaunch = sp.getBoolean("unlogined", true);
        if (firstLaunch) {
            Intent firstLaunchIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(firstLaunchIntent);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new VolleyAPI(this).fillUser();
        new VolleyAPI(this).fillRecipe();

        changeFragment(new FeedFragment());

        binding.bottomNavigation.setOnItemSelectedListener(
                item -> {
                    Fragment fragment = null;
                    switch (item.getItemId()){
                        case(R.id.nav_feed):
                            fragment = new FeedFragment();
                            break;
                        case(R.id.nav_shopping_list):
                            fragment = new ShoppingListFragment();
                            break;
                        case(R.id.nav_create):
                            fragment = new CreateFragment();
                            break;
                        case(R.id.nav_posts):
                            fragment = new PostFragment();
                            break;
                        case(R.id.nav_profile):
                            fragment = new ProfileFragment();
                            break;
                    }
                    changeFragment(fragment);
                    return true;
                }
        );
    }

    private boolean changeFragment(Fragment fragment) {
        if (fragment == null) {
            return false;
        } else if(fragment.getClass() == FeedFragment.class){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, "RecipeFeed")
                    .commit();
            return true;
        } else if(fragment.getClass() == PostFragment.class){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, "PostFeed")
                    .commit();
            return true;
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
    }

    public void updateRecipeAdapter(){
        FeedFragment fragment =((FeedFragment)
                getSupportFragmentManager().findFragmentByTag("RecipeFeed"));
        fragment.updateAdapter();
    }

    public void updateCurrentRecipeAdapter(){
        RecipeFragment fragment = ((RecipeFragment)
                getSupportFragmentManager().findFragmentByTag("RecipeFragment"));
        fragment.updateAdapter();
    }

    public void updatePostAdapter(){
        PostFragment fragment = ((PostFragment)
        getSupportFragmentManager().findFragmentByTag("PostFeed"));
        fragment.updateAdapter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();
            SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
            int position = sharedPreferences.getInt("position", 0);
            uploadPicture(position);


        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadPicture(int position){
        if(imageUri != null) {
            String fileName = System.currentTimeMillis() + "." + getFileExtension(imageUri);
            RecipeCreatorAdapter adapter = new RecipeCreatorAdapter(MainActivity.this, NoDb.GUIDE_LIST, 2);
            StorageReference fileReference = storageReference.child(fileName);
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            NoDb.PICTURE_LINK_LIST.set(position, uri.toString());
                            try {
                                CreateRecipeGuideFragment fragment = ((CreateRecipeGuideFragment)
                                        getSupportFragmentManager().findFragmentByTag("setGuide"));
                                fragment.updateAdapter();
                            }
                            catch (NullPointerException e){

                            }
                        }
                    });
                }
            });
        }
    }


}