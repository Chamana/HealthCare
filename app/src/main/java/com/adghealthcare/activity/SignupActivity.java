package com.adghealthcare.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.adghealthcare.data_model.User;
import com.adghealthcare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by chaman on 30/3/18.
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username_et, password_et, name_et, phoneNo_et, email_et;
    private Button signup_btn;
    private ImageView back_iv;
    ArrayList<String> username;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LocationManager locationManager;
    String city = "", latlong = "";
    Boolean userExists;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        setInit();
    }


    private void init() {
        username_et = (EditText) findViewById(R.id.username_et);
        password_et = (EditText) findViewById(R.id.password_et);
        name_et = (EditText) findViewById(R.id.name_et);
        phoneNo_et = (EditText) findViewById(R.id.phone_et);
        email_et = (EditText) findViewById(R.id.email_et);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        back_iv = (ImageView) findViewById(R.id.backarrow_iv);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        username = new ArrayList<>();
        userExists = false;
        sharedPreferences=getApplicationContext().getSharedPreferences("hc",MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    private void setInit() {
        signup_btn.setOnClickListener(this);
        back_iv.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_btn:
               // getLocation();
                if (TextUtils.isEmpty(username_et.getText().toString())) {
                    username_et.setError("Please enter the username");
                } else if (TextUtils.isEmpty(password_et.getText().toString())) {
                    password_et.setError("Please enter the password");
                } else if (TextUtils.isEmpty(name_et.getText().toString())) {
                    name_et.setError("Please enter the name");
                } else if (TextUtils.isEmpty(phoneNo_et.getText().toString())) {
                    phoneNo_et.setError("Please enter the phone number");
                } else if (TextUtils.isEmpty(email_et.getText().toString())) {
                    email_et.setError("Please enter the email address");
                } else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                username.add(ds.getKey());
                            }
                            if (username.size() == 0) {
                                User user = new User(name_et.getText().toString(), password_et.getText().toString(), username_et.getText().toString(), phoneNo_et.getText().toString(), email_et.getText().toString(), city, "0,0");
                                databaseReference.child(username_et.getText().toString()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        editor.putString("uid",username_et.getText().toString());
                                        editor.commit();
                                        startActivity(new Intent(SignupActivity.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignupActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                for (int i = 0; i < username.size(); i++) {
                                    if (username.get(i).equals(username_et.getText().toString())) {
                                        userExists = true;
                                        break;
                                    }
                                }
                                if (userExists) {
                                    Toast.makeText(SignupActivity.this, "Username already exists. Please try some other username.", Toast.LENGTH_SHORT).show();
                                } else {
                                    User user = new User(name_et.getText().toString(), password_et.getText().toString(), username_et.getText().toString(), phoneNo_et.getText().toString(), email_et.getText().toString(), city, latlong);
                                    databaseReference.child(username_et.getText().toString()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            editor.putString("uid",username_et.getText().toString());
                                            editor.commit();
                                            startActivity(new Intent(SignupActivity.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignupActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Signup is clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.backarrow_iv:
                super.onBackPressed();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}