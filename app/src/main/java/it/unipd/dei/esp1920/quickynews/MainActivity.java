package it.unipd.dei.esp1920.quickynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unipd.dei.esp1920.quickynews.fragments.ForYou;
import it.unipd.dei.esp1920.quickynews.fragments.Saved;
import it.unipd.dei.esp1920.quickynews.fragments.TopNews;
import it.unipd.dei.esp1920.quickynews.settings.Settings;


public class MainActivity extends AppCompatActivity {

    private final String TAG="MainActivity";

    private Fragment selectFrag;
    private Toolbar mToolbar;
    //private TextView mTextViewTitle;
    private int flagFragment; //variabile per il parsing di int

    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    private CustomTabsClient mCustomTabsClient;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                Log.d(TAG, "onCustomTabsServiceConnected");
                mCustomTabsClient = client;
                mCustomTabsClient.warmup(0L);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mCustomTabsClient = null;
            }
        };

        boolean ok = CustomTabsClient.bindCustomTabsService(this, CUSTOM_TAB_PACKAGE_NAME, connection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate()");


        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        flagFragment=preferences.getInt("flagFragmentVal", 0);
        Log.d(TAG,"flagFragmentVal onCreate(): " + flagFragment);

        BottomNavigationView.OnNavigationItemSelectedListener navList= new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d(TAG,"onNavigationItemSelected()");
                switch (menuItem.getItemId()) {
                    case R.id.nav_top_news:
                        selectFrag = new TopNews();
                        flagFragment=0;
                        break;
                    case R.id.nav_saved_news:
                        selectFrag = new Saved();
                        flagFragment=2;
                        break;
                    case R.id.nav_for_you:
                        selectFrag = new ForYou();
                        flagFragment=1;
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, selectFrag).commit();
                return true;
            }
        };

        //TOOLBAR
        mToolbar=findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);              //rende predefinata la toolbar creata
        getSupportActionBar().setTitle("Quicky News");

        //BOTTOM NAVIGATION BAR
        BottomNavigationView botNav=findViewById(R.id.bottom_navigation);
        botNav.setOnNavigationItemSelectedListener(navList);

        if(flagFragment==0) {
            //Log.d(TAG, "ENTRO NEL PRIMO IF");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, new TopNews()).commit();
            botNav.setSelectedItemId(R.id.nav_top_news);
        }
        else if(flagFragment==2) {
            //Log.d(TAG, "ENTRO NEL SECONDO IF");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, new Saved()).commit();
            botNav.setSelectedItemId(R.id.nav_saved_news);
        }
        else {
            //Log.d(TAG, "ENTRO NEL TERZO IF");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_con, new ForYou()).commit();
            botNav.setSelectedItemId(R.id.nav_for_you);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG,"onResume() ");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG,"onPause()");
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("flagFragmentVal", flagFragment);
        editor.apply();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        switch(item.getItemId()) {
            case R.id.tool_settings: //pressione tasto impostazioni
                Intent myIntent = new Intent(this, Settings.class);
                startActivity(myIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
