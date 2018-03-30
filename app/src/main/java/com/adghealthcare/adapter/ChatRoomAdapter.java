package com.adghealthcare.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adghealthcare.*;
import com.adghealthcare.activity.ChatScreen;
import com.adghealthcare.data_model.ChatRoom;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>{

    List<ChatRoom> chatRoomList;
    Context context;

    public ChatRoomAdapter(Context context,List<ChatRoom> chatRoomList){
        this.context=context;
        this.chatRoomList=chatRoomList;
    }

    @Override
    public ChatRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView= LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_card,parent,false);
        return new ChatRoomViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ChatRoomViewHolder holder, int position) {
        final ChatRoom chatRoom=chatRoomList.get(position);
        holder.textView_chatroom_id.setText(chatRoom.getId().toUpperCase());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatScreen.class);
                intent.putExtra("chatRoom_id",chatRoom.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder{
        TextView textView_chatroom_id;
        CardView cardView;

        public ChatRoomViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView) itemView.findViewById(R.id.cardView);
            textView_chatroom_id=(TextView)itemView.findViewById(R.id.textView_chatroom_id);
        }
    }
}
