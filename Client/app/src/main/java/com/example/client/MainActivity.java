package com.example.client;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener,MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener{

    private String APPKEY = "002448364a9b04e2bdfdad9d74c9ce5a";
    private DrawerLayout drawerLayout;
    private View drawerView;
    private Button openMenuButton;
    private Button toLogin;
    private Button search;
    private String pointAdrress;
    private String x;
    private String y;

    public static final int MULTIPLE_PERMISSIONS = 1801;
    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    MapReverseGeoCoder reverseGeoCoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        checkPermissions();




        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);


        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        mapView.setShowCurrentLocationMarker(true);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mapViewContainer.addView(mapView);

        //getHashKey();




            Intent intent = getIntent();
            Bundle extra= intent.getExtras();
            if( extra != null) {
                Log.i("intent","받음");
                x = intent.getExtras().getString("x");
                y = intent.getExtras().getString("y");
                Log.e("x",x);
                Log.e("y",y);
                System.out.println(x);
                System.out.println(y);
                double a = Double.parseDouble(x);
                double b = Double.parseDouble(y);
                //y = Double.parseDouble(intent.getExtras().getString("y"));

                MapPOIItem customMarker = new MapPOIItem();
                customMarker.setItemName("Custom Marker");
                customMarker.setTag(1);
                customMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(b,a));
                customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                customMarker.setCustomImageResourceId(R.drawable.pin_blue); // 마커 이미지.
                customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
                customMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                customMarker.setCustomSelectedImageResourceId(R.drawable.pin);

                mapView.removeAllPOIItems();
                mapView.addPOIItem(customMarker);
            }




        drawerLayout = (DrawerLayout) findViewById(R.id.mainLayout);
        drawerView = (View) findViewById((R.id.drawerView));
        drawerLayout.addDrawerListener(listener);

        openMenuButton = (Button) findViewById(R.id.open_menu_button);
        openMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(drawerView);
            }
        });

        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("searchview","searchview 클릭");
                Intent intent_to_search = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent_to_search);
                overridePendingTransition(R.anim.fadein, R.anim.none);

            }
        });

    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            toLogin = (Button) findViewById(R.id.to_login);
            toLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                Intent intent_to_login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent_to_login);

                }
            });
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };


    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);

            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    private void showNoPermissionToastAndFinish() {

        Toast toast = Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT);
        toast.show();

        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                boolean isDeny = false;
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            //permission denyed
                            isDeny = true;
                        }
                    }
                }

                if (isDeny) {
                    showNoPermissionToastAndFinish();
                }
            }
        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        Log.i("디테일로그", "onMapViewInitialized");
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        Log.i("디테일로그", "onMapViewCenterPointMoved");
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        Log.i("디테일로그", "onMapViewZoomLevelChanged");
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        Log.i("디테일로그", "onMapViewSingleTapped");
        mapView.removeAllPOIItems();
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        Log.i("디테일로그", "onMapViewDoubleTapped");
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        MapPOIItem customMarker = new MapPOIItem();
        customMarker.setItemName("Custom Marker");
        customMarker.setTag(1);
        customMarker.setMapPoint(mapPoint);
        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker.setCustomImageResourceId(R.drawable.pin_blue); // 마커 이미지.
        customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        customMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
        customMarker.setCustomSelectedImageResourceId(R.drawable.pin);

        mapView.removeAllPOIItems();
        mapView.addPOIItem(customMarker);

        MapReverseGeoCoder mapGeoCoder = new MapReverseGeoCoder( APPKEY, mapPoint, this, this );
        mapGeoCoder.startFindingAddress( );
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        Log.i("디테일로그", "onMapViewDragStarted");
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        Log.i("디테일로그", "onMapViewDragEnded");
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        Log.i("디테일로그", "onMapViewMoveFinished");
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        MapReverseGeoCoder mapGeoCoder = new MapReverseGeoCoder( APPKEY, mapPOIItem.getMapPoint(), this, this );
        mapGeoCoder.startFindingAddress( );
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        Intent intent_to_add = new Intent(MainActivity.this, AddActivity.class);
        intent_to_add.putExtra("pointAdress",pointAdrress);
        startActivity(intent_to_add);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {


    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        Log.i("검색된 주소",  s);
        pointAdrress = s;
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

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



    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            Log.i("디테일로그", "CustomCalloutBallonAdapter");
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            Log.i("디테일로그", "getCalloutBalloon");

            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {

            return null;
        }
    }
}


