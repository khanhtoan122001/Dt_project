package com.example.dt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Click1(View view)
    {
        Intent intent = new Intent(this, Eng_Vie.class);
        startActivity(intent);
    }
    public void Click2(View view)
    {
        Intent intent = new Intent(this, MultiLanguage.class);
        startActivity(intent);
    }

}