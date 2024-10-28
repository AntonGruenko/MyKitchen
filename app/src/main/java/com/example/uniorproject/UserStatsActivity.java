package com.example.uniorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uniorproject.databinding.ActivityMainBinding;
import com.example.uniorproject.databinding.ActivityUserStatsBinding;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.rest.VolleyAPI;

import java.util.Calendar;

public class UserStatsActivity extends AppCompatActivity {

    ActivityUserStatsBinding binding;
    private int userHeight;
    private int userWeight;
    private boolean isUserMale;
    private int userAge;
    private double[] userActiveness;

    private int userProteins;
    private int userFats;
    private int userCarbohydrates;
    private int userGlasses;
    private int userCalories;

    private String userName;
    private String userEmail;
    private String userPassword;
    private String userProfilePic;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Bundle extras = getIntent().getExtras();

        userName = extras.getString("nameString");
        userEmail = extras.getString("emailString");
        userPassword = extras.getString("passwordString");
        userProfilePic = sharedPreferences.getString("userProfilePic", "");
        userActiveness = new double[1];

        binding.activenessRadio.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.activenessVariant1: userActiveness[0] = 1.2;
                    break;
                case R.id.activenessVariant2: userActiveness[0] = 1.38;
                    break;
                case R.id.activenessVariant3: userActiveness[0] = 1.46;
                    break;
                case R.id.activenessVariant4: userActiveness[0] = 1.55;
                    break;
                case R.id.activenessVariant5: userActiveness[0] = 1.64;
                    break;
                case R.id.activenessVariant6: userActiveness[0] = 1.73;
                    break;
                case R.id.activenessVariant7: userActiveness[0] = 1.9;
                    break;
            }
        });

        binding.genderRadio.setOnCheckedChangeListener(((radioGroup, i) -> {
            switch (i) {
                case R.id.female:
                    isUserMale = false;
                    break;
                case R.id.male:
                    isUserMale = true;
                    break;
            }
        }));

        binding.buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.editHeight.getText() != null
                        && binding.editWeight.getText() != null
                        && binding.editAge.getText() != null
                        && binding.genderRadio.getCheckedRadioButtonId() != -1
                        && binding.activenessRadio.getCheckedRadioButtonId() != -1) {
                    userHeight = Integer.parseInt(binding.editHeight.getText().toString());
                    userWeight = Integer.parseInt(binding.editWeight.getText().toString());
                    userAge = Integer.parseInt(binding.editAge.getText().toString());

                    if (isUserMale)
                        userCalories = (int) (((userWeight * 10) + (userHeight * 6.25) - (userAge * 5) + 5) * userActiveness[0]);
                    else
                        userCalories = (int) (((userWeight * 10) + (userHeight * 6.25) - (userAge * 5) - 161) * userActiveness[0]);

                    userProteins = (int) (userCalories * 0.3 / 4);
                    userFats = (int) (userCalories * 0.2 / 4.5);
                    userCarbohydrates = (int) (userCalories * 0.5 / 4);
                    userGlasses = (int) (userWeight * 0.15);
                    Calendar dateCalendar = Calendar.getInstance();

                    User newUser = new User(userName, userEmail, userPassword, "", userProfilePic, userCalories, userProteins, userFats, userCarbohydrates, dateCalendar.getTimeInMillis());
                    new VolleyAPI(UserStatsActivity.this).addUser(newUser);

                    editor.clear().apply();
                    editor.putBoolean("unlogined", false);
                    editor.putString("userEmail", userEmail);
                    editor.putLong("date", dateCalendar.getTimeInMillis());
                    editor.putLong("registrationDate", dateCalendar.getTimeInMillis());
                    editor.apply();

                    Intent intent = new Intent(UserStatsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(UserStatsActivity.this, "Введите данные!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}