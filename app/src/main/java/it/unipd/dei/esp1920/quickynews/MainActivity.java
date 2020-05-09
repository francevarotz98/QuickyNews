package it.unipd.dei.esp1920.quickynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.IntentFilter;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unipd.dei.esp1920.quickynews.fragments.Categories;
import it.unipd.dei.esp1920.quickynews.fragments.Favorites;
import it.unipd.dei.esp1920.quickynews.fragments.TopNews;
import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;

public class MainActivity extends AppCompatActivity {

    private final String TAG="MainActivity";
    public String  flag; //variabile che mi fa capire in quale fragment sono TOPNEWS=0, FAVORITES=1, SETTINGS=2. è una stringa da fare il parsing
    public int flagI; //variabile per il parsing di int
    private BroadcastReceiver  mNetConnectionReceiver=new NetConnectionReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate() MainActivity");


        if (savedInstanceState != null) {
            // Restore value of members from saved state and populare your RecyclerView again
        }

        //BOTTOM NAVIGATION BAR
        BottomNavigationView botNav=findViewById(R.id.bottom_navigation);
        botNav.setOnNavigationItemSelectedListener(navList);
        // OGNI VOLTA CHE SI APRE L'APP VIENE FUORI IL FRAGMENT DELLE TOP NEWS...
        //NEL CASO DEL PRIMO ACCESSO METTEREI UN IF/SWITCH CHE MI PORTA ALLE SETTINGS
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, new TopNews()).commit();      //OGNI VOLTA CHE APRO L'APP ESCE TOP NEWS


        this.registerReceiver(mNetConnectionReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG,"onResume() MainActivity");
        //this.registerReceiver(mNetConnectionReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause()
    {
        super.onPause();
       // this.unregisterReceiver(mNetConnectionReceiver);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navList= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectFrag=null;

            switch (menuItem.getItemId()) {
                case R.id.nav_top_news:
                    selectFrag = new TopNews();
                    break;
                case R.id.nav_categories:
                    selectFrag = new Categories();
                    break;
                case R.id.nav_pref_news:
                    selectFrag = new Favorites();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, selectFrag).commit();
            return true;
        }
    };



}
