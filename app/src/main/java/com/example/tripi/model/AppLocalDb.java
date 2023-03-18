package com.example.tripi.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tripi.MyApplication;

@Database(entities = {Trip.class}, version = 2)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract TripDao tripDao();
}
public class AppLocalDb{
    static public AppLocalDbRepository getAppDb() {
        return Room.databaseBuilder(MyApplication.getAppContext(),
                        AppLocalDbRepository.class,
                        "dbFileName.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    private AppLocalDb(){}
}