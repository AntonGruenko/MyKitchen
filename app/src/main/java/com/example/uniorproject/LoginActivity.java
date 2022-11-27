package com.example.uniorproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.uniorproject.databinding.ActivityLoginBinding;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String emailString;
    private String passwordString;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailString = binding.emailEditText.getText().toString();
                passwordString = binding.passwordEditText.getText().toString();

                if(!(emailString.equals("") || passwordString.equals(""))) {

                    new VolleyAPI(getApplicationContext()).findUserByEmail(emailString, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            User user = UserMapper.userFromJson(response);
                            if (user.getPassword().equals(passwordString)) {
                                SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("unlogined", false);
                                editor.putInt("userId", user.getId());
                                editor.apply();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Неверный пароль!", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onError(VolleyError error) {

                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, "Введите данные", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
