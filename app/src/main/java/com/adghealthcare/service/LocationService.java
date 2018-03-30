package com.adghealthcare.service;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class LocationService extends Service{
    SharedPreferences sharedPreferences;
    LocationManager locationManager;
    LocationListener locationListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference phoneReference;
    long lastGPSLocTime=0;
    String user_id="";
    String phone_id= Build.BRAND+" "+Build.DEVICE;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        sharedPreferences=getSharedPreferences("salt",MODE_PRIVATE);
        user_id=sharedPreferences.getString("user_id","");
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Intent intent=new Intent("my_device_location");
                intent.putExtra("latLong",location.getLatitude()+","+location.getLongitude());
                sendBroadcast(intent);
                System.out.print(location.toString());
                /*final long currGPSLocTime=Calendar.getInstance().getTimeInMillis();
                if(currGPSLocTime-lastGPSLocTime<10000){
                    if(!location.getProvider().equals("gps")){
                        return;
                    }
                }
                if(location.getProvider().equals("gps")){
                    lastGPSLocTime=currGPSLocTime;
                }
                String currTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                String val=location.getProvider()+"#"+location.getLatitude()+"#"+location.getLongitude()+"#"+currTime;
                phoneReference.child("val").setValue(val, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null){
                        }
                        else{
                        }
                    }
                });*/
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                sendBroadcast(new Intent("location_update").putExtra("coor","onStatusChanged"+provider));
            }

            @Override
            public void onProviderEnabled(String provider) {
                sendBroadcast(new Intent("location_update").putExtra("coor","onProviderEnabled"));
            }

            @Override
            public void onProviderDisabled(String provider) {
                sendBroadcast(new Intent("location_update").putExtra("coor","onProviderDisabled"));
            }
        };

        //Firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        phoneReference=firebaseDatabase.getReference().child("users").child(user_id).child("phones").child(phone_id);

        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,locationListener);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,locationListener);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }

    public void getTimeDate(){

    }
}
