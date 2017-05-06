package com.example.k.newsgateway;

import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, Serializable{


    //for  broadcasts:
    static final String BROADCAST_SOURCE_REQUEST="BROADCAST_SOURCE_REQUEST";
    static final String BROADCAST_ARTICLES_REQUEST="BROADCAST_ARTICLES_REQUEST";
    static final String BROADCAST_ARTICLES_LIST="BROADCAST_ARTICLES_LIST";

    static final String ARTICLES_LIST="ARTICLES_LIST";
    static final String SOURCE_DATA = "SOURCE_DATA";
    static final String ARTICLE_DATA= "ARTICLE_DATA";
    
    //
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<String> sourcelist;
    public ArrayList<Article> arrayList;

    private PageAdapter pageAdapter;
    private List<Fragment> articleFragments;
    private ViewPager pager;

    private HashMap<String, Source> sourceMap;

    private String category="";
    private String source;

    private NewsService.ServiceReceiver serviceReceiver;
    private NewsReceiver newsReceiver;

    @Override
    protected void onDestroy() {
        unregisterReceiver(newsReceiver);
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sourcelist= new ArrayList<>();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, sourcelist));
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.opentext, R.string.closedtext);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //handle fragments for swiping
        //still need to handle pic urls and no pic case
        articleFragments=getFragments();

        pageAdapter = new PageAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

        // download sources, all sources at first

        new NewsSourceDownloader(this, category).execute();

        //new stuff //


        Intent i = new Intent(MainActivity.this, NewsService.class);
        startService(i);


        newsReceiver = new NewsReceiver();
        IntentFilter filter1 = new IntentFilter(MainActivity.BROADCAST_ARTICLES_LIST);
        registerReceiver(newsReceiver, filter1);

        Log.d(TAG, "onCreate: end");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean bool = onMenuItemClick(item);

        if(bool){
            new NewsSourceDownloader(this, category).execute();


        }
        //noinspection SimplifiableIfStatement
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle");
            return true;
        }

       return super.onOptionsItemSelected(item);
    }

    private List<Fragment> getFragments(){
        List<Fragment> articleFList=new ArrayList<Fragment>();
        return articleFList;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.left_drawer:
                return false;
            case R.id.all:
                return true;
            case R.id.bus:
                category="business";
                return true;
            case R.id.ent:
                category="entertainment";
                return true;
            case R.id.gaming:
                category="gaming";
                return true;
            case R.id.music:
                category="music";
                return true;
            case R.id.politics:
                category="politics";
                return true;
            case R.id.sci:
                category="science-and-nature";
                return true;
            case R.id.sport:
                category="sport";
                return true;
            case R.id.tech:
                category="technology";
                return true;
            case R.id.general:
                category="general";
                return true;
        }
        return false;
    }

    private void selectItem(int position){
        Toast.makeText(MainActivity.this, "you picked " + sourcelist.get(position), Toast.LENGTH_SHORT).show();
        //open articles!!!4
        //

        //change app bar heading

        setTitle(sourcelist.get(position));

        source =sourceMap.get(sourcelist.get(position)).getiD();
        sendArticlesReq();//seems to work!!! up until this point at least

        //
        ArrayList<Article> fakeArticles = new ArrayList<>();//pageAdapter is null here? seems incorrect
        for(int i=0;i<5; i++){
            fakeArticles.add(new Article("author"+i, "","","", "", "", ""));
        }

        //articleFragments=getFragments();

        updateArticles(fakeArticles);

        //
/*

        pageAdapter=new PageAdapter(getSupportFragmentManager());
        pager=(ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);*/

        Log.d(TAG, "selectItem");

        mDrawerLayout.closeDrawer(mDrawerList);
    }
    private void reDoFragments(int idx) {

        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);

        articleFragments.clear();
        String src = sourcelist.get(idx);
        int count = (int) (Math.random() * 8 + 2);

        for (int i = 0; i < count; i++) {
            articleFragments.add(ArticleFragment.newInstance(src + " Headline #" + (i+1)));
            //pageAdapter.notifyChangeInPosition(i);
        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }


    public void sendSourceReq(View v){
        Intent intent = new Intent();
        intent.setAction(BROADCAST_SOURCE_REQUEST);
        sendBroadcast(intent);
    }

    public void sendArticlesReq(){
        Log.d(TAG, "sendArticlesReq");



        /*newsReceiver = new NewsReceiver();
        IntentFilter filter = new IntentFilter(BROADCAST_ARTICLES_REQUEST);
        registerReceiver(newsReceiver, filter);*/

        Intent intent = new Intent();
        intent.setAction(BROADCAST_ARTICLES_REQUEST);
        intent.putExtra(SOURCE_DATA, source);
        sendBroadcast(intent);

    }

    public void updateArticles(ArrayList<Article> aList){

        //sourcelist.clear();
        //if(!sourceMap.isEmpty()){sourceMap.clear();} causes crash and may not be necessary?

        //sourceMap = new HashMap<>(aList.size());
        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);

        articleFragments.clear();

        for (Article a:aList) {
            articleFragments.add(ArticleFragment.newInstance(a.getAuthor()));
            //pageAdapter.notifyChangeInPosition(i);
        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);

    }

    public void updateSource(ArrayList<Source> sList){

        sourcelist.clear();
        //if(!sourceMap.isEmpty()){sourceMap.clear();} causes crash and may not be necessary?

        sourceMap = new HashMap<>(sList.size());

        for(Source s:sList){
            sourcelist.add(s.getName());
            sourceMap.put(s.getName(), s);
        }

        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, sourcelist));

    }
    public class NewsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "NewsReceiver: onReceive");//onReceive just runs and runs over and over again and i don't really know why
            switch(intent.getAction()){
                
                case BROADCAST_ARTICLES_LIST:

                    if (intent.hasExtra(ARTICLES_LIST))
                    {
                        Log.d(TAG, "NewsReceiver: inside switch, if");
                        Bundle extra = getIntent().getExtras();
                        if(extra!=null){
                            Toast.makeText(MainActivity.this, "Articles broadcast received", Toast.LENGTH_SHORT).show();
                            ArrayList<Article> articleArrayList =(ArrayList<Article>)extra.getSerializable(ARTICLES_LIST);
                            Log.d(TAG, "not null, "+articleArrayList.get(0).getTitle());
                        }
                    }

                    break;
                case BROADCAST_SOURCE_REQUEST:
                    String data="";
                    if (intent.hasExtra(SOURCE_DATA))
                        data=intent.getStringExtra(SOURCE_DATA);
                    //we're gonna do some stuff with this data
                    break;
            }
            
        }
    }

    private class PageAdapter extends FragmentPagerAdapter{

        private long baseID = 0;

        public PageAdapter(FragmentManager fm){super(fm);}


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return articleFragments.get(position);
        }

        @Override
        public int getCount() {
            return articleFragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseID + position;
        }

        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseID += getCount() + n;
        }
    }

}
