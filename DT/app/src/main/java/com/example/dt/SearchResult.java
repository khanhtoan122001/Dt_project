package com.example.dt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

public class SearchResult extends AppCompatActivity {

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        result = intent.getStringExtra("eng");
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle(result);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.speak:

                        return true;
                    default:break;
                }
                return false;
            }
        });
        try {
            Translate translateRequest = new Translate(result);
            String response = translateRequest.Post();
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(Translate.prettify(response));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}