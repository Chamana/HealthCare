package com.adghealthcare.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adghealthcare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by chaman on 30/3/18.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout username_til,password_til;
    private EditText username_et,password_et;
    private Button login_btn;
    private TextView signup_tv;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> usernames;
    private String[] permissions=new String[]{android.Manifest.permission.INTERNET,android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION};
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setInit();
    }

    private void init() {
        checkUserPermission();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users");
        username_til=(TextInputLayout)findViewById(R.id.username_til);
        password_til=(TextInputLayout)findViewById(R.id.password_til);
        username_et=(EditText)findViewById(R.id.username_et);
        password_et=(EditText)findViewById(R.id.password_et);
        login_btn=(Button)findViewById(R.id.login_btn);
        signup_tv=(TextView)findViewById(R.id.signup_tv);
        usernames=new ArrayList<>();
        sharedPreferences=getApplicationContext().getSharedPreferences("hc",MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    private boolean checkUserPermission() {
        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(android.Manifest.permission.INTERNET)== PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) ==PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED){
                return true;
            }else{
                ActivityCompat.requestPermissions(this,permissions,1);
                return false;
            }
        }else{
            return true;
        }
    }

    private void setInit() {
        login_btn.setOnClickListener(this);
        signup_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn:if(TextUtils.isEmpty(username_et.getText().toString())){
                                    username_et.setError("Please enter the username");
                                }else if(TextUtils.isEmpty(password_et.getText().toString())){
                                    password_et.setError("Please enter the password");
                                }else{
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                                usernames.add(ds.getKey());
                                            }
                                            for(int i=0;i<usernames.size();i++){
                                                if(usernames.get(i).equals(username_et.getText().toString())){
                                                    DatabaseReference databaseReference1=firebaseDatabase.getReference("users/"+username_et.getText().toString()+"/password");
                                                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if(password_et.getText().toString().equals(dataSnapshot.getValue().toString())){
                                                                editor.putString("uid",username_et.getText().toString());
                                                                editor.commit();
                                                                startActivity(new Intent(LoginActivity.this, Home.class));
                                                            }else{
                                                                Toast.makeText(LoginActivity.this, "Invalid password. Try Again.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                break;
            case R.id.signup_tv:startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            break;
        }
    }
}
