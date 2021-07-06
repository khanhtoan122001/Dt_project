package com.example.dt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.android.material.appbar.MaterialToolbar;
//import com.squareup.okhttp.OkHttpClient;

import okhttp3.OkHttpClient;


public class SearchResult extends AppCompatActivity {
    //final String result;
    String url;
    TextView txtSpelling, txtCategory, txtTranslations, txtLookup, txtExamples;
    String eng;
    MediaPlayer mediaPlayer = new MediaPlayer();
    DictionaryRequest dr;
    MaterialToolbar toolbar;

    //String subscriptionKey = "5840f6cb5483475b8219c76b7f49a78a";
    //String location = "southeastasia";
    OkHttpClient client = new OkHttpClient();
    Translate translate;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        eng = intent.getStringExtra("eng");
        toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        toolbar.setTitle(eng);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.speak:
                        playMedia();
                        return true;
                    case R.id.search:
                        Intent intent1 = new Intent(SearchResult.this, Eng_Vie.class);
                        startActivity(intent1);
                        return true;
                    case R.id.main:
                        Intent intent2 = new Intent(SearchResult.this, MainActivity.class);
                        startActivity(intent2);
                }
                return false;
            }
        });
        txtSpelling = (TextView) findViewById(R.id.txtspelling);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtTranslations = (TextView) findViewById(R.id.txtTranslations);
        txtLookup = (TextView) findViewById(R.id.txtLookup);
        txtExamples = (TextView) findViewById(R.id.txtExamples);

        //translate = new Translate(eng, "en", "vi", txtTranslations, txtLookup, txtExamples, this);
        translate = new Translate(eng, "vi", "en", txtTranslations, txtLookup, txtExamples, this);
        translate.Run();
        //translate.GetLookup();
        sendRequest();
        //playMedia();
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
        dr = new DictionaryRequest(this, txtSpelling, txtCategory, mediaPlayer);
        url = dictionaryEntries("pronunciations");
        dr.execute(url);
    }


}