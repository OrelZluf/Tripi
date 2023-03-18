package com.example.tripi.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    private static final Model _instance = new Model();
    public static Model instance(){
        return _instance;
    }
    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FirebaseModel firebaseModel = new FirebaseModel();
    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    private Model() {}

    public interface Listener<T>{
        void onComplete(T data);
    }

    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }
    final public MutableLiveData<LoadingState> EventTripListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);

    private LiveData<List<Trip>> tripList;
    public LiveData<List<Trip>> getAllTrips() {
        if (tripList == null) {
            tripList = localDb.tripDao().getAll();
            refreshAllTrips();
        }
        return tripList;
    }

    public void refreshAllTrips(){
        EventTripListLoadingState.setValue(LoadingState.LOADING);
        Long localLastUpdate = Trip.getLocalLastUpdate();

        firebaseModel.getAllTripsSince(localLastUpdate, list->{
            executor.execute(()->{
                Log.d("TAG", " firebase return : " + list.size());
                Long time = localLastUpdate;
                for(Trip tr : list){
                    localDb.tripDao().insertAll(tr);
                    if (time < tr.getLastUpdated()){
                        time = tr.getLastUpdated();
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // update local last update
                Trip.setLocalLastUpdate(time);
                EventTripListLoadingState.postValue(LoadingState.NOT_LOADING);
            });
        });
    }

    public void addTrip(Trip tr, Listener<Void> listener){
        firebaseModel.addTrip(tr,(Void)->{
            refreshAllTrips();
            listener.onComplete(null);
        });
    }
}
