package com.example.client;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import static com.example.client.LoginActivity.token;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {

    private String pointAddress, city, county, last;

    private TextView addressText;
    private ImageButton reviewSendBtn;
    private ChipGroup chipGroupSight, chipGroupTaste, chipGroupTouch;
    private Chip chip;

    private String[] sightList = {"RED", "GREEN", "BLUE", "YELLOW", "WHITE", "BLACK", "PURPLE", "ORANGE", "PINK", "BROWN"};
    private String[] touchList = {"SOFT", "HARD", "COOL", "WARM", "HOT", "COLD"};
    private String[] tasteList = {"SWEET", "SALTY", "BITTER", "SOUR", "NUTTY", "HOT"};

    private Retrofit retrofit;
    private ReviewServiceApi reviewServiceApi;
    //private final String serverURL = "http://192.168.77.245/";
    private final String serverURL = "http://192.249.18.111/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_add_information);

        addressText = (TextView) findViewById(R.id.reviewAddressTextView);
        reviewSendBtn = (ImageButton) findViewById(R.id.sendReviewBtn);
        pointAddress = getIntent().getExtras().getString("pointAdress");
        String[] splits = pointAddress.split("\\s", 3);
        city = splits[0];
        county = splits[1];
        last = splits[2];
        addressText.setText(pointAddress);

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

        //chipGroupSight.removeAllViews();
        chipGroupSight = findViewById(R.id.chipGroupSight);
        for(int i = 0; i < sightList.length; ++i) {
            chip = (Chip)this.getLayoutInflater().inflate(R.layout.layout_chip_choice, null, false);
            chip.setText(sightList[i]);
            chip.setId(i);

            chipGroupSight.addView(chip);
        }

        //chipGroupTouch.removeAllViews();
        chipGroupTouch = findViewById(R.id.chipGroupTouch);
        for(int i = 0; i < touchList.length; ++i) {
            chip = (Chip)this.getLayoutInflater().inflate(R.layout.layout_chip_choice, null, false);
            chip.setText(touchList[i]);
            chip.setId(i);
            chipGroupTouch.addView(chip);
        }

        //chipGroupTaste.removeAllViews();
        chipGroupTaste = findViewById(R.id.chipGroupTaste);
        for(int i = 0; i < tasteList.length; ++i) {
            chip = (Chip)this.getLayoutInflater().inflate(R.layout.layout_chip_choice, null, false);
            chip.setText(tasteList[i]);
            chip.setId(i);
            chipGroupTaste.addView(chip);
        }

        reviewSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Area area = new Area(city, county, last);

                int sightIdx = chipGroupSight.getCheckedChipId();
                int touchIdx = chipGroupTouch.getCheckedChipId();
                int tasteIdx = chipGroupTaste.getCheckedChipId();

                Log.d("ChipCheck", Integer.toString(sightIdx));
                Log.d("ChipCheck", Integer.toString(touchIdx));
                Log.d("ChipCheck", Integer.toString(tasteIdx));

                Review review = new Review(area, sightIdx, touchIdx, tasteIdx);

                reviewServiceApi.reviewPost(review).enqueue(new Callback<Review>() {
                    @Override
                    public void onResponse(Call<Review> call, Response<Review> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(AddActivity.this, "Area Add Success", Toast.LENGTH_SHORT);
                            finish();
                        } else {
                            Toast.makeText(AddActivity.this, "Area Add Fail", Toast.LENGTH_SHORT);
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



    }
}
