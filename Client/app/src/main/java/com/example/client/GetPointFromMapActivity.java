package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

public class GetPointFromMapActivity extends AppCompatActivity {

    private Button getAddressBtn;
    private MapView mapView;
    private ViewGroup mapViewContainer;

    private String APPKEY = "002448364a9b04e2bdfdad9d74c9ce5a";
    private MapReverseGeoCoder mMapReverseGeoCoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_point_from_map);

        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.getCenterMapView);

        mapView.setShowCurrentLocationMarker(true);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.custom_location_marker, new MapPOIItem.ImageOffset(30, 30));

        mapViewContainer.addView(mapView);

        getAddressBtn = (Button)findViewById(R.id.getAddressBtn);
        getAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPoint point = mapView.getMapCenterPoint();
                mMapReverseGeoCoder = new MapReverseGeoCoder(APPKEY, point, new MapReverseGeoCoder.ReverseGeoCodingResultListener() {
                    @Override
                    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
                        Intent intent = new Intent();
                        intent.putExtra("Address", s);

                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
                        Toast.makeText(GetPointFromMapActivity.this, "Failed to get Address, Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }, GetPointFromMapActivity.this);

                mMapReverseGeoCoder.startFindingAddress();
            }
        });
    }
}