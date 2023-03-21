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
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tripi.model.Model;
import com.example.tripi.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

public class MyProfileFragment extends Fragment {
    EditText nameEt;
    EditText emailEt;
    EditText passwordEt;
    Uri imageUri;
    ImageView userImg;

    private FirebaseAuth mAuth;
    private View view;

    public MyProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        Button saveBtn = view.findViewById(R.id.saveButton);
        Button cancelBtn = view.findViewById(R.id.cancelButton);
        nameEt = view.findViewById(R.id.editTextName);
        emailEt = view.findViewById(R.id.editTextTextEmailAddress);
        passwordEt = view.findViewById(R.id.editPassword);
        userImg = view.findViewById(R.id.imageView2);

        nameEt.setText(mAuth.getCurrentUser().getDisplayName());
        emailEt.setText(mAuth.getCurrentUser().getEmail());

        Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).into(userImg);

        Button browse = view.findViewById(R.id.browseButton);
        Button myTripsBtn = view.findViewById(R.id.myTripsButton);

        browse.setOnClickListener(event -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryActivityResultLauncher.launch(galleryIntent);
        });

        myTripsBtn.setOnClickListener(event -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("myTrips", true);
            Navigation.findNavController(event).navigate(R.id.action_myProfileFragment_to_tripListFragment,bundle);
        });

        saveBtn.setOnClickListener(event -> {
            nameEt = view.findViewById(R.id.editTextName);
            emailEt = view.findViewById(R.id.editTextTextEmailAddress);
            passwordEt = view.findViewById(R.id.editPassword);
            try {
                Model.Listener<String> listener = new Model.Listener<String>() {
                    @Override
                    public void onComplete(String uploadedUri) {
                        if (uploadedUri != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(nameEt.getText().toString()).setPhotoUri(Uri.parse(uploadedUri)).build();

                            // update display name, image and email
                            if (!mAuth.getCurrentUser().updateProfile(profileUpdates).isSuccessful() ||
                                    !mAuth.getCurrentUser().updateEmail(emailEt.getText().toString()).isSuccessful()){
                                // TODO: print error while update
                            }

                            // update password
                            if(!passwordEt.getText().toString().isEmpty()){
                                if(!mAuth.getCurrentUser().updatePassword(passwordEt.getText().toString()).isSuccessful()){
                                    // TODO: print error
                                    String err = "err";
                                }
                            }

                            Navigation.findNavController(event).navigate(R.id.action_myProfileFragment_to_tripListFragment);
                        }
                    }
                };

                Model.instance().uploadImage(imageUri, listener);
            } catch (Exception e) {
                System.out.println("Error uploading image, " + e);
            }

        });

        cancelBtn.setOnClickListener(event -> {
            Navigation.findNavController(event).navigate(R.id.action_myProfileFragment_to_tripListFragment);
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
                        userImg.setImageURI(image_uri);
                    }
                }
            });
}