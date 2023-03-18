package com.example.tripi.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    private static final Model _instance = new Model();
    public static Model instance(){
        return _instance;
    }

    private Model(){
        // TODO: remove Mock data
        addTrip(new Trip("1","דור", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Israel-2013-Aerial_21-Masada.jpg/1200px-Israel-2013-Aerial_21-Masada.jpg","מצודה","טיול מומלץ למשפחות", "קל"));
        addTrip(new Trip("2","דור", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Israel-2013-Aerial_21-Masada.jpg/1200px-Israel-2013-Aerial_21-Masada.jpg","מצודה","טיול מומלץ למשפחות", "קל"));
        addTrip(new Trip("3","דור", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Israel-2013-Aerial_21-Masada.jpg/1200px-Israel-2013-Aerial_21-Masada.jpg","מצודה","טיול מומלץ למשפחות", "קל"));
    }

    List<Trip> data = new LinkedList<>();

    public List<Trip> getAllTrips(){
        return data;
    }

    public void addTrip(Trip tr){
        data.add(tr);
    }

    public void deleteTrip(int position){
        data.remove(position);
    }

    public void editTrip(int position, Trip tr){
        data.set(position, tr);
    }
}
