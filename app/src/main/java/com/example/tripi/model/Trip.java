package com.example.tripi.model;

import android.media.Image;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.tripi.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Trip {
    @PrimaryKey
    @NonNull
    public String id = "";
    public String name = "";
    public String tripImgUrl = "";
    public String tripLocation = "";
    public String tripDescription = "";
    public String tripLevel = "";
    public Long lastUpdated;

    static final String NAME = "name";
    static final String ID = "id";
    static final String IMAGE_URL = "image_url";
    static final String LOCATION = "location";
    static final String DESCRIPTION = "description";
    static final String LEVEL = "level";
    static final String COLLECTION = "trips";
    static final String LAST_UPDATED = "lastUpdated";
    static final String LOCAL_LAST_UPDATED = "trips_local_last_update";

    public Trip() {}

    public Trip(String id, String name, String tripImgUrl, String tripLocation, String tripDescription, String tripLevel){
        this.id = id;
        this.name = name;
        this.tripImgUrl = tripImgUrl;
        this.tripLocation = tripLocation;
        this.tripDescription = tripDescription;
        this.tripLevel = tripLevel;
    }

    public static Trip fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String name = (String)json.get(NAME);
        String image_url = (String)json.get(IMAGE_URL);
        String location = (String)json.get(LOCATION);
        String description = (String)json.get(DESCRIPTION);
        String level = (String)json.get(LEVEL);

        Trip tr = new Trip(id, name, image_url, location, description, level);
        try {
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            tr.setLastUpdated(time.getSeconds());
        } catch (Exception e){

        }
        return tr;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getAppContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getAppContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED, time);
        editor.commit();
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(NAME, getName());
        json.put(IMAGE_URL, getTripImgUrl());
        json.put(LOCATION, getTripLocation());
        json.put(DESCRIPTION, getTripDescription());
        json.put(LEVEL, getTripLevel());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTripImgUrl() {
        return tripImgUrl;
    }

    public void setTripImgUrl(String tripImgUrl) {
        this.tripImgUrl = tripImgUrl;
    }

    public String getTripLocation() {
        return tripLocation;
    }

    public void setTripLocation(String tripLocation) {
        this.tripLocation = tripLocation;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public void setTripDescription(String tripDescription) {
        this.tripDescription = tripDescription;
    }

    public String getTripLevel() {
        return tripLevel;
    }

    public void setTripLevel(String tripLevel) {
        this.tripLevel = tripLevel;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
