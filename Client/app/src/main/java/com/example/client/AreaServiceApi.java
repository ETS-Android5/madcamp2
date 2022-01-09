package com.example.client;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AreaServiceApi {
    @FormUrlEncoded
    @POST("areas/")
    Call<AreaDataClass> areaPost(@FieldMap HashMap<String, Object> param);
}
