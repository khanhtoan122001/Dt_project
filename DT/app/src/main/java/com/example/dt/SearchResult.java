package com.example.dt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.google.android.material.appbar.MaterialToolbar;


public class SearchResult extends AppCompatActivity {
    //final String result;

    String url;
    TextView textView;
    String eng;
    MediaPlayer mediaPlayer = new MediaPlayer();
    DictionaryRequest dr;


    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        eng = intent.getStringExtra("eng");
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle(eng);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.speak:
                        playMedia();
                        return true;
                }
                return false;
            }
        });
        textView = (TextView) findViewById(R.id.textView);
        sendRequest();
        playMedia();
    }

    public void playMedia(){
        mediaPlayer.start();
        //textView.setText("");
    }

    private String dictionaryEntries(String field) {
        final String language = "en-gb";
        final String word = eng;
        final String fields = field;
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }

    public void sendRequest(){
        dr = new DictionaryRequest(this, textView, mediaPlayer);
        url = dictionaryEntries("pronunciations");
        dr.execute(url);
    }

}