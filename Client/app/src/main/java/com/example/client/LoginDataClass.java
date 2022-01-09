package com.example.client;

import com.google.gson.annotations.SerializedName;

public class LoginDataClass {

    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("password1")
    private String password1;
    @SerializedName("password2")
    private String password2;
    @SerializedName("key")
    private String key;
    @SerializedName("user")
    private Integer user;

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

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public Integer getUser() { return user; }

    public void setUser(Integer user) { this.user = user; }
}
