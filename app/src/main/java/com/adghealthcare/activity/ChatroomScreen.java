package com.adghealthcare.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.adghealthcare.R;
import com.adghealthcare.adapter.ChatRoomAdapter;
import com.adghealthcare.data_model.ChatRoom;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatroomScreen extends AppCompatActivity {
    RecyclerView recyclerView_chatrooms;

    List<ChatRoom> chatRoomList;
    ChatRoomAdapter chatRoomAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatroomsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom_screen);

        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        chatroomsReference=firebaseDatabase.getReference("chatroom");

        recyclerView_chatrooms=(RecyclerView)findViewById(R.id.recyclerView_chatrooms);
        chatRoomList=new ArrayList<>();
        chatRoomAdapter=new ChatRoomAdapter(this,chatRoomList);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView_chatrooms.setLayoutManager(linearLayoutManager);
        recyclerView_chatrooms.setAdapter(chatRoomAdapter);

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        chatRoomList.clear();

        chatroomsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.cancel();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String key=ds.getKey();
                    chatRoomList.add(new ChatRoom(key));
                }
                chatRoomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.cancel();
            }
        });
    }

    public void back_onClick(View v){
        finish();
    }
}
