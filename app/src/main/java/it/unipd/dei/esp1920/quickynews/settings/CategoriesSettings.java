package it.unipd.dei.esp1920.quickynews.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import it.unipd.dei.esp1920.quickynews.R;

public class CategoriesSettings extends Settings {

    private int fontSize,mFontSize;
    private final String TAG="Categories1";
    private Toolbar mToolbar;
    private TextView mTextView2;
    private static boolean bln_cb_sport,bln_cb_tech, bln_cb_busin, bln_cb_science; //variabili per salvare lo stato delle categorie


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.settings_categories);

        //TOOLBAR
        mToolbar=findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);              //rende predefinata la toolbar creata
        getSupportActionBar().setTitle("Choose categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = getSharedPreferences("cat",MODE_PRIVATE);

        //--------------------------------------------  //per settare la dim del font
        SharedPreferences preferences2 = getSharedPreferences("fontSizeKey",MODE_PRIVATE);
        fontSize = preferences2.getInt("seekBarFontValue", 35);
        if(fontSize<20) {   //very small
            mFontSize=12;
        }
        else if(fontSize<40) {  //small
            mFontSize=17;
        }
        else if(fontSize<60) {  //normal
            mFontSize=22;
        }
        else if(fontSize<80) {  // big
            mFontSize=27;
        }
        else{                   //very big
            mFontSize=32;
        }

        bln_cb_sport = preferences.getBoolean("chBoxSport",bln_cb_sport);
        bln_cb_tech = preferences.getBoolean("chBoxTech",bln_cb_tech);
        bln_cb_busin = preferences.getBoolean("chBoxBus",bln_cb_busin);
        bln_cb_science = preferences.getBoolean("chBoxSc",bln_cb_science);

        CheckBox mSelectedSport = findViewById(R.id.checkBox_sport);
        CheckBox mSelectedTech = findViewById(R.id.checkBox_tech);
        CheckBox mSelectedFood = findViewById(R.id.checkBox_business);
        CheckBox mSelectedMot = findViewById(R.id.checkBox_science);

        mSelectedSport.setTextSize(mFontSize);
        mSelectedTech.setTextSize(mFontSize);
        mSelectedFood.setTextSize(mFontSize);
        mSelectedMot.setTextSize(mFontSize);
        mSelectedSport.setChecked(bln_cb_sport);
        mSelectedTech.setChecked(bln_cb_tech);
        mSelectedFood.setChecked(bln_cb_busin);
        mSelectedMot.setChecked(bln_cb_science);

        mTextView2 = (TextView) findViewById(R.id.tV_back_font_inst);
        double tmp=mFontSize-0.3*mFontSize;
        float tmp2 = (float)tmp;
        mTextView2.setTextSize(tmp2);


        Log.d("KEY", "fontSize= "+ fontSize);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");

        SharedPreferences preferences = getSharedPreferences("cat",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //------SALVATAGGIO STATO
        CheckBox mSelectedSport = findViewById(R.id.checkBox_sport);
        CheckBox mSelectedTech = findViewById(R.id.checkBox_tech);
        CheckBox mSelectedFood = findViewById(R.id.checkBox_business);
        CheckBox mSelectedMot = findViewById(R.id.checkBox_science);
        bln_cb_sport = mSelectedSport.isChecked();
        bln_cb_tech = mSelectedTech.isChecked();
        bln_cb_busin = mSelectedFood.isChecked();
        bln_cb_science = mSelectedMot.isChecked();

        editor.putBoolean("chBoxSport", bln_cb_sport);
        editor.putBoolean("chBoxTech", bln_cb_tech);
        editor.putBoolean("chBoxBus", bln_cb_busin);
        editor.putBoolean("chBoxSc", bln_cb_science);

        // Commit to storage synchronously
        editor.apply();
    }





}
