package com.example.dt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.android.material.appbar.MaterialToolbar;

import okhttp3.OkHttpClient;


public class SearchResult extends AppCompatActivity {
    //final String result;
    String url;
    TextView textView;
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
                }
                return false;
            }
        });
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("");
        translate = new Translate(eng, textView, this);
        translate.GetTranslations();
        sendRequest();
        playMedia();
        /*try {
            client.newCall(translate.getRequestTranstale()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.print(e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                try {
                                    textView.setText(translate.prettify(response.body().string()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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