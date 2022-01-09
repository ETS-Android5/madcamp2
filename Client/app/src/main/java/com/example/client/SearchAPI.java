package com.example.client;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SearchAPI {
    @GET("v2/local/search/keyword.json")
    Call<SearchDataClass> getSearchData(
            @Header("Authorization") String key,
            @Query("query") String query,
            @Query("category_group_code") String category_group_code

    );

    @FormUrlEncoded
    @POST("/rest-auth/registration/")
    Call<LoginDataClass> postFunc(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @PUT("/retrofit/put/{id}")
    Call<ResponseBody> putFunc(@Path("id") String id, @Field("data") String data);

    @DELETE("/retrofit/delete/{id}")
    Call<ResponseBody> deleteFunc(@Path("id") String id);
}
