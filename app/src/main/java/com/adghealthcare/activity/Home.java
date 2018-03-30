package com.adghealthcare.activity;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adghealthcare.*;
import com.adghealthcare.Manifest;
import com.adghealthcare.R;
import com.adghealthcare.service.LocationService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements OnMapReadyCallback{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView textView_name,textView_regno,textView_activity_title;
    ImageView imageView_notifications,imageView_zone;
    CircularImageView circularImageView_dp;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    Context context=this;
    String myUID="";
    Circle circle;

    SupportMapFragment mapFragment;
    TextView textView_time;
    GoogleMap googleMap=null;
    Marker marker=null;
    String latlong="";
    BroadcastReceiver deviceLocationBdRec;
    CircleOptions circleOptions;
    String city="";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference placeRef;
    int safe_code=0;

    int yellow=0x88f7dc6f;
    int red=0x88FF5733;
    int green=0x77b6feb6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences=getSharedPreferences("vit_companion", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        firebaseDatabase=FirebaseDatabase.getInstance();
        placeRef=firebaseDatabase.getReference("place/");
        String firebase_token=FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(context, firebase_token, Toast.LENGTH_SHORT).show();
        System.out.print("\n\n\n"+firebase_token+"\n\n\n");

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        textView_activity_title=(TextView)toolbar.findViewById(R.id.textView_activity_title);
        textView_activity_title.setText("Home");
        myUID=sharedPreferences.getString("uid","gopal");

        imageView_zone=(ImageView)findViewById(R.id.imageView_zone);
        mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        if(isPermissionGiven()){
            startService(new Intent(this, LocationService.class));
            Toast.makeText(context, "service started", Toast.LENGTH_SHORT).show();
        }
        else{
            requestPermission();
        }
        circleOptions=new CircleOptions().center(new LatLng(12.9796,79.1375)).radius(1000).strokeWidth(0f).fillColor(yellow);
    }

    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        navigationView=(NavigationView)findViewById(R.id.navigationView);
        circularImageView_dp=(CircularImageView)navigationView.getHeaderView(0).findViewById(R.id.circularImageView_dp);

        textView_name=(TextView)navigationView.getHeaderView(0).findViewById(R.id.textView_name);
        textView_name.setText(sharedPreferences.getString("name","Gopalakrishnan V"));

        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.nav_menu);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        drawerLayout.closeDrawers();
                        Toast.makeText(context, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_near_by:
                        startActivity(new Intent(Home.this,NearbyPlaces.class));
                        drawerLayout.closeDrawers();
                        Toast.makeText(context, "Near by", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_diagnosis:
                        drawerLayout.closeDrawers();
                        Toast.makeText(context, "nav_diagnosis", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_family:
                        drawerLayout.closeDrawers();
                        Toast.makeText(context, "nav_family", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_chatrooms:
                        drawerLayout.closeDrawers();
                        Toast.makeText(context, "nav_chatrooms", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_live_feed:
                        drawerLayout.closeDrawers();
                        Toast.makeText(context, "nav_live_feed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_sos:
                        drawerLayout.closeDrawers();
                        Toast.makeText(context, "nav_sos", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_logout:
                        drawerLayout.closeDrawers();
                        Toast.makeText(context, "nav_logout", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        drawerLayout.closeDrawers();
                        return false;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.googleMap=googleMap;
        circle=googleMap.addCircle(circleOptions);
    }

    public void updateUI(String latLong_new){
        Toast.makeText(context, latLong_new, Toast.LENGTH_SHORT).show();
        if(latLong_new.length()==0){
            return;
        }
        String splitted[]=latLong_new.split(",");
        LatLng latestPosition=new LatLng(Double.parseDouble(splitted[0]),Double.parseDouble(splitted[1]));

        if(googleMap==null){
            Toast.makeText(this, "Google map is null", Toast.LENGTH_SHORT).show();
            return;
        }
        if(marker==null){
            marker=googleMap.addMarker(new MarkerOptions().position(latestPosition));

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latestPosition,18.0f));
            circleOptions.center(latestPosition);
            circle.remove();
            circle=googleMap.addCircle(circleOptions);

        }
        else{
            marker.setPosition(latestPosition);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latestPosition,18.0f));
            circleOptions.center(latestPosition);
            circle.remove();
            circle=googleMap.addCircle(circleOptions);
        }
    }

    public void nav_onClick(View v){
        drawerLayout.openDrawer(Gravity.START);
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

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(deviceLocationBdRec==null){
            deviceLocationBdRec=new BroadcastReceiver() {
                @Override
                public void onReceive(final Context context, Intent intent) {
                    String latLong_new=intent.getExtras().getString("latLong","");

                    Geocoder geocoder = new Geocoder(Home.this, Locale.getDefault());
                    try {
                        String[] latLongSplitted=latLong_new.split(",");
                        List<Address> addressList = geocoder.getFromLocation(Double.parseDouble(latLongSplitted[0]),Double.parseDouble(latLongSplitted[1]), 1);
                        city = addressList.get(0).getLocality();
                        placeRef.child(city).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int count=0;
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    if(ds.getValue().toString().equals("safe")){
                                        count++;
                                    }
                                    else{
                                        count--;
                                    }
                                }
                                if(count>0){
                                    circleOptions.fillColor(green);
                                    /*circle.remove();
                                    circle=googleMap.addCircle(circleOptions);*/
                                }
                                else if(count<0){
                                    circleOptions.fillColor(red);
                                }
                                else{
                                    circleOptions.fillColor(yellow);
                                }
                                Toast.makeText(context, count+"", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    catch (IOException e)
                    {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    updateUI(latLong_new);
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

    public void changeZone(View v){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you at safe zone?");
        builder.setPositiveButton("SAFE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageView_zone.setImageResource(R.drawable.checked);
                placeRef.child(city).child(myUID).setValue("safe");
                dialog.cancel();
            }
        });
        builder.setNegativeButton("DANGER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageView_zone.setImageResource(R.drawable.close);
                placeRef.child(city).child(myUID).setValue("danger");
                dialog.cancel();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
