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

import com.android.volley.VolleyError;
import com.example.uniorproject.adapter.RecipeCreatorAdapter;
import com.example.uniorproject.databinding.ActivityMainBinding;
import com.example.uniorproject.domain.Day;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.fragment.CreateFragment;
import com.example.uniorproject.fragment.CreateRecipeGuideFragment;
import com.example.uniorproject.fragment.FeedFragment;
import com.example.uniorproject.fragment.PostFragment;
import com.example.uniorproject.fragment.ProfileFragment;
import com.example.uniorproject.fragment.RecipeFragment;
import com.example.uniorproject.fragment.SearchFragment;
import com.example.uniorproject.fragment.ShoppingListFragment;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Uri imageUri;
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference("recipePictures");
    private final StorageReference userStorageReference = FirebaseStorage.getInstance().getReference("avatars");

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
        new VolleyAPI(MainActivity.this).findUserByEmail(sp.getString("userEmail", ""), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                User user = UserMapper.userFromJson(response);
                changeDateIfNeeded(user);
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("recipeSharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("recipeKcal", 0);
        editor.putFloat("recipeProteins", 0);
        editor.putFloat("recipeFats", 0);
        editor.putFloat("recipeCarbohydrates", 0);
        editor.putFloat("recipeSugar", 0);
        editor.commit();
        new VolleyAPI(this).fillUser();
        new VolleyAPI(this).fillRecipe(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        });

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
                        case(R.id.nav_search):
                            fragment = new SearchFragment();
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
                    .replace(R.id.fragment_container, fragment, "UserProfile")
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();

            SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
            int position = sharedPreferences.getInt("position", 0);
            uploadPicture(position);

        }

        if(requestCode == 8 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            String fileName = System.currentTimeMillis() + "." + getFileExtension(imageUri);

            SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("userId", 0);
            String userName = sharedPreferences.getString("userName", "");
            String userEmail = sharedPreferences.getString("userEmail", "");
            String userPassword = sharedPreferences.getString("userPassword", "");
            String userStatus = sharedPreferences.getString("userStatus", "");
            int userKcal = sharedPreferences.getInt("userKcal", 0);
            int userProteins = sharedPreferences.getInt("userProteins", 0);
            int userFats = sharedPreferences.getInt("userFats", 0);
            int userCarbohydrates = sharedPreferences.getInt("userCarbohydrates", 0);
            long registrationDate = sharedPreferences.getLong("registrationDate", 0);

            StorageReference fileReference = userStorageReference.child(fileName);
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            new VolleyAPI(MainActivity.this).updateUser(userId, userName, userEmail, userPassword, userStatus, uri.toString(), userKcal, userProteins, userFats, userCarbohydrates, registrationDate);
                            ProfileFragment fragment = ((ProfileFragment)
                                    getSupportFragmentManager().findFragmentByTag("UserProfile"));
                            fragment.updateProfilePicture(uri);
                        }
                    });
                }
            });
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
                                fragment.updateAdapter(position);
                            }
                            catch (NullPointerException e){

                            }
                        }
                    });
                }
            });
        }
    }

    private void changeDateIfNeeded(User user) {
        SharedPreferences sp = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        long date = sp.getLong("date", 0);
        Calendar currentCalendar = Calendar.getInstance();
        Calendar oldCalendar = Calendar.getInstance();
        oldCalendar.setTimeInMillis(date);

        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        oldCalendar.set(Calendar.HOUR_OF_DAY, 0);
        oldCalendar.set(Calendar.MINUTE, 0);
        oldCalendar.set(Calendar.SECOND, 0);
        oldCalendar.set(Calendar.MILLISECOND, 0);

        long period = currentCalendar.getTimeInMillis() - oldCalendar.getTimeInMillis();
        long registrationPeriod = currentCalendar.getTimeInMillis() - user.getRegistrationDate();
        period /= 86400000;
        date = date + period * 86400000;
        int daysFromRegistration = (int) registrationPeriod / 86400000;
        e.putLong("date", date);
        e.apply();

        new VolleyAPI(MainActivity.this).findUserByEmail(sp.getString("userEmail", ""), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                User user = UserMapper.userFromJson(response);
                new VolleyAPI(MainActivity.this).findDaysByUserAndDay(user.getId(), daysFromRegistration, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                    }

                    @Override
                    public void onError(VolleyError error) {
                        new VolleyAPI(MainActivity.this).addDay(new Day(user, daysFromRegistration, 0, 0, 0, 0, false), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {

                            }

                            @Override
                            public void onError(@Nullable VolleyError error) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onError(VolleyError error) {

            }
        });



    }


}