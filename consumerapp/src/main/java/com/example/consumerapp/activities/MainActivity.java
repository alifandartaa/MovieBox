package com.example.consumerapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.consumerapp.R;
import com.example.consumerapp.fragment.FavoriteFragment;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FavoriteFragment favoriteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        favoriteFragment = new FavoriteFragment();
        fragmentTransaction.replace(R.id.frame_test, favoriteFragment);
        fragmentTransaction.commit();
    }
}
