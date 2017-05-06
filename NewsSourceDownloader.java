package com.example.k.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by K on 4/30/2017.
 */

public class NewsSourceDownloader extends AsyncTask<String, Integer, String> {

    private MainActivity ma;
    private String category;
    private String catString;
    private int count;



    private final String newsSourcesURL="https://newsapi.org/v1/sources?language=en&country=us";
    private static final String apiKeyStr="&apiKey=240c6f9809b34e84a66a24ff0f672e90";
    //"https://newsapi.org/v1/sources?language=en&country=us&apiKey=______"
    //by category
    //"https://newsapi.org/v1/sources?language=en&country=us&category=______&apiKey=____"

    private static final String TAG = "NewsSourceDownloader";

    public NewsSourceDownloader(MainActivity mainActivity, String c){
        ma=mainActivity;
        category=c;
        if(!c.equals("")){
            catString="&category="+c;
        }else{catString="";}
        Log.d(TAG, "constructor");}

    public NewsSourceDownloader(String c){
        category=c;
        if(!c.equals("")){
            catString="&category="+c;
        }else{catString="";}
        Log.d(TAG, "constructor for test");}


    @Override
    protected void onPreExecute() {
        Log.d(TAG, "Loading source data");
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<Source> sourceArrayList = parseJSON(s);//s should not be null!!!
        ma.updateSource(sourceArrayList);
        Log.d(TAG, "onPostExecute");
    }

    @Override
    protected String doInBackground(String... params) {

        Uri dataUri = Uri.parse(newsSourcesURL +catString+ apiKeyStr); //must be changed to real URL
        String urlToUse=dataUri.toString();
        Log.d(TAG, "doInBackground:" + urlToUse);

        StringBuilder sb = new StringBuilder();

        try{
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));//hasn't crashed yet fabulous

            String line;
            while((line=reader.readLine())!=null){
                sb.append(line).append('\n'); //CHECK AND CHANGE
            }

            Log.d(TAG, "doInBackground: "+sb.toString());

        } catch (Exception e){
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " +sb.toString());

        return sb.toString();
    }

    public String getJSON()
    {
        Uri dataUri = Uri.parse(newsSourcesURL +catString+ apiKeyStr); //must be changed to real URL
        String urlToUse=dataUri.toString();
        Log.d(TAG, "doInBackground:" + urlToUse);

        StringBuilder sb = new StringBuilder();

        try{
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));//hasn't crashed yet fabulous

            String line;
            while((line=reader.readLine())!=null){
                sb.append(line).append('\n'); //CHECK AND CHANGE
            }

            Log.d(TAG, "doInBackground: "+sb.toString());

        } catch (Exception e){
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " +sb.toString());

        return sb.toString();

    }

    public ArrayList<Source> parseJSON(String s){

        ArrayList<Source> sourceArrayList = new ArrayList<>();

        try{
            JSONObject jObjMain= new JSONObject(s);//this line causes the crash! wow debugging is the best. -> now changed from Array to Object.
            JSONArray jSources = (JSONArray)jObjMain.get("sources");
            count =jSources.length();


            for(int i=0;i<count;i++){
                JSONObject jSourcesJSONObject = (JSONObject)jSources.getJSONObject(i);
                String id=(String)jSourcesJSONObject.get("id");
                String name=(String)jSourcesJSONObject.get("name");
                String url=(String)jSourcesJSONObject.get("url");
                String category=(String)jSourcesJSONObject.get("category");

                sourceArrayList.add(new Source(id, name, url, category));
            }

            return sourceArrayList;

        }catch (Exception e){

            Log.d(TAG, "parseJSON: "+ e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
