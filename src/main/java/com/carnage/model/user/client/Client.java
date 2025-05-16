package com.carnage.model.user.client;

import com.carnage.model.user.User;

public class Client extends User {

    private String address;


    public Client(String userName, String userEmail, String userPassword, String address) {
        super(userName, userEmail, userPassword);
        this.address = address;
    }
}
