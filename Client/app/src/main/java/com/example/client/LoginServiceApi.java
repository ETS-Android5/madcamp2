package com.example.client;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LoginServiceApi {
    @FormUrlEncoded
    @POST("/rest-auth/registration/")
    Call<LoginDataClass> signUpPost(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/rest-auth/login/")
    Call<LoginDataClass> logInPost(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/rest-auth/logout/")
    Call<LoginDataClass> logOutPost(@FieldMap HashMap<String, Object> param);

}