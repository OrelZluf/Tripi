package com.example.tripi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    HeaderFragment frag1;
    Fragment inDisplay;
    FragmentManager manager;
    FragmentTransaction tran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();
        tran = manager.beginTransaction();

        frag1 = new HeaderFragment();
        displayFragment(frag1);
    }

    private void displayFragment(Fragment frag) {
        tran.remove(frag);
        tran.add(R.id.headerFragment_container, frag);

        if (inDisplay != null) tran.remove(inDisplay);
        tran.addToBackStack("TAG");
        tran.commit();
        inDisplay = frag;
    }
}