package it.unipd.dei.esp1920.quickynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.IntentFilter;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unipd.dei.esp1920.quickynews.fragments.Categories;
import it.unipd.dei.esp1920.quickynews.fragments.Favorites;
import it.unipd.dei.esp1920.quickynews.fragments.TopNews;
import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;
import it.unipd.dei.esp1920.quickynews.storage.AvailableSpace;

public class MainActivity extends AppCompatActivity {

    private final String TAG="MainActivity";
    private Toolbar mToolbar;
   // private String  flag; //variabile che mi fa capire in quale fragment sono TOPNEWS=0, FAVORITES=1, SETTINGS=2. è una stringa da fare il parsing
    private int flagFragment; //variabile per il parsing di int
    private BroadcastReceiver  mNetConnectionReceiver;
    private static final String KEY_BUNDLE = "KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate()");

        if (savedInstanceState != null) {
            flagFragment = savedInstanceState.getInt(KEY_BUNDLE, 0);
        }

        //long space= AvailableSpace.getTotalDiskSpace();
        mNetConnectionReceiver =new NetConnectionReceiver();
        this.registerReceiver(mNetConnectionReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //TOOLBAR
        mToolbar=findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);              //rende predefinata la toolbar creata
        //BOTTOM NAVIGATION BAR
        BottomNavigationView botNav=findViewById(R.id.bottom_navigation);
        botNav.setOnNavigationItemSelectedListener(navList);

        //TODO: NEL CASO DEL PRIMO ACCESSO METTEREI UN IF/SWITCH CHE MI PORTA ALLE SETTINGS
        if(flagFragment==0) {
            //Log.d(TAG, "ENTRO NEL PRIMO IF");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, new TopNews()).commit();
        }
        else if(flagFragment==1) {
            //Log.d(TAG, "ENTRO NEL SECONDO IF");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, new Favorites()).commit();
        }
        else {
            //Log.d(TAG, "ENTRO NEL TERZO IF");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, new Categories()).commit();
        }


    }


    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG,"onResume() ");
        //this.registerReceiver(mNetConnectionReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG,"onPause()");

    }


    @Override
    public void onStop()
    {
        super.onStop();
        Log.d(TAG,"onStop()");
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG,"onDestroy()");
        this.unregisterReceiver(mNetConnectionReceiver);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navList= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectFrag=null;
            Log.d(TAG,"onNavigationItemSelected()");
            switch (menuItem.getItemId()) {
                case R.id.nav_top_news:
                    selectFrag = new TopNews();
                    flagFragment=0;
                    break;
                case R.id.nav_pref_news:
                    selectFrag = new Favorites();
                    flagFragment=1;
                    break;
                case R.id.nav_categories:
                    selectFrag = new Categories();
                    flagFragment=2;
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, selectFrag).commit();
            return true;
        }
    };


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG,"onSaveInstanceState()");
        savedInstanceState.putInt(KEY_BUNDLE,flagFragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.tool_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
