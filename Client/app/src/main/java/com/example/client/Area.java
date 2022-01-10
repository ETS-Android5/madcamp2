package com.example.client;

import com.google.gson.annotations.SerializedName;

public class Area {
    @SerializedName("city")
    private String city;
    @SerializedName("county")
    private String county;
    @SerializedName("last")
    private String last;

    public String getCity() { return city; }

    public String getCounty() { return county; }

    public String getLast() { return last; }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Area(String city, String county, String last) {
        this.city = city;
        this.county = county;
        this.last = last;
    }
}
