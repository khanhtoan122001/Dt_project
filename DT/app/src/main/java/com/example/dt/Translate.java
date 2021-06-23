package com.example.dt;

import android.content.Context;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

import com.google.gson.*;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Translate extends AppCompatActivity {
    final String text;
    final TextView textView;
    final Context context;
    private static String subscriptionKey = "5840f6cb5483475b8219c76b7f49a78a";

    public String translations;
    // Add your location, also known as region. The default is global.
    // This is required if using a Cognitive Services resource.
    private static String location = "southeastasia";

    HttpUrl urlTranslate = new HttpUrl.Builder()
            .scheme("https")
            .host("api.cognitive.microsofttranslator.com")
            .addPathSegment("/translate")
            .addQueryParameter("api-version", "3.0")
            .addQueryParameter("from", "en")
            .addQueryParameter("to", "vi")
            .build();
    HttpUrl urlLookup = new HttpUrl.Builder()
            .scheme("https")
            .host("api.cognitive.microsofttranslator.com")
            .addPathSegment("/dictionary/lookup")
            .addQueryParameter("api-version", "3.0")
            .addQueryParameter("from", "en")
            .addQueryParameter("to", "vi")
            .build();
    HttpUrl urlExamples = new HttpUrl.Builder()
            .scheme("https")
            .host("api.cognitive.microsofttranslator.com")
            .addPathSegment("/dictionary/examples")
            .addQueryParameter("api-version", "3.0")
            .addQueryParameter("from", "en")
            .addQueryParameter("to", "vi")
            .build();
    public Translate(String text, TextView textView, Context context) {
        this.text = text;
        this.textView = textView;
        this.context = context;
    }

    // Instantiates the OkHttpClient.
    //OkHttpClient client = new OkHttpClient();
    //String result = "";

    // This function performs a POST request.
    private Request getRequestTranstale() throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \"" + text + "\"}]");
        Request request = new Request.Builder().url(urlTranslate).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", "application/json")
                .build();
        return request;
    }

    public void GetTranslations(){
        OkHttpClient client = new OkHttpClient();
        try {
            client.newCall(this.getRequestTranstale()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    CharSequence textViewstr = textView.getText();
                                    /*JSONObject js = new JSONObject(response.body().string());
                                    JSONArray results = js.getJSONArray("results");*/
                                    textView.setText(textViewstr + prettify(response.body().string()));
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
        }
        //return translations;
    }

    public Request getRequestExamples() throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \"Shark\", \"Translation\": \"tiburón\"}]");
        Request request = new Request.Builder().url(urlExamples).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", "application/json")
                .build();
        return request;
    }

    // This function prettifies the json response.
    public String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

}