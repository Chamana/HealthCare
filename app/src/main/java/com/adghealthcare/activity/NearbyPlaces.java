package com.adghealthcare.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adghealthcare.R;
import com.adghealthcare.service.LocationService;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NearbyPlaces extends AppCompatActivity implements OnMapReadyCallback{
    Circle circle;
    SupportMapFragment mapFragment;
    TextView textView_time;
    GoogleMap googleMap=null;
    Marker marker=null;
    String latlong="",currLatLong="";
    BroadcastReceiver deviceLocationBdRec;
    CircleOptions circleOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);
        mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }
    public void back_onClick(View v){
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.googleMap=googleMap;
        showHospitals((LinearLayout)findViewById(R.id.ll_hos));
    }

    public boolean isPermissionGiven(){
        if(Build.VERSION.SDK_INT>=23){
            int coarseLocationPermission= ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int fineLocationPermission=ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            int granted= PackageManager.PERMISSION_GRANTED;

            if(coarseLocationPermission==granted && fineLocationPermission==granted){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return true;
        }
    }

    public void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            int granted=PackageManager.PERMISSION_GRANTED;
            if(grantResults[0]==granted && grantResults[1]==granted){
                if(isPermissionGiven()){
                    startService(new Intent(this, LocationService.class));
                }
            }
            else{
                Toast.makeText(this, "Give permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showHospitals(View v){
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+currLatLong+"&radius=2000&types=hospital&key=AIzaSyCeSIoetm1WPMs84WXDF7hj-DEMAsfy-Ws";

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("The response is "+response);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        String name=jsonArray.getJSONObject(i).getString("name");
                        JSONObject jsonObjectResult=jsonArray.getJSONObject(i).getJSONObject("geometry");
                        JSONObject jsonObjLoc=jsonObjectResult.getJSONObject("location");
                        double lat=jsonObjLoc.getDouble("lat");
                        double lng=jsonObjLoc.getDouble("lng");
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng))).setTitle(name);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15.0f));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(NearbyPlaces.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }
    public void showRestaurants(View v){
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+currLatLong+"&radius=2000&types=restaurant&key=AIzaSyCeSIoetm1WPMs84WXDF7hj-DEMAsfy-Ws";

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("The response is "+response);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        String name=jsonArray.getJSONObject(i).getString("name");
                        JSONObject jsonObjectResult=jsonArray.getJSONObject(i).getJSONObject("geometry");
                        JSONObject jsonObjLoc=jsonObjectResult.getJSONObject("location");
                        double lat=jsonObjLoc.getDouble("lat");
                        double lng=jsonObjLoc.getDouble("lng");
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng))).setTitle(name);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15.0f));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(NearbyPlaces.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(deviceLocationBdRec==null){
            deviceLocationBdRec=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    currLatLong=intent.getExtras().getString("latLong","");
                }
            };
        }
        registerReceiver(deviceLocationBdRec,new IntentFilter("my_device_location"));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(deviceLocationBdRec!=null){
            unregisterReceiver(deviceLocationBdRec);
        }
    }
}
