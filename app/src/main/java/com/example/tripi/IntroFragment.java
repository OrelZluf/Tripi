package com.example.tripi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroFragment extends Fragment {
    public IntroFragment() {
        // Required empty public constructor
    }

    public static IntroFragment newInstance(String param1, String param2) {
        IntroFragment fragment = new IntroFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro, container, false);

        View registerBtn = view.findViewById(R.id.registerBtn);
        View loginBtn = view.findViewById(R.id.loginBtn);
        registerBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_introFragment_to_registrationFragment));
        loginBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_introFragment_to_loginFragment));

        return view;
    }
}