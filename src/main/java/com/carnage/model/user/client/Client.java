package com.carnage.model.user.client;

import com.carnage.model.user.User;

public class Client extends User {

    private String address;
    private String phone;

    public Client() {
    }

    public Client(String userName, String userEmail, String userPassword, String address, String phone) {
        super(userName, userEmail, userPassword);
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPhone() {
        return this.phone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "address='" + address + '\'' +
                ", phone=" + phone +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
