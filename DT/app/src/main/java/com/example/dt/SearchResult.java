package com.example.dt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;

public class SearchResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        String eng = intent.getStringExtra("eng");
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle(eng);
    }
}