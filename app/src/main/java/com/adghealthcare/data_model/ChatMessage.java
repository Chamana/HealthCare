package com.adghealthcare.data_model;


public class ChatMessage {
    private String sender_id,text;

    public ChatMessage(){

    }

    public ChatMessage(String sender_id,String text){
        this.sender_id=sender_id;
        this.text=text;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
