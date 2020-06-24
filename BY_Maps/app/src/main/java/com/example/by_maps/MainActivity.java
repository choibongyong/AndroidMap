package com.example.by_maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    NaverMap myMap;
    int count_Basic =1, count_Hybrid =1, count_Navi =1;
    int n =0;
//    ArrayList<String> arrayList;
//    ArrayAdapter<String> arrayAdapter;
//    Spinner spinner = (Spinner)findViewById(R.id.spinner);

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);

        //arrayList = new ArrayList<>();
        //arrayList.add("하이브리드"); arrayList.add("네비"); arrayList.add("일반");

        // arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList);

        //spinner.setAdapter(arrayAdapter);
        //spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }
        mapFragment.getMapAsync((OnMapReadyCallback) this);

    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {

        this.myMap = naverMap;

        final Button button_Basic = (Button)findViewById(R.id.button);
        final Button button_Hybrid = (Button)findViewById(R.id.button2);
        final Button button_Terrain = (Button)findViewById(R.id.button3);
        final ToggleButton toggleButton = (ToggleButton)findViewById(R.id.toggleButton);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleButton.isChecked()){
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, false);
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, true);
                }else{
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, false);
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);
                }
            }
        });

        button_Basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naverMap.setMapType(NaverMap.MapType.Basic);
                if(count_Basic++ % 2 == 0){
                    button_Hybrid.setVisibility(view.GONE);
                    button_Terrain.setVisibility(view.GONE);
                }else{
                    button_Hybrid.setVisibility(view.VISIBLE);
                    button_Terrain.setVisibility(view.VISIBLE);
                }
            }
        });
        button_Hybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naverMap.setMapType(NaverMap.MapType.Hybrid);

                if(count_Hybrid++ % 2 == 0){
                    button_Basic.setVisibility(view.VISIBLE);
                    button_Terrain.setVisibility(view.VISIBLE);
                }else{
                    button_Basic.setVisibility(view.GONE);
                    button_Terrain.setVisibility(view.GONE);
                }
            }
        });
        button_Terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naverMap.setMapType(NaverMap.MapType.Terrain);

                if(count_Navi++ % 2 == 0){
                    button_Basic.setVisibility(view.VISIBLE);
                    button_Hybrid.setVisibility(view.VISIBLE);
                }else{
                    button_Basic.setVisibility(view.GONE);
                    button_Hybrid.setVisibility(view.GONE);
                }
            }
        });


//        LatLng coord = new LatLng(200, 200);
//        Toast.makeText(getApplicationContext(), "위도: " + coord.latitude + "경도: " + coord.longitude,Toast.LENGTH_SHORT).show();

        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(35.945379, 126.682170));
        naverMap.moveCamera(cameraUpdate);

        Marker marker0 = new Marker();
        marker0.setPosition(new LatLng(37.566672, 126.978412)); //서울시청
        marker0.setMap(naverMap);

        Marker marker1 = new Marker();
        marker1.setPosition(new LatLng(35.945379, 126.682170)); //군산대학교 아카데미
        marker1.setMap(naverMap);

        Marker marker2 = new Marker();
        marker2.setPosition(new LatLng(36.282929, 126.917359)); //집
        marker2.setMap(naverMap);

        marker0.setTag("서울시청");
        marker1.setTag("군산대학교");
        marker2.setTag("집");

        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag();
            }
        });

        Overlay.OnClickListener listener = overlay -> {
            Marker marker = (Marker)overlay;

            if (marker.getInfoWindow() == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(marker);
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close();
            }
            return true;
        };

        marker0.setOnClickListener(listener);
        marker1.setOnClickListener(listener);
        marker2.setOnClickListener(listener);


        //위치오버레이
//        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
//        locationOverlay.setVisible(true);
//        locationOverlay.setPosition(new LatLng(35.945379, 126.682170));



    }
}
