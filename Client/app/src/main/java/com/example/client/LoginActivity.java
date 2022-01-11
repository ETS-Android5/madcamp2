package com.example.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    //private final String serverURL = "http://192.168.77.245/";
    private final String serverURL = "http://192.249.18.111/";
    private Context context;

    private Retrofit retrofit;
    private LoginServiceApi service;

    private Button loginBtn;
    private Button logOutBtn;
    private Button toSignupBtn;
    private EditText nameEditText;
    private EditText pwEditText;

    private TextView logInDebugText;

    static String token = "";
    static Integer id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;

        toSignupBtn = (Button) findViewById(R.id.logInSignInBtn);
        loginBtn = (Button) findViewById(R.id.logInBtn);
        logOutBtn = (Button) findViewById(R.id.logOutBtn);
        nameEditText = (EditText) findViewById(R.id.logInIdEditText);
        pwEditText = (EditText) findViewById(R.id.logInPasswordEditText);
        logInDebugText = (TextView) findViewById(R.id.login_debugText);

        retrofit = new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(LoginServiceApi.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> input = new HashMap<>();
                input.put("username",nameEditText.getText().toString());
                input.put("password",pwEditText.getText().toString());

                service.logInPost(input).enqueue(new Callback<LoginDataClass>() {
                    @Override
                    public void onResponse(Call<LoginDataClass> call, Response<LoginDataClass> response) {
                        if(response.isSuccessful()) {
                            token = response.body().getKey();
                            id = response.body().getUser();
                            logInDebugText.setText("로그인 중");
                            Intent intent_to_main = new Intent(LoginActivity.this,MainActivity.class);
                            intent_to_main.putExtra("CallType",2);
                            intent_to_main.putExtra("signin_state",0);
                            intent_to_main.putExtra("id",id);
                            intent_to_main.putExtra("username",nameEditText.getText().toString());

                            setResult(Activity.RESULT_OK, intent_to_main);
                            finish();
                        } else {
                            logInDebugText.setText("로그인 Fail");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginDataClass> call, Throwable t) {

                    }
                });
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> input = new HashMap<>();

                service.logOutPost(input).enqueue(new Callback<LoginDataClass>() {
                    @Override
                    public void onResponse(Call<LoginDataClass> call, Response<LoginDataClass> response) {
                        if(response.isSuccessful()) {
                            token = "";
                            id = -1;
                            logInDebugText.setText("로그아웃");
                        } else {
                            logInDebugText.setText("로그아웃 Fail");
                        }

                    }
                    @Override
                    public void onFailure(Call<LoginDataClass> call, Throwable t) {

                    }
                });
            }
        });

        toSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToSignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intentToSignup);
            }
        });
    }
}