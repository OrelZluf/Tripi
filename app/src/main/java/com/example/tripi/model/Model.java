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
        addTrip(new Trip("1","דור", "","מצודה","טיול מומלץ למשפחות", "קל"));
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
