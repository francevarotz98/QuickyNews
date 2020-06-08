package it.unipd.dei.esp1920.quickynews.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import it.unipd.dei.esp1920.quickynews.R;

public class CategoriesSettings extends Settings {

    private int fontSize,mFontSize;
    private final String TAG="Categories1";
    private Toolbar mToolbar;
    private static boolean bln_cb_sport,bln_cb_tech, bln_cb_busin, bln_cb_science; //variabili per salvare lo stato delle categorie


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.categories_settings);

        //TOOLBAR
        mToolbar=findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);              //rende predefinata la toolbar creata
        getSupportActionBar().setTitle("Choose categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = getSharedPreferences("cat",MODE_PRIVATE);

        bln_cb_sport = preferences.getBoolean("chBoxSport",bln_cb_sport);
        bln_cb_tech = preferences.getBoolean("chBoxTech",bln_cb_tech);
        bln_cb_busin = preferences.getBoolean("chBoxBus",bln_cb_busin);
        bln_cb_science = preferences.getBoolean("chBoxSc",bln_cb_science);

        CheckBox mSelectedSport=(CheckBox)findViewById(R.id.checkBox_sport);
        CheckBox mSelectedTech=(CheckBox)findViewById(R.id.checkBox_tech);
        CheckBox mSelectedFood=(CheckBox)findViewById(R.id.checkBox_business);
        CheckBox mSelectedMot=(CheckBox)findViewById(R.id.checkBox_science);
        mSelectedSport.setChecked(bln_cb_sport);
        mSelectedTech.setChecked(bln_cb_tech);
        mSelectedFood.setChecked(bln_cb_busin);
        mSelectedMot.setChecked(bln_cb_science);

        //--------------------------------------------  //per settare la dim del font
        SharedPreferences preferences2 = getSharedPreferences("fontSizeKey",MODE_PRIVATE);
        fontSize = preferences2.getInt("seekBarFontValue", 0);
        if(fontSize<20) {
            mFontSize=10;
        }
        else if(fontSize<40) {
            mFontSize=15;
        }
        else if(fontSize<60) {
            mFontSize=20;
        }
        else if(fontSize<80) {
            mFontSize=25;
        }
        else{
            mFontSize=30;
        }
        mSelectedSport.setTextSize(mFontSize);
        mSelectedTech.setTextSize(mFontSize);
        mSelectedFood.setTextSize(mFontSize);
        mSelectedMot.setTextSize(mFontSize);
    }
    //-------------------------------------------- fino a qua c'è il setting delle dim  del testo
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");

        SharedPreferences preferences = getSharedPreferences("cat",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //------SALVATAGGIO STATO
        CheckBox mSelectedSport=(CheckBox)findViewById(R.id.checkBox_sport);
        CheckBox mSelectedTech=(CheckBox)findViewById(R.id.checkBox_tech);
        CheckBox mSelectedFood=(CheckBox)findViewById(R.id.checkBox_business);
        CheckBox mSelectedMot=(CheckBox)findViewById(R.id.checkBox_science);
        bln_cb_sport=mSelectedSport.isChecked();
        bln_cb_tech=mSelectedTech.isChecked();
        bln_cb_busin=mSelectedFood.isChecked();
        bln_cb_science=mSelectedMot.isChecked();

        editor.putBoolean("chBoxSport", bln_cb_sport);
        editor.putBoolean("chBoxTech", bln_cb_tech);
        editor.putBoolean("chBoxBus", bln_cb_busin);
        editor.putBoolean("chBoxSc", bln_cb_science);

        // Commit to storage synchronously
        editor.apply();
    }


    public static boolean getPreferenceSport(){
        return bln_cb_sport;
    }

    /*
    private boolean testSport(){
        SharedPreferences pref= getPreferences(MODE_PRIVATE);
        bln_cb_sport = pref.getBoolean("chBoxSport",false);
        return bln_cb_sport;
    }
    */


    public static boolean getPreferenceTech(){
        return bln_cb_tech;
    }

    public static boolean getPreferenceBusiness(){
        return bln_cb_busin;
    }

    public static boolean getPreferenceScience(){
        return bln_cb_science;
    }



}
