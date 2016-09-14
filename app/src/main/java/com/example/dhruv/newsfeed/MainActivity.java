package com.example.dhruv.newsfeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

public class
MainActivity extends AppCompatActivity {

    private static final String TAG = "MainAct";
    public static RelativeLayout relativeLayout;
    public static ListView listViewTopStories;
    public static ListView listViewSports;
    public static ListView listViewTech;
    public static ListView listViewWold;
    public TabLayout tabLayout;
    public int eye=0;
    public static int savedArticleSize;
    public static int sharedPrefSize[] = new int[15];
    public static SharedPreferences sref;
    public static SharedPreferences.Editor editor;
    public static int flag[] = new int[15];
    public static int position;
    public static boolean b;
    public static TextToSpeech t1;
    public static final String appDirectoryName = "News Feed";
    public static final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), appDirectoryName);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        sref = MainActivity.this.getSharedPreferences("sharedPrefSize", 0);
        for(int i=0;i<15;i++)
        {
            sharedPrefSize[i]  = sref.getInt("size"+i,0);
        }

        for(int i=0;i<15;i++)
        {
            flag[i]=0;
        }

        SharedPreferences saved = MainActivity.this.getSharedPreferences("savedArticle",0);
        savedArticleSize = saved.getInt("size",0);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        b = (activeNetworkInfo != null && activeNetworkInfo.isConnected());
        if(!b)
        {
            Toast.makeText(MainActivity.this, "A P P  R U N N I N G  I N  O F F L I N E   M O D E", Toast.LENGTH_LONG).show();
        }

        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    if (MainActivity.position < 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            t1.setLanguage(Locale.forLanguageTag("values-hi-rIN"));
                        } else {
                            t1.setLanguage(Locale.ENGLISH);
                        }
                    }
                }
            }
        });
        t1.setSpeechRate(0.8f);


        Log.d(TAG, "beforeTabLayout");
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("SEARCH")); //0
        Log.d(TAG, "afterTabLayout");
        tabLayout.addTab(tabLayout.newTab()); //1
        tabLayout.addTab(tabLayout.newTab().setText("Top Stories"));    //2
        Log.d(TAG, "tab1Added");
        tabLayout.addTab(tabLayout.newTab().setText("PAPERS"));   //3
        tabLayout.addTab(tabLayout.newTab().setText("Sports"));         //4
        Log.d(TAG, "tab2Added");
        tabLayout.addTab(tabLayout.newTab().setText("PAPERS"));   //5
        tabLayout.addTab(tabLayout.newTab().setText("Tech"));       //6
        Log.d(TAG, "tab3Added");
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.activity_main_two));       //7
        tabLayout.addTab(tabLayout.newTab().setText("HINDI"));      //8

        tabLayout.addTab(tabLayout.newTab().setText("Weather")); //weather      //9

        tabLayout.addTab(tabLayout.newTab().setText("Saved Articles")); // 10


        tabLayout.post(tabLayoutConfig);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Log.d(TAG, "viewPagerCAll");
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        Log.d(TAG, "setAdapter");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("SEARCH");
        tabLayout.getTabAt(1).setText("SAVED ARTICLES");
        tabLayout.getTabAt(2).setText("TOP STORIES");
        tabLayout.getTabAt(3).setText("PAPERS");
        tabLayout.getTabAt(4).setText("SPORTS");
//        tabLayout.getTabAt(1).setIcon(R.drawable.topstories);
//        tabLayout.getTabAt(2).setIcon(R.drawable.sportshindi);
//        tabLayout.getTabAt(3).setIcon(R.drawable.tech);
//        tabLayout.getTabAt(4).setIcon(R.drawable.world);
        tabLayout.getTabAt(5).setText("PAPERS");
        tabLayout.getTabAt(6).setText("TECH");
        tabLayout.getTabAt(8).setText("HINDI");
        tabLayout.getTabAt(9).setText("WEATHER");

//        tabLayout.getTabAt(2).setCustomView(R.layout.activity_main_two);
//        tabLayout.getTabAt(4).setCustomView(R.layout.activity_main_two);
//        tabLayout.getTabAt(6).setCustomView(R.layout.activity_main_two);
        tabLayout.getTabAt(8).setCustomView(R.layout.activity_main_two);

//        tabLayout.setBackgroundColor(getResources().getColor(R.color.black));
//        tabLayout.setBackgroundColor(R.style.MyCustomTabLayout);

        viewPager.setOffscreenPageLimit(1);           //look into it once all tabs set
        Log.d(TAG, "adapterAllSet");
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                MainActivity.position = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
//                viewPager.arrowScroll(View.FOCUS_RIGHT);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    if(Search_class.webView.canGoBack())
                    {
                        Search_class.webView.goBack();
                    }
                    else if(TopStoryReference.wb.canGoBack())
                    {
                        eye=1;
                        TopStoryReference.wb.goBack();
                    }
                    else if(SportsReference.wb.canGoBack())
                    {
                        eye=2;
                        SportsReference.wb.goBack();
                    }


                    else if(eye==1)
                    {
                        TopStoryReference.rlWithTop.setVisibility(View.VISIBLE);
                        TopStoryReference.getRlWithTopWb.setVisibility(View.GONE);
                    }
                    else if(eye==2)
                    {
                        SportsReference.rlWithTop.setVisibility(View.VISIBLE);
                        SportsReference.getRlWithTopWb.setVisibility(View.GONE);
                    }

                    else
                    {
                        onBackPressed();
                    }
                    return  true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    Runnable tabLayoutConfig = new Runnable() {
        @Override
        public void run()
        {

            if(tabLayout.getWidth() < MainActivity.this.getResources().getDisplayMetrics().widthPixels)
            {
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                ViewGroup.LayoutParams mParams = tabLayout.getLayoutParams();
                mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                tabLayout.setLayoutParams(mParams);

            }
            else
            {
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
        }
    };

    @Override
    public void onBackPressed() {
        Log.d(TAG,"backPressedCalled");
        Toast.makeText(MainActivity.this, "Press Again To Exit News Feed", Toast.LENGTH_SHORT).show();
        t1.shutdown();
        super.onBackPressed();
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

//class introTimeCalc extends AsyncTask
//{
//    @Override
//    protected Object doInBackground(Object[] params) {
//
//        while(SystemClock.uptimeMillis()-MainActivity.currTime<=6000)
//        {
//
//        }
////        MainActivity.layoutDisappear();
//        return null;
//    }
//}