package com.adghealthcare.data_model;

/**
 * Created by chaman on 30/3/18.
 */

public class User {
    String name,password,username,phonenumber,email,area,latlon;

    public User(){

    }

    public User(String name, String password, String username, String phonenumber, String email, String area, String latlon) {
        this.name = name;
        this.password = password;
        this.username = username;
        this.phonenumber = phonenumber;
        this.email = email;
        this.area = area;
        this.latlon = latlon;
    }
}
