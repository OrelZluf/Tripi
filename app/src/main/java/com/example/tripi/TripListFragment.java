package com.example.tripi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
        Button newStudentBtn =  (Button) view.findViewById(R.id.newStudentBtn);
        data = Model.instance().getAllTrips();
        newStudentBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddTripFragment.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);
        list = view.findViewById(R.id.triplistfrag_list);

        return view;
    }

    @Override
    public void onStart() {
        data = Model.instance().getAllTrips();

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TripRecyclerAdapter(getLayoutInflater(),data);
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new TripRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);

                Intent intent = new Intent(getContext(), EditTripFragment.class);
                Bundle params = new Bundle();

                params.putInt("pos", pos);
                intent.putExtras(params);

                startActivity(intent);
            }
        });

        super.onStart();
    }
}