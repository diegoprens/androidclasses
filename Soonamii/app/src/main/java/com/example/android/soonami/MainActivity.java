/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.soonami;

import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {


    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2012-01-01&endtime=2012-12-01&minmagnitude=6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TsunamiTask task = new TsunamiTask();
        task.execute();
    }

    private void updateUi(Event earthquake) {
        // Display the earthquake title in the UI
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(earthquake.title);

        // Display the earthquake date in the UI
        TextView dateTextView = (TextView) findViewById(R.id.date);
        dateTextView.setText(getDateString(earthquake.time));

        // Display whether or not there was a tsunami alert in the UI
        TextView tsunamiTextView = (TextView) findViewById(R.id.tsunami_alert);
        tsunamiTextView.setText(getTsunamiAlertString(earthquake.tsunamiAlert));
    }

    private String getDateString(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm:ss z");
        return formatter.format(timeInMilliseconds);
    }

    private String getTsunamiAlertString(int tsunamiAlert) {
        switch (tsunamiAlert) {
            case 0:
                return getString(R.string.alert_no);
            case 1:
                return getString(R.string.alert_yes);
            default:
                return getString(R.string.alert_not_available);
        }
    }

    private class TsunamiTask extends AsyncTask<URL, Void, Event>{

        @Override
        protected Event doInBackground(URL... urls) {
            URL url = createURL(USGS_REQUEST_URL);
            String jsonResponse = null;
            try {
                jsonResponse = makeHttpRequest(url);

            } catch(Exception e){
                Log.e(LOG_TAG, "Problema haciendo la solicitud HTTP");
            }

            Event earthquake = extractEventFromJson(jsonResponse);

            return earthquake;
        }

        @Override
        protected void onPostExecute(Event earthquake){
            if( earthquake == null ) return;
            updateUi(earthquake);
        }

        private Event extractEventFromJson(String earthquake){
            if( TextUtils.isEmpty(earthquake) ) return null;
            try {
               JSONObject base = new JSONObject(earthquake);
               JSONArray features = base.getJSONArray("features");

               if( features.length() > 0 ){
                   JSONObject firstObject = features.getJSONObject(0);
                   JSONObject properties = firstObject.getJSONObject("properties");

                   String place = properties.getString("place");
                   long time = properties.getLong("time");
                   int tsunamiAlert = properties.getInt("tsunami");

                   return new Event(place,time,tsunamiAlert);
               }
            }
            catch(JSONException e){
                Log.e(LOG_TAG, "Problema parseando el JSON");
            }
            return null;
        }

        private URL createURL(String stringUrl){
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e){
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException{
            String jsonResponse = "";
            if( url == null ) return jsonResponse;

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if( urlConnection.getResponseCode() == 200 ){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
            }
            catch (IOException e){
                Log.e(LOG_TAG, "Problema recuperando los resultados del JSON.", e);
            }
            finally {
                if(urlConnection != null) urlConnection.disconnect();
                if( inputStream != null ) inputStream.close();
            }

            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }


    }

}
