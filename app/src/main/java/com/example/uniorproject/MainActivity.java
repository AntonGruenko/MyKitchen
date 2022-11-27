package com.example.uniorproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.uniorproject.databinding.ActivityMainBinding;
import com.example.uniorproject.fragment.CreateFragment;
import com.example.uniorproject.fragment.FeedFragment;
import com.example.uniorproject.fragment.PostFragment;
import com.example.uniorproject.fragment.ProfileFragment;
import com.example.uniorproject.fragment.ShoppingListFragment;
import com.example.uniorproject.rest.VolleyAPI;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

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

    public void updatePostAdapter(){
        PostFragment fragment = ((PostFragment)
        getSupportFragmentManager().findFragmentByTag("PostFeed"));
        fragment.updateAdapter();
    }


}