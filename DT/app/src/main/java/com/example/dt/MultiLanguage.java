package com.example.dt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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

    TextView list;
    Spinner sp1, sp2;
    ArrayList<JSONObject> listLang = new ArrayList<JSONObject>();
    ArrayList<String> lang = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_language);

        list = (TextView) findViewById(R.id.list);
        sp1 = (Spinner) findViewById(R.id.spinner);
        sp2 = (Spinner) findViewById(R.id.spinner2);

        GetListLang();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lang);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp1.setAdapter(adapter);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp2.setAdapter(adapter);

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

                            //list.setText(jsonArray.toString());

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    public String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);
    }
}