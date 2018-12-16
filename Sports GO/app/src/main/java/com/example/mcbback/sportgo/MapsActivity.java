package com.example.mcbback.sportgo;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button btn_back;
    private EditText map_address;
    private Button btn_search;
    private Button btn_select;
    private String place_address=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_maps);
        map_address = (EditText) findViewById(R.id.map_address);
        btn_back= (Button)findViewById(R.id.map_back);
        btn_search = (Button)findViewById(R.id.map_search);
        btn_select = (Button)findViewById(R.id.map_select);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("Address");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                List<Address> addressList = null;
                try {
                    addressList = geocoder.getFromLocation(latitude,longitude,1); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }


                String address = addressList.get(0).getAddressLine(0); // 주소
                System.out.println(address);


                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(address);
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                mMap.clear();
                mMap.addMarker(mOptions);
                place_address = address;
            }
        });
        btn_search.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str=map_address.getText().toString();
                if(str.length() ==0){
                    Toast.makeText(MapsActivity.this, "Please, set the keyword", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Address> addressList = null;
                try {
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if(addressList.size()==0){
                    Toast.makeText(MapsActivity.this, "No search results", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 콤마를 기준으로 split
                for (int i =0; i < addressList.size(); i++){
                    String address = addressList.get(i).getAddressLine(0); // 주소
                    System.out.println(address);



                    Double latitude = addressList.get(i).getLatitude(); // 위도
                    Double longitude = addressList.get(i).getLongitude(); // 경도
                    System.out.println(latitude);
                    System.out.println(longitude);

                    // 좌표(위도, 경도) 생성
                    LatLng point = new LatLng(latitude, longitude);
                    // 마커 생성
                    MarkerOptions mOptions2 = new MarkerOptions();
                    mOptions2.title("search result");
                    mOptions2.snippet(address);
                    mOptions2.position(point);
                    // 마커 추가
                    mMap.clear();
                    mMap.addMarker(mOptions2);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
                    place_address = address;
                }
            }
        });

/////////////


        // 해당 좌표로 화면 줌
        ////////////////////
        btn_select.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(place_address == null){
                    Toast.makeText(MapsActivity.this, "please, set the address(marker on map)", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MapsActivity.this, place_address,Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                intent.putExtra("place_address", place_address);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        btn_back.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        ////////////////////

        LatLng seoul = new LatLng(37.511259,126.991566);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul,11));
    }



}