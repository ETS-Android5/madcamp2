package com.example.client;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;

public class SignupActivity extends AppCompatActivity {
    private final String URL = "http://192.168.0.55:80/";

    private Retrofit retrofit;
    private ApiService service;

    private Button signup;
    private EditText username;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = (Button) findViewById(R.id.signup_button);
        username = (EditText) findViewById(R.id.signup_username);
        password = (EditText) findViewById(R.id.signup_password);
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> input = new HashMap<>();
                input.put("username","user1");
                input.put("email", "user1@gmail.com");
                input.put("password1","1234");
                input.put("password2","1234");

               service.postFunc(input).enqueue(new Callback<LoginDataClass>() {
                   @Override
                   public void onResponse(Call<LoginDataClass> call, Response<LoginDataClass> response) {
                       if(response.isSuccessful()){
                           LoginDataClass data = response.body();
                       }
                   }

                   @Override
                   public void onFailure(Call<LoginDataClass> call, Throwable t) {

                   }
               });

            }
        });
    }
}
