package com.example.tripi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tripi.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    protected Uri imageUri;
    private FirebaseAuth mAuth;
    ActivityResultLauncher<String> galleryLauncher;
    ImageView userImg;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    imageUri = result;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        View browseBtn = view.findViewById(R.id.browseButton);
        View button = view.findViewById(R.id.registerButton);
        TextView failedRegistrationText = (TextView) view.findViewById(R.id.failedRegistration);
        userImg = view.findViewById(R.id.imageView2);
        
        button.setOnClickListener((view1)->{
            EditText mail = (EditText)view.findViewById(R.id.editTextTextEmailAddress);
            EditText password = (EditText)view.findViewById(R.id.editPassword);
            EditText dispName = (EditText)view.findViewById(R.id.editTextName);
            createAccount(mail.getText().toString(), password.getText().toString(), dispName.getText().toString(), view1, failedRegistrationText);
        });

        browseBtn.setOnClickListener(event -> {
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
                        imageUri = result.getData().getData();
                        userImg.setImageURI(imageUri);
                    }
                }
            });

    private void createAccount(String email, String password, String dispName, View view, TextView failedRegistration) {
        if (email.isEmpty() || password.isEmpty() || dispName.isEmpty() || imageUri.toString().isEmpty()) failedRegistration.setVisibility(View.VISIBLE);
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                try {
                                    Model.Listener<String> listener = new Model.Listener<String>() {
                                        @Override
                                        public void onComplete(String uploadedUri) {
                                            if (uploadedUri != null) {
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(dispName).setPhotoUri(Uri.parse(uploadedUri)).build();
                                                user.updateProfile(profileUpdates);
                                                Navigation.findNavController(view).navigate(R.id.action_registrationFragment_to_tripListFragment);
                                            }
                                        }
                                    };

                                    Model.instance().uploadImage(imageUri, listener);
                                } catch (Exception e) {
                                    failedRegistration.setVisibility(View.VISIBLE);
                                }
                            } else {
                                failedRegistration.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }
}