package com.example.tripi;

import static java.util.UUID.randomUUID;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tripi.model.Model;
import com.example.tripi.model.Trip;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class AddTripFragment extends Fragment {

    private FirebaseAuth mAuth;
    ImageView img;

    public AddTripFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);

        Button saveBtn = view.findViewById(R.id.saveButton);
        EditText locationEt = view.findViewById(R.id.editTextLocation);
        EditText descriptionEt = view.findViewById(R.id.editTextDescription);
        EditText levelEt = view.findViewById(R.id.editTextLevel);
        img = view.findViewById(R.id.imageView2);

        // TODO: Add image
        Button browse = view.findViewById(R.id.browseButton);

        browse.setOnClickListener(event -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryActivityResultLauncher.launch(galleryIntent);
        });

        saveBtn.setOnClickListener(event -> {
            Model.instance().addTrip(new Trip(randomUUID().toString(), mAuth.getCurrentUser().getDisplayName(), "image",
                                                locationEt.getText().toString(),
                                                descriptionEt.getText().toString(),
                                                levelEt.getText().toString(),
                                                mAuth.getCurrentUser().getUid()));

            Navigation.findNavController(event).navigate(R.id.action_addTripFragment_to_tripListFragment);
        });

        // Inflate the layout for this fragment
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