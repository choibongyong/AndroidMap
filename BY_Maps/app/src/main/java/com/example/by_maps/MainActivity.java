package com.example.by_maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    NaverMap myMap;
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

        Button button_Basic = (Button)findViewById(R.id.button);
        button_Basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naverMap.setMapType(NaverMap.MapType.Basic);
            }
        });

        Button button_Hybrid = (Button)findViewById(R.id.button2);
        button_Hybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naverMap.setMapType(NaverMap.MapType.Hybrid);
            }
        });

        Button button_Navi = (Button)findViewById(R.id.button3);
        button_Navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naverMap.setMapType(NaverMap.MapType.Navi);
            }
        });

        LatLng coord = new LatLng(37, 127);
        Toast.makeText(getApplicationContext(), "위도: " + coord.latitude + "경도: " + coord.longitude,Toast.LENGTH_SHORT).show();
        //coord.distanceTo(new LatLng(15,200));

    }
}
