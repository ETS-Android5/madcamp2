package com.example.client;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/retrofit/get")
    Call<ResponseBody> getFunc(@Query("data") String data);

    @FormUrlEncoded
    @POST("/rest-auth/registration/")
    Call<LoginDataClass> postFunc(@Field("username") String name, @Field("email") String email,
                                  @Field("password1") String password1, @Field("password2") String password2);

    @FormUrlEncoded
    @PUT("/retrofit/put/{id}")
    Call<ResponseBody> putFunc(@Path("id") String id, @Field("data") String data);

    @DELETE("/retrofit/delete/{id}")
    Call<ResponseBody> deleteFunc(@Path("id") String id);
}