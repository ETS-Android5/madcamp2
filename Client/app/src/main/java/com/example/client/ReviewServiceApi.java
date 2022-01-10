package com.example.client;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReviewServiceApi {
    @POST("reviews/")
    Call<Review> reviewPost(@Body Review param);
}
