package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {
    private final String URL = "http://192.249.18.111/";

    private Retrofit retrofit;
    private LoginServiceApi service;

    private Button signup;

    private TextView errorName, errorEmail, errorPW, errorPW2;

    private EditText username, email, password, password2;

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

        errorName = (TextView) findViewById(R.id.signup_usernameDanger);
        errorEmail = (TextView) findViewById(R.id.signup_emailDanger);
        errorPW = (TextView) findViewById(R.id.signup_pwDanger);
        errorPW2 = (TextView) findViewById(R.id.signup_pw2Danger);

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
                           Toast.makeText(SignupActivity.this, "Sign In Complete", Toast.LENGTH_SHORT).show();
                           Log.d("SignIn", "onResponse: 성공");
                           finish();
                       }
                       else{
                           try {
                               RestSignInError restError = (RestSignInError) retrofit.responseBodyConverter(
                                       RestSignInError.class, RestSignInError.class.getAnnotations()).convert(response.errorBody());
                               setWarningText(restError);
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }
                   }

                   @Override
                   public void onFailure(Call<LoginDataClass> call, Throwable t) {
                   }
               });

            }
        });
    }

    public void setWarningText(RestSignInError restError) {
        errorName.setText("");
        errorEmail.setText("");
        errorPW.setText("");
        errorPW2.setText("");

        List<String> nameError = restError.username;
        if(nameError != null) {
            errorName.setText(nameError.get(0));
        }

        List<String> emailError = restError.email;
        if(emailError != null) {
            errorEmail.setText(emailError.get(0));
        }

        List<String> pwError = restError.password1;
        if(pwError != null) {
            errorPW.setText(pwError.get(0));
        }

        List<String> pw2Error = restError.non_field_errors;
        if(pw2Error != null) {
            errorPW2.setText(pw2Error.get(0));
        }
    }

}

