package com.adghealthcare.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adghealthcare.R;
import com.adghealthcare.adapter.ChatMessageAdapter;
import com.adghealthcare.data_model.ChatMessage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatScreen extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String chatRoom_id="";
    String myUID="";
    TextView textView_activity_title;
    RecyclerView recyclerView_chat;
    EditText editText_message;

    List<ChatMessage> chatMessageList;
    ChatMessageAdapter chatMessageAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        sharedPreferences=getSharedPreferences("hc",MODE_PRIVATE);
        chatRoom_id=getIntent().getExtras().getString("chatRoom_id","");
        myUID=sharedPreferences.getString("uid","gopal");

        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        chatReference=firebaseDatabase.getReference("chatroom/"+chatRoom_id);

        textView_activity_title=(TextView)findViewById(R.id.textView_activity_title);
        textView_activity_title.setText(chatRoom_id.toUpperCase());
        recyclerView_chat=(RecyclerView)findViewById(R.id.recyclerView_chat);
        editText_message=(EditText)findViewById(R.id.editText_message);

        chatMessageList=new ArrayList<>();
        chatMessageAdapter=new ChatMessageAdapter(chatMessageList,this,myUID);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView_chat.setLayoutManager(linearLayoutManager);
        recyclerView_chat.setAdapter(chatMessageAdapter);

        chatReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage=dataSnapshot.getValue(ChatMessage.class);
                chatMessageList.add(chatMessage);
                chatMessageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendMessage(View v){
        String text=editText_message.getText().toString().trim();
        if(text.length()==0){
            return;
        }
        editText_message.setText("");
        String messageId=chatReference.push().getKey();
        final ChatMessage chatMessage=new ChatMessage(myUID,text);
        chatReference.child(messageId).setValue(chatMessage, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError==null){
                    Toast.makeText(ChatScreen.this, "message stored", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back_onClick(View v){
        finish();
    }
}
