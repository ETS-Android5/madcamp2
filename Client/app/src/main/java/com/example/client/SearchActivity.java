package com.example.client;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private final String URL = "https://dapi.kakao.com/";
    private final String API_KEY = "KakaoAK 5c2c8b4f5e2f6a5f1a1c673de30c7bf8";

    private Retrofit retrofit;
    private SearchAPI searchAPI;

    private SearchView search;
    private RecyclerView recyclerView;

    private SearchAdapter searchAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_search);

        search =(SearchView) findViewById(R.id.search);
        search.setIconified(false);
        search.setPadding(100,0,0,0);


        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        searchAPI = retrofit.create(SearchAPI.class);


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAPI.getSearchData(API_KEY,search.getQuery().toString()).enqueue(new Callback<SearchDataClass>() {
                    @Override
                    public void onResponse(Call<SearchDataClass> call, Response<SearchDataClass> response) {
                        if(response.isSuccessful()){
                            Log.d("Test", "Raw: response.raw()");
                            Log.d("Test", new Gson().toJson(response.body()));

                            recyclerView = findViewById(R.id.search_recyclerview);
                            searchAdapter = new SearchAdapter(getApplicationContext(), response.body().getDocuments());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(searchAdapter);
                        }
                        else{
                            Log.w("MainActivity", "통신 실패: ${t.message}");
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchDataClass> call, Throwable t) {
                        Log.w("MainActivity", "통신 실패: ${t.message}");
                    }
                });
                return true;
            }

            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                searchAPI.getSearchData(API_KEY,search.getQuery().toString()).enqueue(new Callback<SearchDataClass>() {
                    @Override
                    public void onResponse(Call<SearchDataClass> call, Response<SearchDataClass> response) {
                        if(response.isSuccessful()){
                            Log.d("Test", "Raw: response.raw()");
                            Log.d("Test", new Gson().toJson(response.body()));

                            recyclerView = findViewById(R.id.search_recyclerview);
                            searchAdapter = new SearchAdapter(getApplicationContext(), response.body().getDocuments());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(searchAdapter);
                        }
                        else{
                            Log.w("MainActivity", "통신 실패: ${t.message}");
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchDataClass> call, Throwable t) {
                        Log.w("MainActivity", "통신 실패: ${t.message}");
                    }
                });
                return true;
            }
        });



    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
