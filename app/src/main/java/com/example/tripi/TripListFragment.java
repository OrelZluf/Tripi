package com.example.tripi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripi.databinding.FragmentTripListBinding;
import com.example.tripi.model.Model;
import com.google.firebase.auth.FirebaseAuth;

public class TripListFragment extends Fragment {
    FragmentTripListBinding binding;
    TripRecyclerAdapter adapter;
    TripListFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTripListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.triplistfragList.setHasFixedSize(true);
        binding.triplistfragList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TripRecyclerAdapter(getLayoutInflater(),viewModel.getData().getValue());
        binding.triplistfragList.setAdapter(adapter);

        View profileBtn = view.findViewById(R.id.profileBtn);
        View newTripBtn = view.findViewById(R.id.newTripBtn);
        profileBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_tripListFragment_to_myProfileFragment));
        newTripBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_tripListFragment_to_addTripFragment));

        viewModel.getData().observe(getViewLifecycleOwner(),list->{
            adapter.setData(list);
        });

        Model.instance().EventTripListLoadingState.observe(getViewLifecycleOwner(),status->{
            binding.swipeRefresh.setRefreshing(status == Model.LoadingState.LOADING);
        });

        binding.swipeRefresh.setOnRefreshListener(()->{
            reloadData();
        });

        View button = view.findViewById(R.id.disconnectButton);

        button.setOnClickListener((view1)->{
            if (FirebaseAuth.getInstance() != null) {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view1).navigate(R.id.action_tripListFragment_to_introFragment);
            }
        });

        return view;
    }

    // TODO : request trip advisor API
//    private void getTripAdvisor(){
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://travel-advisor.p.rapidapi.com/attractions/list-by-latlng?longitude=35.0818155&latitude=31.4117257&lunit=km&currency=USD&lang=en_US")
//                .get()
//                .addHeader("X-RapidAPI-Key", "ea573a0d8amsh861d48412130674p125478jsne31351e27815")
//                .addHeader("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
//                .build();
//
//        Response response = client.newCall(request).execute();
//    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(TripListFragmentViewModel.class);
    }

    void reloadData(){
        Model.instance().refreshAllTrips();
    }
}