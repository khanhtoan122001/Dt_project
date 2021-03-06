package com.example.dt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.provider.SearchRecentSuggestions;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/*import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;*/

import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MultiLanguage extends AppCompatActivity {

    TextView result;
    Spinner sp1, sp2;
    String from = "", to = "";
    ArrayList<JSONObject> listLang = new ArrayList<JSONObject>();
    ArrayList<String> lang = new ArrayList<>();
    ArrayAdapter adapter;//ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_dropdown_item_1line);
    //SearchView searchView;
    EditText editText;
    LinearProgressIndicator loading;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_language);

        sp1 = (Spinner) findViewById(R.id.spinner);
        sp2 = (Spinner) findViewById(R.id.spinner2);

        Intent intent = getIntent();
        text = intent.getStringExtra("eng");


        //searchView = (SearchView) findViewById(R.id.search_view);

        result = (TextView) findViewById(R.id.txtResult);
        editText = (EditText) findViewById(R.id.text_content);
        loading = (LinearProgressIndicator) findViewById(R.id.loading2);
        loading.hide();
        GetListLang();

        result.setMovementMethod(new ScrollingMovementMethod());

        adapter = new ArrayAdapter(this, R.layout.spinner_item, lang);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JSONObject obj = listLang.get(position);
                try {
                    from = obj.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JSONObject obj = listLang.get(position);
                try {
                    to = obj.getString("code");
                    Translate translate = new Translate(editText.getText().toString(), to, from, result, loading);
                    translate.GetTranslations();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Translate translate = new Translate(s.toString(), to, from, result, loading);
                translate.GetTranslations();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Translate translate = new Translate(query, to, from, result, loading);
                translate.GetTranslations();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void GetListLang(){
        /*HttpUrl listLang = new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/languages")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("scope", "translation")
                .build();
        String subscriptionKey = "5840f6cb5483475b8219c76b7f49a78a";
        String location = "southeastasia";*/

        Request request = new Request.Builder().url("https://api.cognitive.microsofttranslator.com/languages?api-version=3.0&scope=translation")
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //list.setText(prettify(response.body().string()));
                            JSONObject js = new JSONObject(response.body().string());

                            JSONObject trans = js.getJSONObject("translation");

                            //JSONArray jsonArray = new JSONArray();//trans.toJSONArray(trans.names());

                            Iterator x = trans.keys();

                            while (x.hasNext()){
                                String key = (String) x.next();

                                JSONObject obj = (JSONObject) trans.get(key);

                                obj.put("code", key);

                                listLang.add(obj);

                                lang.add(obj.getString("name"));

                                //jsonArray.put(obj);
                            }

                            sp1.setAdapter(adapter);
                            sp2.setAdapter(adapter);

                            if(text == null){
                                text = "hi";
                            }
                            else{
                                editText.setText(text);
                            }

                            sp2.setSelection(85);

                            //list.setText(jsonArray.toString());

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    /*public String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);
    }*/
}