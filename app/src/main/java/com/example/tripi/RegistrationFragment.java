package com.example.tripi;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FirebaseAuth mAuth;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        View button = view.findViewById(R.id.registerButton);

        button.setOnClickListener((view1)->{
            EditText mail = (EditText)view.findViewById(R.id.editTextTextEmailAddress);
            EditText password = (EditText)view.findViewById(R.id.editPassword);
            EditText dispName = (EditText)view.findViewById(R.id.editTextName);
            createAccount(mail.getText().toString(), password.getText().toString(), dispName.getText().toString());
        });

        return view;
    }

    private void createAccount(String email, String password, String dispName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(dispName).setPhotoUri(Uri.parse("https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Israel-2013-Aerial_21-Masada.jpg/1200px-Israel-2013-Aerial_21-Masada.jpg")).build();
                            user.updateProfile(profileUpdates);
                            System.out.println("Success");
                        } else {
                            System.out.println("Failure: " + task.getException());
                        }
                    }
                });
    }
}