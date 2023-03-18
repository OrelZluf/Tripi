package com.example.tripi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripi.model.Model;
import com.example.tripi.model.Trip;

import java.util.List;

public class TripListFragmentViewModel extends ViewModel {
    private LiveData<List<Trip>> data = Model.instance().getAllTrips();

    LiveData<List<Trip>> getData(){
        return data;
    }
}
