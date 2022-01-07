package com.example.client;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Retrofit;

public class SignupActivity extends AppCompatActivity {
    private final String URL = "";

    private Retrofit retrofit;
    private ApiService service;

    private Button signup;
    private EditText username;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


    }
}
