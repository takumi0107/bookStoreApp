package com.example.bookstoreapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.acrivity_main2);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame2, new RecyclerViewFragment()).commit();
    }
}
