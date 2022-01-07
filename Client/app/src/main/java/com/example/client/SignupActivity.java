package com.example.client;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private EditText password2;
    private EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_signup);

        signup = (Button) findViewById(R.id.signup_button);
        username = (EditText) findViewById(R.id.signup_username);
        password = (EditText) findViewById(R.id.signup_password);
        password2 = (EditText) findViewById(R.id.password2);
        email = (EditText) findViewById(R.id.email);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> input = new HashMap<>();
                input.put("username",username.getText().toString());
                input.put("email", email.getText().toString());
                input.put("password1",password.getText().toString());
                input.put("password2",password2.getText().toString());

               service.postFunc(input).enqueue(new Callback<LoginDataClass>() {
                   @Override
                   public void onResponse(Call<LoginDataClass> call, Response<LoginDataClass> response) {
                       if(response.isSuccessful()){

                       }
                       else{

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
