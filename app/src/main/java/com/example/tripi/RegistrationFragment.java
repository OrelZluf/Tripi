package com.example.tripi;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.EditText;
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
        View browseBtn = view.findViewById(R.id.profilePictureButton);
        View button = view.findViewById(R.id.registerButton);
        TextView failedRegistrationText = (TextView) view.findViewById(R.id.failedRegistration);

        button.setOnClickListener((view1)->{
            EditText mail = (EditText)view.findViewById(R.id.editTextTextEmailAddress);
            EditText password = (EditText)view.findViewById(R.id.editPassword);
            EditText dispName = (EditText)view.findViewById(R.id.editTextName);
            createAccount(mail.getText().toString(), password.getText().toString(), dispName.getText().toString(), view1, failedRegistrationText);
        });

        browseBtn.setOnClickListener((view2) -> {
            galleryLauncher.launch("image/*");
        });

        return view;
    }

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
                                    // Not working
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(MyApplication.getAppContext().getContentResolver(), imageUri);
                                    Model.instance().uploadImage(user.getUid(), bitmap, url-> {
                                        if (url != null) {
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(dispName).setPhotoUri(Uri.parse(url)).build();
                                            user.updateProfile(profileUpdates);
                                            Navigation.findNavController(view).navigate(R.id.action_registrationFragment_to_tripListFragment);
                                        }
                                    });
                                } catch (IOException e) {
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