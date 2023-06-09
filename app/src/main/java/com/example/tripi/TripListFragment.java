package com.example.tripi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripi.databinding.FragmentTripListBinding;
import com.example.tripi.model.Model;
import com.example.tripi.model.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TripListFragment extends Fragment {
    FragmentTripListBinding binding;
    TripRecyclerAdapter adapter;
    TripListFragmentViewModel viewModel;
    private FirebaseAuth mAuth;
    boolean isMyTrips = false;
    List<Trip> myTripsList;
    Trip apiTrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            getTripAdvisor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        binding = FragmentTripListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();

        binding.triplistfragList.setHasFixedSize(true);
        binding.triplistfragList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TripRecyclerAdapter(getLayoutInflater(),viewModel.getData().getValue());
        binding.triplistfragList.setAdapter(adapter);

        adapter.setOnItemClickListener(new TripRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Trip tr;

                if (isMyTrips && myTripsList != null) {
                    tr = myTripsList.get(pos);
                } else {
                    tr = viewModel.getData().getValue().get(pos);
                }

                if (tr.userId != null && tr.userId.equals(mAuth.getCurrentUser().getUid())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("tripId", tr.id);
                    bundle.putString("tripLocation", tr.tripLocation);
                    bundle.putString("tripDescription", tr.tripDescription);
                    bundle.putString("tripLevel", tr.tripLevel);
                    bundle.putString("tripImgUrl", tr.tripImgUrl);
                    Navigation.findNavController(view).navigate(R.id.action_tripListFragment_to_editTripFragment, bundle);
                }
            }
        });

        View profileBtn = view.findViewById(R.id.profileBtn);
        View newTripBtn = view.findViewById(R.id.newTripBtn);
        profileBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_tripListFragment_to_myProfileFragment));
        newTripBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_tripListFragment_to_addTripFragment));

        try {
            isMyTrips = getArguments().getBoolean("myTrips");
        } catch (Exception e){
            isMyTrips = false;
        }

        viewModel.getData().observe(getViewLifecycleOwner(),list->{
            myTripsList = list.stream().filter(trip -> {
                if(trip.userId!= null) {
                    return trip.userId.equals(mAuth.getCurrentUser().getUid());
                } else {
                    return false;
                }
            }).collect(Collectors.toList());

            if (isMyTrips){
                if (apiTrip != null) {
                    myTripsList.add(0, apiTrip);
                }
                adapter.setData(myTripsList);
            } else {
                if (apiTrip != null) {
                    list.add(0, apiTrip);
                }
                adapter.setData(list);
            }
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

    class RetrieveFeedTask extends AsyncTask<String, Void, Response> {

        protected Response doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://travel-advisor.p.rapidapi.com/attractions/list-by-latlng?longitude=35.0818155&latitude=31.4117257&lunit=km&currency=USD&lang=en_US")
                    .get()
                    .addHeader("X-RapidAPI-Key", "ea573a0d8amsh861d48412130674p125478jsne31351e27815")
                    .addHeader("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
                    .build();

            try {
                return client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Response response) {
            try {
                JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
                JsonObject objTrToAdd = (JsonObject) jsonObject.get("data").getAsJsonArray().get(1);

                apiTrip = new Trip(UUID.randomUUID().toString(), "מומלץ!", objTrToAdd.getAsJsonObject("photo").getAsJsonObject("images").getAsJsonObject("small").get("url").getAsString(),objTrToAdd.get("location_string").getAsString(), objTrToAdd.get("name").getAsString(), objTrToAdd.get("ranking_category").getAsString(), "fakeUserId");
                reloadData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getTripAdvisor() throws IOException {
        new RetrieveFeedTask().execute();
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