package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private final String URL = "";

    private Retrofit retrofit;
    private ApiService service;

    private Button login;
    private Button toSignup;
    private EditText username;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_login);

        toSignup = (Button) findViewById(R.id.to_signup_button);
        toSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToSignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intentToSignup);
            }
        });
    }
}
