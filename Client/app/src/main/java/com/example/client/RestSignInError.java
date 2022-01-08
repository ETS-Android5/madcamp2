package com.example.client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestSignInError {
    @SerializedName("username")
    public List<String> username;

    @SerializedName("email")
    public List<String> email;

    @SerializedName("password1")
    public List<String> password1;

    @SerializedName("non_field_errors")
    public List<String> non_field_errors;
}
