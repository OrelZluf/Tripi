package com.example.tripi.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TripDao {
    @Query("select * from Trip")
    LiveData<List<Trip>> getAll();

    @Query("select * from Trip where id = :tripId")
    Trip getTripById(String tripId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Trip... trips);

    @Delete
    void delete(Trip trip);
}