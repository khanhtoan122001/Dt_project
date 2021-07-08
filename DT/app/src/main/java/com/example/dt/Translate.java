package com.example.dt;

import android.content.Context;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.progressindicator.LinearProgressIndicator;

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
    String text, to, from;
    TextView txtTranslations, txtLookup, txtExamples;
    Context context;
    LinearProgressIndicator loading;
    private static String subscriptionKey = "5840f6cb5483475b8219c76b7f49a78a";

    List<String> lookup = new ArrayList<>();
    List<String> listexamples = new ArrayList<>();
    public String translations;
    // Add your location, also known as region. The default is global.
    // This is required if using a Cognitive Services resource.
    private static String location = "southeastasia";

    public Translate(String text, String to, String from, TextView txtTranslations, TextView txtLookup, TextView txtExamples, Context context, LinearProgressIndicator loading) {
        this.text = text;
        this.to = to;
        this.from = from;
        this.txtTranslations = txtTranslations;
        this.txtLookup = txtLookup;
        this.txtExamples = txtExamples;
        this.context = context;
        this.loading = loading;
    }

    public Translate(String text, String to, String from, TextView txtTranslations, LinearProgressIndicator loading){
        this.txtTranslations = txtTranslations;
        this.text = text;
        this.to = to;
        this.from = from;
        this.loading = loading;
    }

    void Run(){
        loading.show();
        GetTranslations();
        GetLookup();
        //loading.hide();
    }

    HttpUrl GetUrlTranslate(String from, String to){
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/translate")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("from", from)
                .addQueryParameter("to", to)
                .build();
    }

    HttpUrl GetUrlLookup(String from, String to){
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/dictionary/lookup")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("from", from)
                .addQueryParameter("to", to)
                .build();
    }

    HttpUrl GetUrlExamples(String from, String to){
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/dictionary/examples")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("from", from)
                .addQueryParameter("to", to)
                .build();
    }

    // Instantiates the OkHttpClient.
    //OkHttpClient client = new OkHttpClient();
    //String result = "";

    // This function performs a POST request.
    private Request getTranstaleRequest(String text) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \"" + text + "\"}]");
        return new Request.Builder().url(GetUrlTranslate(from, to)).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", "application/json")
                .build();
    }

    private Request getLookupRequest(String text) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \"" + text + "\"}]");
        return new Request.Builder().url(GetUrlLookup(from, to)).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", "application/json")
                .build();
    }

    private void GetTransExamples(){
        loading.show();
        for(String item : listexamples){
            OkHttpClient client = new OkHttpClient();
            try {
                client.newCall(getTranstaleRequest(item)).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.hide();
                                try {
                                    txtExamples.setText(txtExamples.getText() + "\n\n" + item + "\n" + GetStrTranslation(response.body().string()));
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void GetTranslations(){
        loading.show();
        OkHttpClient client = new OkHttpClient();
        try {
            client.newCall(this.getTranstaleRequest(text)).enqueue(new Callback() {
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
                                    txtTranslations.setText(GetStrTranslation(response.body().string()));
                                    //txtTranslations.setText(response.body().string());
                                    //txtTranslations.setTextSize(20);
                                    loading.hide();
                                } catch (IOException | JSONException e) {
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

    public void GetLookup(){
        OkHttpClient client = new OkHttpClient();
        loading.show();
        try {
            client.newCall(this.getLookupRequest(text)).enqueue(new Callback() {
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
                                    String lookupStr = GetStrLookup(response.body().string());

                                    GetExamples();

                                    loading.hide();

                                    txtLookup.setText(lookupStr);
                                    //txtLookup.setTextSize(15);
                                } catch (IOException | JSONException e) {
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
    }

    public Request getRequestExamples(String trans) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \""+ text +"\", \"Translation\": \"" + trans + "\"}]");
        Request request = new Request.Builder().url(GetUrlExamples(from, to)).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", "application/json")
                .build();
        return request;
    }

    public void GetExamples(){
        OkHttpClient client = new OkHttpClient();
        for(String item : lookup){
            loading.show();
            try {
                client.newCall(getRequestExamples(item)).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.hide();
                                try {
                                    listexamples.clear();
                                    JSONArray js = new JSONArray(response.body().string());
                                    JSONObject results = js.getJSONObject(0);
                                    //String txt = "";
                                    JSONArray examples = results.getJSONArray("examples");
                                    for(int i = 0; i < 2 && i < examples.length(); i++){
                                        JSONObject example = examples.getJSONObject(i);
                                        String source = example.getString("sourcePrefix");
                                        source += example.getString("sourceTerm")
                                                + example.getString("sourceSuffix");
                                        String target = example.getString("targetPrefix");
                                        target += example.getString("targetTerm")
                                                + example.getString("targetSuffix");
                                        //txt += source + "\n" + target + "\n\n";
                                        listexamples.add(source);
                                    }
                                    GetTransExamples();
                                    //txtExamples.setText(String.format("%s\n%s", txtExamples.getText(), txt));
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String GetStrTranslation(String str) throws JSONException {
        JSONArray js = new JSONArray(str);
        JSONObject results = js.getJSONObject(0);

        JSONArray translations = results.getJSONArray("translations");

        JSONObject txt = translations.getJSONObject(0);

        return txt.getString("text");
    }

    public String GetStrLookup(String str) throws JSONException {
        JSONArray js = new JSONArray(str);
        JSONObject results = js.getJSONObject(0);
        String lookupStr = "";
        JSONArray translations = results.getJSONArray("translations");
        for(int i = 0; i < translations.length() && i < 5; i++){
            JSONObject obj = translations.getJSONObject(i);
            lookupStr += obj.getString("normalizedTarget");
            lookup.add(obj.getString("normalizedTarget"));
            if(i != translations.length() - 1)
                lookupStr += ", ";
        }

        return lookupStr;
    }

    // This function prettifies the json response.
    /*public String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);
    }*/

}