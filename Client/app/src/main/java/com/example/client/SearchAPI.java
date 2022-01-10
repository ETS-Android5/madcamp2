package com.example.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface SearchAPI {
    @GET("v2/local/search/keyword.json")
    Call<SearchDataClass> getSearchData(
            @Header("Authorization") String key,
            @Query("query") String query,
            @Query("category_group_code") String category_group_code

    );
}
