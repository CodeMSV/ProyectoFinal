package com.carnage.model.user;

public abstract class User {
    protected Integer userIdentifier;
    protected static Integer UserIdentifier = 0;
    protected  String userName;
    protected  String userEmail;
    protected  String userPassword;

    public User(String userName, String userEmail, String userPassword) {
        this.userIdentifier = UserIdentifier++;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
}
