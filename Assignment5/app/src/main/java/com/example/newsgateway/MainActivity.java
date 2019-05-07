package com.example.newsgateway;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String BROADCAST_ARTICLE_REQ = "Article Request";
    public static final String BROADCAST_RECEIVED_ARTICLES = "Articles Received";



    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayList<NewsSource> sourceList = new ArrayList<>();
    HashMap<String, ArrayList<NewsSource>> sourceHashMap = new HashMap<>();
    HashMap<String, Integer> colorHashMap = new HashMap<>();
    Menu mainMenu;

    ListView listView;

    ArrayAdapter<String> newsArrayAdapter;

    ArrayList<String> sourceNameList = new ArrayList<>();

    private ArticleService articleService;

    private MainReciever mainReciever;

    private ViewPager viewPager;

    private ArticleFragmentAdapter articleFragmentAdapter;

    private ArrayList<ArticleFragment> articleFragmentsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout); // <== Important!
        mDrawerList = findViewById(R.id.left_drawer); // <== Important!
        viewPager = findViewById(R.id.pager);


        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = sourceNameList.get(position);

                        for (int i = 0; i < sourceList.size(); i++) {
                            NewsSource tempNewsSource = sourceList.get(i);
                            if (tempNewsSource.getName().equals(name)) {
                                Toast.makeText(MainActivity.this, tempNewsSource.getId(), Toast.LENGTH_SHORT).show();
                                broadcastArticleReq(tempNewsSource.getId());
                                Log.d("MainActivity", "id: " + tempNewsSource.getId());
                                mDrawerLayout.closeDrawer(mDrawerList);
                                return;
                            }
                        }

                    }
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(   // <== Important!
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );


        // Get sources
        AsyncGetSources asyncGetSources = new AsyncGetSources(this);
        asyncGetSources.execute();

        articleService = new ArticleService();

        mainReciever = new MainReciever(this);
        IntentFilter intentFilter = new IntentFilter(BROADCAST_RECEIVED_ARTICLES);
        registerReceiver(mainReciever, intentFilter);



        Intent intent = new Intent(MainActivity.this, ArticleService.class);
        startService(intent);

        articleFragmentAdapter = new ArticleFragmentAdapter(getSupportFragmentManager(), articleFragmentsList);
        viewPager.setAdapter(articleFragmentAdapter);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mainReciever);
        super.onDestroy();

    }

    public void broadcastArticleReq(String id) {
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ARTICLE_REQ);
        intent.putExtra("id", id);
        sendBroadcast(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Add all menu item
        categoryList.add("all");
        menu.add(Menu.NONE, categoryList.indexOf("all"), Menu.NONE, "all");
        mainMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState(); // <== IMPORTANT
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig); // <== IMPORTANT
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {  // <== Important!
            Log.d("MA", "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        sourceNameList.clear();

        ArrayList<NewsSource> tempList = sourceHashMap.get(item.getTitle().toString());

        for (int i = 0; i < tempList.size(); i++) {
            sourceNameList.add(tempList.get(i).getName());
        }

        ((ArrayAdapter) mDrawerList.getAdapter()).notifyDataSetChanged();





        return super.onOptionsItemSelected(item);

    }

    public void addSource(ArrayList<NewsSource> newsSourceArrayList, ArrayList<String> categoryList) {
        //sourceHashMap.put(source.getName(), source);
        //sourceList.add(source);
        for (int i = 0; i < categoryList.size(); i++) {
            ArrayList<NewsSource> tempList = new ArrayList<NewsSource>();
            for (int j = 0; j < newsSourceArrayList.size(); j++) {
                NewsSource source = newsSourceArrayList.get(j);

                // Categories are the same
                if (categoryList.get(i).equals(source.getCategory())) {
                    tempList.add(source);
                }
            }

            // Add to hash map
            sourceHashMap.put(categoryList.get(i), tempList);
        }

        // Put all keys into menu
        for (String key: sourceHashMap.keySet()) {
            // Add to menu
            mainMenu.add(key);

        }

        for (int i = 1; i < mainMenu.size(); i++) {
            MenuItem menuItem = mainMenu.getItem(i);
            SpannableString s = new SpannableString(menuItem.getTitle());

            switch (i) {
                case 1:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type1)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type1);
                    break;

                case 2:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type2)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type2);

                    break;

                case 3:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type3)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type3);
                    break;

                case 4:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type4)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type4);
                    break;

                case 5:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type5)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type5);
                    break;

                case 6:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type6)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type6);
                    break;


                case 7:
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.type7)), 0, s.length(), 0);
                    menuItem.setTitle(s);
                    colorHashMap.put(menuItem.getTitle().toString(), R.color.type7);
                    break;
            }
        }



        // Add all key that contains all sources
        sourceHashMap.put("all", newsSourceArrayList);

        // Initialize sourceList and categoryList
        this.categoryList = categoryList;
        this.sourceList.addAll(newsSourceArrayList);

        sourceNameList = new ArrayList<String>();

        for (int i = 0; i < sourceList.size(); i++) {
            sourceNameList.add(sourceList.get(i).getName());
            colorHashMap.put(sourceList.get(i).getName(), colorHashMap.get(sourceList.get(i).getCategory()));
        }

        // Initialize adapter
        newsArrayAdapter = new ArrayAdapter<>(this,   // <== Important!
                R.layout.drawer_list_item, sourceNameList);

        // TODO: ArrayAdapter put as a variable of NewsSources
        mDrawerList.setAdapter(newsArrayAdapter);

        if (getSupportActionBar() != null) {  // <== Important!
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }


    public void clear() {
        sourceHashMap.clear();
        sourceList.clear();
        sourceNameList.clear();
    }

    public void addAllFragments(ArrayList<Article> arrayList) {

        for (int i = 0; i < articleFragmentAdapter.getCount(); i++) {
            articleFragmentAdapter.notifyChangeInPosition(i);
        }

        articleFragmentsList.clear();

        for (int i = 0; i < arrayList.size(); i++) {
            articleFragmentsList.add(ArticleFragment.newInstance(arrayList.get(i), arrayList.size()));
        }

        articleFragmentAdapter.notifyDataSetChanged();

        viewPager.setCurrentItem(0);


    }


}
