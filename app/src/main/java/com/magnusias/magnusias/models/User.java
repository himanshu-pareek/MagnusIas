package com.magnusias.magnusias.models;

public class User {

    private String mFirstName, mLastName, mUsername, mEmail, mContact;

    public User(String firstname, String lastname, String username, String email, String contact) {
        mFirstName = firstname;
        mLastName = lastname;
        mUsername = username;
        mEmail = email;
        mContact = contact;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getContact() {
        return mContact;
    }

}
