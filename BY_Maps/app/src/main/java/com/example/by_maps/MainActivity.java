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
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

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

        LatLng coord = new LatLng(37, 127);
        Toast.makeText(getApplicationContext(), "위도: " + coord.latitude + "경도: " + coord.longitude,Toast.LENGTH_SHORT).show();
        //coord.distanceTo(new LatLng(15,200));

    }
}
