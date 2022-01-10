package com.example.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
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
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private SearchFragment fragment;
    private SearchRecyclerviewFragment recyclerviewFragment;
    private SearchView search;
    private RecyclerView recyclerView;
    private Chip chipAll;
    private Chip chipRestaurant;
    private Chip chipCafe;
    private Chip chipCulture;
    private Switch toggleSwitch;
    private String currentX;
    private String currentY;
    private int searchMode = 0;
    private Button backToMainButton;

    private SearchAdapter searchAdapter;

    private String searchCategory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_search);


        backToMainButton = (Button) findViewById(R.id.backToMainbutton);

        currentX = getIntent().getExtras().getString("x");
        currentY = getIntent().getExtras().getString("y");

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
                getSearchData();
                return true;
            }

            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                getSearchData();
                return true;
            }
        });

        chipAll = (Chip) findViewById(R.id.chip_all);
        chipRestaurant=(Chip) findViewById(R.id.chip_restaurant);
        chipCafe = (Chip) findViewById(R.id.chip_cafe);
        chipCulture = (Chip) findViewById(R.id.chip_culture);

        chipAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCategory="";
                if(searchMode == 0) {
                    getSearchData();
                }

            }
        });
        chipRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCategory="FD6";
                if(searchMode == 0) {
                    getSearchData();
                }
            }
        });
        chipCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCategory="CE7";
                if(searchMode == 0) {
                    getSearchData();
                }
            }
        });
        chipCulture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCategory="CT1";
                if(searchMode == 0) {
                    getSearchData();
                }
            }
        });

        fragmentManager = getSupportFragmentManager();
        recyclerviewFragment = new SearchRecyclerviewFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainerView, recyclerviewFragment).commitAllowingStateLoss();
        toggleSwitch = (Switch) findViewById(R.id.search_toggle_switch);
        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    searchMode = 1;
                    Log.i("토글","on");
                    fragmentManager = getSupportFragmentManager();
                    fragment = new SearchFragment();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, fragment).commitAllowingStateLoss();

                }else{
                    searchMode = 0;
                    Log.i("토글","off");
                    fragmentManager = getSupportFragmentManager();
                    recyclerviewFragment = new SearchRecyclerviewFragment();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, recyclerviewFragment).commitAllowingStateLoss();
                    getSearchData();
                }
            }
        });

        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

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

    public void getSearchData()
    {
        Log.e("currentX",currentX);
        Log.e("currentY",currentY);
        searchAPI.getSearchData(API_KEY, search.getQuery().toString(), searchCategory,currentX,currentY).enqueue(new Callback<SearchDataClass>() {
            @Override
            public void onResponse(Call<SearchDataClass> call, Response<SearchDataClass> response) {
                if (response.isSuccessful()) {
                    Log.d("Test", "Raw: response.raw()");
                    Log.d("Test", new Gson().toJson(response.body()));

                    recyclerView = findViewById(R.id.searchRecyclerview);
                    searchAdapter = new SearchAdapter(getApplicationContext(), response.body().getDocuments());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                    recyclerView.setAdapter(searchAdapter);
                } else {
                    Log.w("MainActivity", "통신 실패: ${t.message}");
                }
            }

            @Override
            public void onFailure(Call<SearchDataClass> call, Throwable t) {
                Log.w("MainActivity", "통신 실패: ${t.message}");
            }
        });
    }
}
