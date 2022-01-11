package com.example.client;

import static android.app.Activity.RESULT_OK;
import static com.example.client.LoginActivity.token;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.util.ArrayList;
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
    private Button querySendBtn, addressChangeBtn;
    private ChipGroup chipGroupSight, chipGroupTaste, chipGroupTouch;
    private Chip chip;

    private Retrofit retrofit;
    private ReviewServiceApi reviewServiceApi;
    //private final String serverURL = "http://192.168.77.245/";
    private final String serverURL = "http://192.249.18.111/";

    private ActivityResultLauncher<Intent> resultLauncher;

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
                            Log.d("Address Response", "Now get response!");
                            List<Area> result = response.body().getResult();
                            ArrayList<String> addressList = new ArrayList<>();
                            for(int i= 0; i < result.size(); ++i) {
                                addressList.add(result.get(i).getAddress());
                                Log.d("Address List", addressList.get(i));
                            }
                            Intent intent = new Intent();
                            intent.putExtra("CallType", 0);
                            intent.putExtra("address", addressList);
                            getActivity().setResult(RESULT_OK, intent);
                            Log.d("LifeCycleCheck", "Before getActivity");
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

        addressChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GetPointFromMapActivity.class);
                resultLauncher.launch(intent);
            }
        });

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    address = intent.getStringExtra("Address");
                    addressText.setText(address);
                    String[] splits = address.split("\\s", 3);
                    city = splits[0];
                    county = splits[1];
                    last = splits[2];
                } else {
                    Toast.makeText(getActivity(), "Something wrong with activity back!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void setViewsById(View view) {

        Log.d("FragmentLifeCycle", "setViewsById");
        addressText = (TextView)view.findViewById(R.id.searchAddressTextView);
        querySendBtn = (Button)view.findViewById(R.id.sendSearchQueryBtn);
        addressChangeBtn = (Button)view.findViewById(R.id.searchAddressChangeBtn);
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
