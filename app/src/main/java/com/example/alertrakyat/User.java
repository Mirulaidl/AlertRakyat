package com.example.alertrakyat;

public class User {
    private String username;
    private String email;
    private String phonenum;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {}

    public User(String username, String email, String phonenum) {
        this.username = username;
        this.email = email;
        this.phonenum = phonenum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoPhone() {
        return phonenum;
    }

    public void setNoPhone(String noPhone) {
        this.phonenum = noPhone;
    }
}

