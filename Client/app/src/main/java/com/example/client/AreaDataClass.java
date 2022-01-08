package com.example.client;

import com.google.gson.annotations.SerializedName;

public class AreaDataClass {

    @SerializedName("reviewer")
    private Integer reviewer;
    @SerializedName("address")
    private String address;
    @SerializedName("sight")
    private Integer sight;
    @SerializedName("touch")
    private Integer touch;
    @SerializedName("taste")
    private Integer taste;

    public Integer getReviewer() { return reviewer; }

    public void setReviewer(Integer reviewer) {
        this.reviewer = reviewer;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSight() { return sight; }

    public void setSight(Integer sight) {
        this.sight = sight;
    }

    public Integer getTouch() { return touch; }

    public void setRTouch(Integer touch) {
        this.touch = touch;
    }

    public Integer getTaste() { return taste; }

    public void setTaste(Integer taste) {
        this.taste = taste;
    }
}
