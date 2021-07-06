package com.example.dt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DictionaryRequest extends AsyncTask<String, Integer, String> {

    //in android calling network requests on the main thread forbidden by default
    //create class to do async job

    final Context context;
    final TextView txtSpelling;
    final TextView txtCategory;
    final LinearProgressIndicator loading;

    final MediaPlayer mediaPlayer;

    DictionaryRequest(Context context, TextView txtSpelling, TextView txtCategory, LinearProgressIndicator loading, MediaPlayer mediaPlayer){
        this.context = context;
        this.txtSpelling = txtSpelling;
        this.txtCategory = txtCategory;
        this.loading = loading;
        this.mediaPlayer = mediaPlayer;
    }
    public String urlAudio = "";
    public String phoneticSpelling;
    public String CategoryInfo;

    @Override
    protected String doInBackground(String... params) {

        //TODO: replace with your own app id and app key
        final String app_id = "46384e09";
        final String app_key = "bc84d98b945a4fa053e177bcfe23fb4b";
        try {
            URL url = new URL(params[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            return stringBuilder.toString();

        }
        catch (Exception e) {
            //e.printStackTrace();
            return e.toString();
        }
    }



    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        loading.hide();
        try {
            JSONObject js = new JSONObject(result);
            JSONArray results = js.getJSONArray("results");

            JSONObject Entries_1 = results.getJSONObject(0);
            JSONArray array_1 = Entries_1.getJSONArray("lexicalEntries");

            JSONObject entries = array_1.getJSONObject(0);
            JSONArray e = entries.getJSONArray("entries");
            JSONObject lexicalCategory = entries.getJSONObject("lexicalCategory");

            JSONObject pronunciations = e.getJSONObject(0);
            JSONArray p = pronunciations.getJSONArray("pronunciations");

            urlAudio = p.getJSONObject(0).getString("audioFile");
            //textView.setText(urlAudio);
            phoneticSpelling = p.getJSONObject(0).getString("phoneticSpelling");

            CategoryInfo = lexicalCategory.getString("text");

            txtSpelling.setText(phoneticSpelling);
            txtCategory.setText(CategoryInfo);
            try {
                mediaPlayer.setDataSource(urlAudio);
                mediaPlayer.prepare();
                //mediaPlayer.start();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
