package com.example.holistictracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MenuFragment.MenuFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new MenuFragment())
                .commit();
    }

    @Override
    public void launchCamera() {
        Intent intent = new Intent(this, holistic_activity.class);
        startActivity(intent);
    }
}