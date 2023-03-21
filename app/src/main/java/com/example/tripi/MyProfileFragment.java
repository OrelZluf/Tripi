package com.example.tripi;

import static java.util.UUID.randomUUID;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tripi.model.Model;
import com.example.tripi.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.auth.User;

public class MyProfileFragment extends Fragment {
    EditText nameEt;
    EditText emailEt;
    EditText passwordEt;

    private FirebaseAuth mAuth;

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


        nameEt.setText(mAuth.getCurrentUser().getDisplayName());
        emailEt.setText(mAuth.getCurrentUser().getEmail());

        // TODO: Add image
        //mAuth.getCurrentUser().updateProfile()

        saveBtn.setOnClickListener(event -> {
            nameEt = view.findViewById(R.id.editTextName);
            emailEt = view.findViewById(R.id.editTextTextEmailAddress);
            passwordEt = view.findViewById(R.id.editPassword);
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nameEt.getText().toString())
                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                    .build();
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
        });

        cancelBtn.setOnClickListener(event -> {
            Navigation.findNavController(event).navigate(R.id.action_myProfileFragment_to_tripListFragment);
        });

        // Inflate the layout for this fragment
        return view;
    }
}