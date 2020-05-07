package it.unipd.dei.esp1920.quickynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unipd.dei.esp1920.quickynews.fragments.Favorites;
import it.unipd.dei.esp1920.quickynews.fragments.Location;
import it.unipd.dei.esp1920.quickynews.fragments.Settings;
import it.unipd.dei.esp1920.quickynews.fragments.TopNews;

import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;

public class MainActivity extends AppCompatActivity {

    private final String TAG="MainActivity";
    private int  firstaccess=0; //variabile che è sempre =1 tranne al primo accesso che è =0
    private BroadcastReceiver mNetConnectionReceiver;
    //easy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate()");

        BottomNavigationView botNav=findViewById(R.id.bottom_navigation);
        botNav.setOnNavigationItemSelectedListener(navList);
        // OGNI VOLTA CHE SI APRE L'APP VIENE FUORI IL FRAGMENT DELLE TOP NEWS...
        //NEL CASO DEL PRIMO ACCESSO METTEREI UN IF CHE MI PORTA ALLE SETTINGS
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, new TopNews()).commit();      //OGNI VOLTA CHE APRO L'APP ESCE TOP NEWS
        mNetConnectionReceiver=new NetConnectionReceiver();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navList= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectFrag=null;

            switch (menuItem.getItemId()) {
                case R.id.nav_top_news:
                    selectFrag = new TopNews();
                    break;
                case R.id.nav_settings:
                    selectFrag = new Settings();
                    break;
                case R.id.nav_pref_news:
                    selectFrag = new Favorites();
                    break;
                case R.id.nav_map:
                    selectFrag = new Location();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, selectFrag).commit();
            return true;
        }
    };



}
