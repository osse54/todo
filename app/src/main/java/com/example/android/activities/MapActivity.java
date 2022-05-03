package com.example.android.activities;

import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.example.android.R;
import com.example.android.util.MyUtils;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.*;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import org.jetbrains.annotations.NotNull;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText searchInputText;
    private ImageView locationSearchIcon;
    private ImageView searchGPSIcon;
    private Button getLocationCancelButton;
    private Button getLocationCompleteButton;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private String location;
    private LatLng latLng;
    private Marker marker;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.getLocationMapView);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.getLocationMapView, mapFragment).commit();
        }

        // onMapReady를 호출
        mapFragment.getMapAsync(this);

        assign();
    }

    @Override
    public void onMapReady(@NonNull @NotNull NaverMap naverMap) {
        this.naverMap = naverMap;
        if(naverMap.getOnMapLongClickListener() == null) {
            naverMap.setOnMapLongClickListener(new NaverMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(@NonNull @NotNull PointF pointF, @NonNull @NotNull LatLng latLng) {
                    MapActivity.this.latLng = latLng;
                    Intent intent = MyUtils.getReverseGeocode(latLng);
                    if(intent.hasExtra("address")) {
                        setLocation(intent.getStringExtra("address"));
                    }
                    mark();
                }
            });
        }
        mark();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    private void assign() {
        searchInputText = findViewById(R.id.searchInputText);
        searchInputText.setText("대암로 247"); // todo: 임시로 설정
        locationSearchIcon = findViewById(R.id.locationSearchIcon);
        searchGPSIcon = findViewById(R.id.searchGPSIcon);
        getLocationCancelButton = findViewById(R.id.getLocationCancelButton);
        getLocationCompleteButton = findViewById(R.id.getLocationCompleteButton);

        Intent intent = getIntent();
        if(intent.hasExtra("location")) {
            setLocation(intent.getStringExtra("location"));
        }
        if(intent.hasExtra("latLng")) {
            latLng = intent.getParcelableExtra("latLng");
        }
        setListener();
    }

    private void setListener() {
        locationSearchIcon.setOnClickListener(v -> {
            new Thread(() -> {
                MyUtils.HANDLER.post(() -> {
                    Intent intent = MyUtils.getGeocode(searchInputText.getText().toString());

                    if(intent.hasExtra("address")) {
                        setLocation(intent.getStringExtra("address"));
                    }

                    if(intent.getStringExtra("latitude") != null && intent.getStringExtra("longitude") != null) {
                        latLng = new LatLng(Double.parseDouble(intent.getStringExtra("latitude")), Double.parseDouble(intent.getStringExtra("longitude")));
                    }

                    mark();
                });
            }).start();
        });

        searchGPSIcon.setOnClickListener(v -> {
            locationSource.activate(new LocationSource.OnLocationChangedListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mark();
                    Intent intent = MyUtils.getReverseGeocode(latLng);
                    if(intent.hasExtra("address")) {
                        setLocation(intent.getStringExtra("address"));
                    }
                    locationSource.deactivate();
                }
            });
            naverMap.setLocationSource(locationSource);
        });

        getLocationCancelButton.setOnClickListener(v -> {
            onBackPressed();
        });

        getLocationCompleteButton.setOnClickListener(v -> {
            if(location == null) {
                MyUtils.alertDialog("검색된 위치 정보가 없습니다.", MapActivity.this);
            } else if(location.equals("")) {
                MyUtils.alertDialog("검색된 위치 정보가 없습니다.", MapActivity.this);
            } else {
                Intent intent = new Intent();
                intent.putExtra("latLng", latLng);
                intent.putExtra("location", location);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void mark() {
        if(latLng != null) {
            if(marker == null) {
                marker = new Marker();
            }
            marker.setPosition(latLng);
            marker.setMap(naverMap);
            naverMap.setCameraPosition(new CameraPosition(latLng, 15 > naverMap.getCameraPosition().zoom ? 15 : naverMap.getCameraPosition().zoom));
        }
    }

    private void setLocation(String location) {
        this.location = location;
        searchInputText.setText(location);
    }
}