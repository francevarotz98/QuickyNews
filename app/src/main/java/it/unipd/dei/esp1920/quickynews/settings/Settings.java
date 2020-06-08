package it.unipd.dei.esp1920.quickynews.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import it.unipd.dei.esp1920.quickynews.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Settings extends AppCompatActivity {

    private Button mSeekbarBtn;
    private Toolbar mToolbar;
    private Button mCategories;
    private Button mFontSize;
    private int mFontSizeI,int_sb;

    private static final String TAG="Settings";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");
        setContentView(R.layout.settings_main);

        //TOOLBAR
        mToolbar=findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);              //rende predefinata la toolbar creata
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences preferences = getPreferences(MODE_PRIVATE);


        SharedPreferences preferences2 = getSharedPreferences("fontSizeKey",MODE_PRIVATE);
        int_sb = preferences.getInt("seekBarFontValue", 0);

        if(int_sb<20) {
            mFontSizeI=10;
        }
        else if(int_sb<40) {
            mFontSizeI=15;
        }
        else if(int_sb<60) {
            mFontSizeI=20;
        }
        else if(int_sb<80) {
            mFontSizeI=25;
        }
        else{
            mFontSizeI=30;
        }




        mCategories=(Button)findViewById(R.id.btn_choose_categories);

        mCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent= new Intent(Settings.this, CategoriesSettings.class);
                Settings.this.startActivity(mIntent);
            }
        });

        mSeekbarBtn=(Button)findViewById(R.id.settings_textV_download_size);
        mSeekbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings.this, SeekBarDialog.class);
                Settings.this.startActivity(myIntent);
            }
        });

        mFontSize=(Button)findViewById(R.id.btn_choose_font_size);
        mFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings.this, FontSize.class);
                Settings.this.startActivity(myIntent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause()");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
