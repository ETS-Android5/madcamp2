package com.example.client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Review {
    @SerializedName("area")
    private Area area;
    @SerializedName("sight")
    private int sight;
    @SerializedName("touch")
    private int touch;
    @SerializedName("taste")
    private int taste;
    @SerializedName("result")
    private List<Area> result;

    public List<Area> getResult() {
        return result;
    }

    public void setResult(List<Area> result) {
        this.result = result;
    }

    public Review(Area area, int sight, int touch, int taste) {
        this.area = area;
        this.sight = sight;
        this.touch = touch;
        this.taste = taste;
    }

    public Area getArea() {
        return area;
    }

    public int getSight() {
        return sight;
    }

    public int getTouch() {
        return touch;
    }

    public int getTaste() {
        return taste;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setSight(int sight) {
        this.sight = sight;
    }

    public void setTouch(int touch) {
        this.touch = touch;
    }

    public void setTaste(int taste) {
        this.taste = taste;
    }
}
