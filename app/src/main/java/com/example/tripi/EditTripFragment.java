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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tripi.model.Model;
import com.example.tripi.model.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class EditTripFragment extends Fragment {

    Trip editedTrip;
    String tripId;
    EditText etTripLocation;
    EditText etTripDescription;
    EditText etTripLevel;
    ImageView img;
    Uri imageUri;
    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_edit_trip, container, false);
        etTripLocation = view.findViewById(R.id.editTextLocation);
        etTripDescription = view.findViewById(R.id.editTextDescription);
        etTripLevel = view.findViewById(R.id.editTextLevel);
        img = view.findViewById(R.id.imageView2);

        tripId = getArguments().getString("tripId");
        etTripLocation.setText(getArguments().getString("tripLocation"));
        etTripDescription.setText(getArguments().getString("tripDescription"));
        etTripLevel.setText(getArguments().getString("tripLevel"));
        imageUri = Uri.parse(getArguments().getString("tripImgUrl"));
        Picasso.get().load(imageUri).into(img);

        Button browse = view.findViewById(R.id.browseButton);
        Button saveBtn = view.findViewById(R.id.saveButton);

        browse.setOnClickListener(event -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryActivityResultLauncher.launch(galleryIntent);
        });

        saveBtn.setOnClickListener(event -> {
            try {
                Model.Listener<String> listener = new Model.Listener<String>() {
                    @Override
                    public void onComplete(String uploadedUri) {
                        if (uploadedUri != null) {
                            update(uploadedUri, event);
                        }
                    }
                };

                if (!imageUri.equals(Uri.parse(getArguments().getString("tripImgUrl")))) {
                    Model.instance().uploadImage(imageUri, listener);
                } else {
                    update(imageUri.toString(), event);
                }

            } catch (Exception e) {
                System.out.println("Failed to add trip");
            }
        });

        return view;
    }

    void update(String uploadedUri, View event) {
        Model.instance().addTrip(new Trip(tripId, mAuth.getCurrentUser().getDisplayName(), uploadedUri,
                etTripLocation.getText().toString(),
                etTripDescription.getText().toString(),
                etTripLevel.getText().toString(),
                mAuth.getCurrentUser().getUid()));
        Navigation.findNavController(event).navigate(R.id.action_editTripFragment_to_tripListFragment);
    }

    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imageUri = result.getData().getData();
                        img.setImageURI(imageUri);
                    }
                }
            });
}