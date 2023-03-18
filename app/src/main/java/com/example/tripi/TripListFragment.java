package com.example.tripi;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripi.databinding.FragmentTripListBinding;
import com.example.tripi.model.Model;

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

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(TripListFragmentViewModel.class);
    }

    void reloadData(){
        Model.instance().refreshAllTrips();
    }
}