package com.example.uniorproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.uniorproject.databinding.ActivityLoginBinding;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String emailString;
    private String passwordString;
    private SharedPreferences sharedPreferences;
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    private static final int REQ_ONE_TAP = 2;
    private boolean showOneTapUI = true;
    private String nameString;


    ActivityResultCallback<ActivityResult> activityResult = new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK) {
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(o.getData());
                    String idToken = credential.getGoogleIdToken();
                    if (idToken != null) {
                        emailString = credential.getId();
                        new VolleyAPI(getApplicationContext()).findUserByEmail(emailString, new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("unlogined", false);
                                editor.putString("userEmail", emailString);
                                editor.apply();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            @Override
                            public void onError(VolleyError error) {
                                if (error.networkResponse.statusCode == 500) {
                                    Intent intent = new Intent(LoginActivity.this, UserStatsActivity.class);
                                    nameString = credential.getDisplayName();
                                    passwordString = "";
                                    intent.putExtra("emailString", emailString);
                                    intent.putExtra("nameString", nameString);
                                    intent.putExtra("passwordString", passwordString);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                } catch (ApiException e) {


                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        ActivityResultLauncher<IntentSenderRequest> intentSender =
                registerForActivityResult(
                        new ActivityResultContracts.StartIntentSenderForResult(),
                        activityResult);

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
                                editor.putString("userEmail", user.getEmail());
                                editor.apply();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Неверный пароль!", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onError(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "Аккаунт не найден!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, "Введите данные", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.buttonGoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(LoginActivity.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                IntentSenderRequest intentSenderRequest =
                                        new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                                intentSender.launch(intentSenderRequest);
                            }
                        })
                        .addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Tag", e.getLocalizedMessage());
                            }
                        });
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
