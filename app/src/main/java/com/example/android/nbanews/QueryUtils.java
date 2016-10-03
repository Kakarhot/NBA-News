package com.example.android.nbanews;

/**
 * Created by wenshuo on 2016/10/4.
 */
import android.text.TextUtils;
import android.util.Log;

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
import java.util.ArrayList;

public final class QueryUtils {


    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils() {
    }

    public static ArrayList<News> fetchNewsData(String requestedUrl){
        URL url = createUrl(requestedUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG,"Error closing input stream");
        }

        ArrayList<News> news = extractNews(jsonResponse);

        return news;
    }

    private static URL createUrl(String requestedUrl){
        URL url = null;
        try{
            url = new URL(requestedUrl);
        }
        catch(MalformedURLException e){
            Log.e(LOG_TAG,"Problem building the URL");
        }
        return url;
    }


    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse = "";

        if(url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                stream = urlConnection.getInputStream();
                jsonResponse = readFromStream(stream);
            } else
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
        }
        catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving news JSON results");
        }
        finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(stream != null){
                stream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream stream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (stream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(stream,Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    private static ArrayList<News> extractNews(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        ArrayList<News> news = new ArrayList<>();

        try {
            JSONObject JSONRootObject = new JSONObject(jsonResponse);
            JSONObject response = JSONRootObject.optJSONObject("response");
            JSONArray results = response.optJSONArray("results");
            for(int i = 0;i < results.length();i++){
                JSONObject result = results.getJSONObject(i);
                String date = result.optString("webPublicationDate");
                String title = result.optString("webTitle");
                String url = result.optString("webUrl");
                news.add(new News(title,date,url));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        return news;
    }
}