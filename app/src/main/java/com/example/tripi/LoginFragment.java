package com.example.tripi;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FirebaseAuth mAuth;

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        View button = view.findViewById(R.id.loginButton);
        TextView failedLoginText = (TextView) view.findViewById(R.id.failedLogin);

        button.setOnClickListener((view1)->{
            EditText mail = (EditText)view.findViewById(R.id.editTextTextEmailAddress);
            EditText password = (EditText)view.findViewById(R.id.editPassword);
            signIn(mail.getText().toString(), password.getText().toString(), view1, failedLoginText);
        });

        return view;
    }

    private void signIn(String email, String password, View view, TextView failedLogin) {
        if (email.isEmpty() || password.isEmpty()) failedLogin.setVisibility(View.VISIBLE);
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_tripListFragment);
                            } else {
                                failedLogin.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }
}