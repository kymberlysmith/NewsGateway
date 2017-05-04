package com.example.k.newsgateway;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;
import android.content.BroadcastReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by K on 4/30/2017.
 */

public class NewsArticleDownloader extends AsyncTask<String, Integer, String>{

    private MainActivity ma;
    private String category;
    private String catString;
    private int count;
    private String source;

    private static final String BROADCAST_ARTICLES_LIST="BROADCAST_ARTICLES_LIST";
    private static final String ARTICLES_LIST="ARTICLES_LIST";

    private final String newsArticlesURL="https://newsapi.org/v1/articles?source=";
    private static final String apiKeyStr="&apiKey=240c6f9809b34e84a66a24ff0f672e90";

    private static final String TAG = "NewsArticlesDownloader";

    public NewsArticleDownloader( String s){
        source=s;
        Log.d(TAG, "constructor");}

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "Loading article data");
    }

    @Override
    protected void onPostExecute(String s) {
        parseJSON(s);//s should not be null!!!


    }

    @Override
    protected String doInBackground(String... params) {

        Uri dataUri = Uri.parse(newsArticlesURL +source+ apiKeyStr); //must be changed to real URL
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

    private void parseJSON(String s){

        List articleArrayList = Collections.synchronizedList(new ArrayList<>());


        try{
            JSONObject jObjMain= new JSONObject(s);//this line causes the crash! wow debugging is the best. -> changed from Array to Object.
            JSONArray jArticles = (JSONArray)jObjMain.get("articles");
            count =jArticles.length();

            for(int i=0;i<count;i++){
                JSONObject jArticlesJSONObject = (JSONObject)jArticles.getJSONObject(i);

                String author="author"; String title="title";String description="description";
                String url="url"; String urlToImage="urlToImage"; String publishedAt="publishedAt";

                String[] attributes={author, title, description, url, urlToImage, publishedAt};
                ArrayList<String> temp= new ArrayList<>();

                for(String str: attributes)
                {
                    Object foo = jArticlesJSONObject.get(str);

                    if(!(foo.toString().equals("null")))
                    {
                        Log.d(TAG, "foo is not null, foo = "+ foo);
                        temp.add((String)foo);
                    }
                    else
                    {
                        Log.d(TAG, "foo is null");
                        temp.add("");
                    }

                }
                /*author = "author";
                title="title";
                description="description";
                url="url";
                urlToImage="urlToImage";
                publishedAt="publishedAt";




                title=(String)jArticlesJSONObject.get("title");
                description=(String)jArticlesJSONObject.get("description");
                url=(String)jArticlesJSONObject.get("url");
                urlToImage=(String)jArticlesJSONObject.get("urlToImage");
                publishedAt=(String)jArticlesJSONObject.get("publishedAt");*/

                Article tempArticle = new Article(temp.get(0), temp.get(1), temp.get(2), temp.get(3), temp.get(4), source, temp.get(5));

                articleArrayList.add(tempArticle);
                Log.d(TAG, "Added to articleArrayList: "+ tempArticle.getTitle());
            }


            NewsService ns = new NewsService();
            ns.setArticles(articleArrayList);

        }catch (Exception e){
            //apparently i am attempting to clear a null arraylist???
            //now i am casting JSON object to JSON string
            Log.d(TAG, "parseJSON: "+ e.getMessage());
            e.printStackTrace();
        }
    }
}
