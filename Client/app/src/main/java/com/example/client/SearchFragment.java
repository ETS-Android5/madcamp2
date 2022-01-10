package com.example.client;

import static com.example.client.LoginActivity.token;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    private String[] sightList = {"RED", "GREEN", "BLUE", "YELLOW", "WHITE", "BLACK", "PURPLE", "ORANGE", "PINK", "BROWN"};
    private String[] touchList = {"SOFT", "HARD", "COOL", "WARM", "HOT", "COLD"};
    private String[] tasteList = {"SWEET", "SALTY", "BITTER", "SOUR", "NUTTY", "HOT"};

    private String address, city, county, last;

    private TextView addressText;
    private Button querySendBtn;
    private ChipGroup chipGroupSight, chipGroupTaste, chipGroupTouch;
    private Chip chip;

    private Retrofit retrofit;
    private ReviewServiceApi reviewServiceApi;
    private final String serverURL = "http://192.168.77.245/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("FragmentLifeCycle", "OnCreateView");
        View view = inflater.inflate(R.layout.fragment_mydbsearch, container, false);
        if(getArguments() != null) {
            address = getArguments().getString("address");
        }

        setViewsById(view);
        setViewInfo();
        retrofitSet();

        querySendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Area area = new Area(city, county, last);

                int sightIdx = chipGroupSight.getCheckedChipId();
                int touchIdx = chipGroupTouch.getCheckedChipId();
                int tasteIdx = chipGroupTaste.getCheckedChipId();

                Review review = new Review(area, sightIdx, touchIdx, tasteIdx);

                reviewServiceApi.searchSensePost(review).enqueue(new Callback<Review>() {
                    @Override
                    public void onResponse(Call<Review> call, Response<Review> response) {
                        if(response.isSuccessful()) {
                            List<Area> result = response.body().getResult();
                            for(int i= 0; i < result.size(); ++i) {
                                Log.d("Area Check", result.get(i).getAddress());
                            }
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "Area Add Fail", Toast.LENGTH_SHORT);
                            try {
                                Log.d("AreaAddRespone", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Review> call, Throwable t) {
                        Log.d("AreaAddRespone", "something wrong");
                    }
                });
            }
        });

        return view;
    }

    private void setViewsById(View view) {

        Log.d("FragmentLifeCycle", "setViewsById");
        addressText = (TextView)view.findViewById(R.id.searchAddressTextView);
        querySendBtn = (Button)view.findViewById(R.id.sendSearchQueryBtn);

        chipGroupSight = (ChipGroup)view.findViewById(R.id.chipGroupSearchSight);
        chipGroupTouch = (ChipGroup)view.findViewById(R.id.chipGroupSearchTouch);
        chipGroupTaste = (ChipGroup)view.findViewById(R.id.chipGroupSearchTaste);
    }

    private void setViewInfo() {
        Log.d("FragmentLifeCycle", "setViewInfo");

        if(address != null && addressText != null ) {
            addressText.setText(address);

            String[] splits = address.split("\\s", 3);
            city = splits[0];
            county = splits[1];
            last = splits[2];
        }

        for(int i = 0; i < sightList.length; ++i) {
            chip = (Chip)this.getLayoutInflater().inflate(R.layout.layout_chip_choice, null, false);
            chip.setText(sightList[i]);
            chip.setId(i);

            chipGroupSight.addView(chip);
        }

        for(int i = 0; i < touchList.length; ++i) {
            chip = (Chip)this.getLayoutInflater().inflate(R.layout.layout_chip_choice, null, false);
            chip.setText(touchList[i]);
            chip.setId(i);
            chipGroupTouch.addView(chip);
        }

        for(int i = 0; i < tasteList.length; ++i) {
            chip = (Chip)this.getLayoutInflater().inflate(R.layout.layout_chip_choice, null, false);
            chip.setText(tasteList[i]);
            chip.setId(i);
            chipGroupTaste.addView(chip);
        }
    }

    private void retrofitSet() {
        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create());

        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest;
                String key = " Token " + token;
                newRequest = chain.request().newBuilder().addHeader("Authorization", key).build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();
        retroBuilder.client(client);

        retrofit = retroBuilder.build();
        reviewServiceApi = retrofit.create(ReviewServiceApi.class);
    }
}
