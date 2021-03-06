package com.example.by_maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import com.naver.maps.map.overlay.PolygonOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    NaverMap myMap;

    int count_Basic =1, count_Hybrid =1, count_Navi =1;
    int n =0;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }
        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }

    public class MyAsyncTask extends AsyncTask<LatLng,String, String>{

        @Override
        protected String doInBackground(LatLng... latLngs) {

            String strCoord = String.valueOf(latLngs[0].longitude) + "," + String.valueOf(latLngs[0].latitude);
            Log.d("myLog", strCoord);

            StringBuilder sb = new StringBuilder();
            StringBuilder urlBuilder =
                    new StringBuilder("https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr&coords=" +strCoord+ "&sourcecrs=epsg:4326&output=json&orders=addr");
            try{
                URL url = new URL(urlBuilder.toString());
                HttpsURLConnection http = (HttpsURLConnection)url.openConnection();
                http.setRequestProperty("Content-type", "application/json");
                http.getRequestProperty("X-NCP-APIGW-API-KEY-ID:{b2swhahkzz}");
                http.getRequestProperty("X-NCP-APIGW-API-KEY:{p4QGLMdzuXMdbK8BBCgIZHVo7MKKtc6mK5k6ScaI}");
                http.setRequestMethod("GET");
                http.connect();

                InputStreamReader in = new InputStreamReader(http.getInputStream(),"utf-8");
                BufferedReader rd;
//                Log.d("getResponseCode: ", http.getResponseCode()+"");
                if(http.getResponseCode() >=200 && http.getResponseCode() <= 300){
                    rd = new BufferedReader(in);
                } else {
                    rd = new BufferedReader(in);
                }

                String line;
                while ((line = rd.readLine()) != null){
                    sb.append(line).append("\n");
                }

                JsonParser parser = new JsonParser();
                JsonObject jsonObject;
                JsonObject jsonObject2;
                String x = "";
                String y = "";

                jsonObject = (JsonObject) parser.parse(sb.toString());
                JsonArray jsonArray = (JsonArray) jsonObject.get("results");
                Log.d("myLog3", jsonArray.toString());

                for(int i=0;i<jsonArray.size();i++){
                    jsonObject2 = (JsonObject)jsonArray.get(i);

                    Log.d("myLog2", jsonObject2.toString());
                    //jsonObject2 = jsonArray.json();
                    if(null != jsonObject2.get("x")){
                        x = (String) jsonObject2.get("x").toString();
                    }
                    if(null != jsonObject2.get("y")){
                        x = (String) jsonObject2.get("y").toString();
                    }
                }
                rd.close();
                in.close();
                http.disconnect();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
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

        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag();
            }
        }); // 마커에 정보띄우기
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
        }; //마커 on/off

        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            List<LatLng> coords = new ArrayList<>();
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                Marker mark = new Marker();
                mark.setPosition(latLng);
                mark.setMap(naverMap);

                PolygonOverlay polygon = new PolygonOverlay();
                coords.add(new LatLng(latLng.latitude,latLng.longitude));

                if(coords.size() > 2){
                    polygon.setCoords(coords);
                    polygon.setMap(naverMap);
                    polygon.setOutlineWidth(5);
                    polygon.setOutlineColor(Color.GREEN);
                }
                if(coords.size() > 4) {
                    polygon.setMap(null);
                    polygon.setCoords(coords);
                    polygon.setMap(naverMap);
                }

                Log.d("coords size : ",Integer.toString(coords.size()));


                mark.setTag("위도: " + latLng.latitude + "경도: " + latLng.longitude);

                mark.setOnClickListener(listener);
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(latLng);

            }
        });

        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(35.945379, 126.682170));
        naverMap.moveCamera(cameraUpdate);

        Marker marker1 = new Marker();
        marker1.setPosition(new LatLng(35.945379, 126.682170)); //군산대학교 아카데미
        marker1.setMap(naverMap);
        marker1.setTag("군산대학교");
        marker1.setOnClickListener(listener);


    }
}
