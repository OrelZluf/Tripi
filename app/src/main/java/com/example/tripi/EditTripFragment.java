package com.example.tripi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tripi.model.Trip;

public class EditTripFragment extends Fragment {

    Trip editedTrip;
    EditText etTripLocation;
    EditText etTripDescription;
    EditText etTripLevel;
    ImageView img;

    public EditTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_trip, container, false);
        etTripLocation = view.findViewById(R.id.editTextLocation);
        etTripDescription = view.findViewById(R.id.editTextDescription);
        etTripLevel = view.findViewById(R.id.editTextLevel);
        img = view.findViewById(R.id.imageView2);

        etTripLocation.setText(getArguments().getString("tripLocation"));
        etTripDescription.setText(getArguments().getString("tripDescription"));
        etTripLevel.setText(getArguments().getString("tripLevel"));

        // TODO: add image
        Button browse = view.findViewById(R.id.browseButton);

        browse.setOnClickListener(event -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryActivityResultLauncher.launch(galleryIntent);
        });

        return view;
    }

    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri image_uri = result.getData().getData();
                        img.setImageURI(image_uri);
                    }
                }
            });
}