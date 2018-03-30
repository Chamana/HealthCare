package com.adghealthcare.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adghealthcare.data_model.ChatMessage;
import com.adghealthcare.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<ChatMessage> chatMessageList;
    Context context;
    String myUID="";
    private final static int OTHERS_MSG = 1;
    private final static int MY_MSG = 2;
    private final static int NOTE = 3;

    public class OthersMessageViewHolder extends RecyclerView.ViewHolder{
        public TextView textView_name;
        public TextView textView_message;
        public RelativeLayout relativeLayout_msg;

        public OthersMessageViewHolder(View itemView) {
            super(itemView);
            textView_name=(TextView)itemView.findViewById(R.id.textView_name);
            textView_message=(TextView) itemView.findViewById(R.id.textView_message);
            relativeLayout_msg=(RelativeLayout) itemView.findViewById(R.id.relativeLayout_msg);
        }
    }

    public class MyMessageViewHolder extends RecyclerView.ViewHolder{
        public TextView textView_message;
        public ProgressBar progressBar;
        public RelativeLayout relativeLayout_msg;

        public MyMessageViewHolder(View itemView) {
            super(itemView);
            textView_message=(TextView)itemView.findViewById(R.id.textView_message);
            relativeLayout_msg=(RelativeLayout) itemView.findViewById(R.id.relativeLayout_msg);
        }
    }

    public ChatMessageAdapter(List<ChatMessage> chatMessageList, Context context, String myUID){
        this.chatMessageList=chatMessageList;
        this.context=context;
        this.myUID=myUID;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==2){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mychat_message_card,parent,false);
            return new MyMessageViewHolder(view);
        }
        else{
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_card,parent,false);
            return new OthersMessageViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessageList.get(position).getSender_id().equals(myUID)){
            return 2;
        }
        else{
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ChatMessage chatMessage=chatMessageList.get(position);
        if (chatMessage==null){
            return;
        }
        if (!chatMessage.getSender_id().equals(myUID)){
            OthersMessageViewHolder othersMessageViewHolder=(OthersMessageViewHolder)holder;

            String sender_id=chatMessage.getSender_id();

            String sender_id_uc=sender_id;
            if(sender_id_uc.length()>1){
                sender_id_uc=(sender_id_uc.charAt(0)+"").toUpperCase()+sender_id_uc.substring(1,sender_id_uc.length());
            }
            //Toast.makeText(context, sender_id_uc, Toast.LENGTH_SHORT).show();
            othersMessageViewHolder.textView_name.setText(sender_id_uc);
            othersMessageViewHolder.textView_message.setText(chatMessage.getText());

            if(position>0){
                if(chatMessageList.get(position-1).getSender_id().equals(sender_id)){
                    othersMessageViewHolder.textView_name.setVisibility(View.GONE);
                }
                else{
                    othersMessageViewHolder.textView_name.setVisibility(View.VISIBLE);
                }
            }
            else{
                othersMessageViewHolder.textView_name.setVisibility(View.VISIBLE);
            }
        }
        else{
            MyMessageViewHolder myMessageViewHolder=(MyMessageViewHolder) holder;
            myMessageViewHolder.textView_message.setText(chatMessage.getText());
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }
}