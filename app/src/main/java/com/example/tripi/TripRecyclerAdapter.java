package com.example.tripi;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripi.model.Trip;

import java.util.List;

class TripViewHolder extends RecyclerView.ViewHolder{
    TextView name;
    TextView tripLocation;
    TextView tripDescription;
    TextView tripLevel;
    ImageView tripImage;
    List<Trip> data;

    public TripViewHolder(@NonNull View itemView, TripRecyclerAdapter.OnItemClickListener listener, List<Trip> data) {
        super(itemView);
        this.data = data;
        name = itemView.findViewById(R.id.triplistrow_name);
        tripLocation = itemView.findViewById(R.id.triplistrow_location);
        tripDescription = itemView.findViewById(R.id.triplistrow_description);
        tripLevel = itemView.findViewById(R.id.triplistrow_level);
        tripImage = itemView.findViewById(R.id.triplistrow_img);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getLayoutPosition();
                listener.onItemClick(pos);
            }
        });
    }

    public void bind(Trip tr, int pos) {
        name.setText(tr.name);
        tripLocation.setText(tr.tripLocation);
        tripDescription.setText(tr.tripDescription);
        tripLevel.setText(tr.tripLevel);
        Picasso.get().load(tr.tripImgUrl).placeholder(R.drawable.ic_launcher_background).into(tripImage);
    }
}

public class TripRecyclerAdapter extends RecyclerView.Adapter<TripViewHolder>{
    OnItemClickListener listener;
    public static interface OnItemClickListener{
        void onItemClick(int pos);
    }

    LayoutInflater inflater;
    List<Trip> data;
    public TripRecyclerAdapter(LayoutInflater inflater, List<Trip> data){
        this.inflater = inflater;
        this.data = data;
    }

    public void setData(List<Trip> data){
        this.data = data;
        notifyDataSetChanged();
    }

    void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.trip_list_row,parent,false);
        return new TripViewHolder(view,listener, data);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip tr = data.get(position);
        holder.bind(tr, position);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }
}