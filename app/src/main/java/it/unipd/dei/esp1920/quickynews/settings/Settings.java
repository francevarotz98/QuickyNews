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
    private int mFontSizeI,fontSize;

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
        fontSize = preferences2.getInt("seekBarFontValue", 35);

        if(fontSize<20) {       //very small
            mFontSizeI=12;
        }
        else if(fontSize<40) {      //small
            mFontSizeI=17;
        }
        else if(fontSize<60) {      //normal
            mFontSizeI=22;
        }
        else if(fontSize<80) {      //big
            mFontSizeI=27;
        }
        else{                       //very big
            mFontSizeI=32;
        }




        mCategories=findViewById(R.id.btn_choose_categories);
        mCategories.setTextSize(mFontSizeI);
        mCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent= new Intent(Settings.this, CategoriesSettings.class);
                Settings.this.startActivity(mIntent);
            }
        });

        mSeekbarBtn=findViewById(R.id.settings_textV_download_size);
        mSeekbarBtn.setTextSize(mFontSizeI);
        mSeekbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings.this, SeekBarDownload.class);
                Settings.this.startActivity(myIntent);
            }
        });

        mFontSize=findViewById(R.id.btn_choose_font_size);
        mFontSize.setTextSize(mFontSizeI);
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
