package com.example.tripi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tripi.model.Model;
import com.example.tripi.model.Trip;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class TripListFragment extends Fragment {
    List<Trip> data;
    RecyclerView list;
    TripRecyclerAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        data = Model.instance().getAllTrips();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);
        list = view.findViewById(R.id.triplistfrag_list);
        data = Model.instance().getAllTrips();

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TripRecyclerAdapter(getLayoutInflater(),data);
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new TripRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Navigation.findNavController(view).navigate(R.id.action_tripListFragment_to_addTripFragment);
            }
        });

        View profileBtn = view.findViewById(R.id.profileBtn);
        View newTripBtn = view.findViewById(R.id.newTripBtn);
        profileBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_tripListFragment_to_myProfileFragment));
        newTripBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_tripListFragment_to_addTripFragment));

        return view;
    }
}