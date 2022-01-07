package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {
    private final String URL = "http://192.168.77.245:8000/";

    private Retrofit retrofit;
    private LoginServiceApi service;

    private Button signup;
    private TextView debugText;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_signup);

        signup = (Button) findViewById(R.id.signup_button);
        username = (EditText) findViewById(R.id.signup_usernameEdit);
        email = (EditText) findViewById(R.id.singup_emailEdit);
        password = (EditText) findViewById(R.id.singup_passwordEdit);
        password2 = (EditText) findViewById(R.id.singup_passwordEdit2);
        debugText = (TextView)findViewById(R.id.signup_debugText);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(LoginServiceApi.class);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> input = new HashMap<>();
                input.put("username",username.getText().toString());
                input.put("email", email.getText().toString());
                input.put("password1",password.getText().toString());
                input.put("password2",password2.getText().toString());

               service.signUpPost(input).enqueue(new Callback<LoginDataClass>() {
                   @Override
                   public void onResponse(Call<LoginDataClass> call, Response<LoginDataClass> response) {
                       if(response.isSuccessful()){

                           LoginDataClass result = response.body();
                           Toast.makeText(SignupActivity.this, "Sign In Complete", Toast.LENGTH_SHORT).show();
                           Log.d("SignIn", "onResponse: 성공");
                       }
                       else{
                           debugText.setText("SomeThing Wrong");
                       }
                   }

                   @Override
                   public void onFailure(Call<LoginDataClass> call, Throwable t) {
                        debugText.setText(t.getMessage());
                   }
               });

            }
        });
    }
}
