package com.example.tripi.model;

import android.media.Image;

public class Trip {
    public String id;
    public String name;
    public String tripImgUrl;
    public String tripLocation;
    public String tripDescription;
    public String tripLevel;

    public Trip(String id, String name, String tripImgUrl, String tripLocation, String tripDescription, String tripLevel){
        this.id = id;
        this.name = name;
        this.tripImgUrl = tripImgUrl;
        this.tripLocation = tripLocation;
        this.tripDescription = tripDescription;
        this.tripLevel = tripLevel;
    }
}
