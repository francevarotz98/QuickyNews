package it.unipd.dei.esp1920.quickynews.settings;


/*
* CLASS TO SET THE MAX NUMBER OF NEWS THE
* USER CAN DOWNLOAD
*
* */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import it.unipd.dei.esp1920.quickynews.R;
import it.unipd.dei.esp1920.quickynews.storage.AvailableSpace;

public class SeekBarDownload extends Settings {

    private SeekBar  mSeekBar;
    private long totSpace,minSpace,maxSpace;      //serve per settare il testo agli estremi della seekbar
    private double minNumberNews,maxNumberNews;
    private int newsChosen,int_sb;       //#news scelti dall'utente
    private TextView mTextView,mTextViewMin,mTextViewMax,mTextView2;
    private String str_et;
    private Toolbar mToolbar;
    private int mFontSize,fontSize;
    private static final String TAG="SeekBarDownload";
    private final static double SIZE_SAVED_NEWS = 0.76 ; //one saved news weigths  0.76 MB ( very rounded up value )


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_seekbar_);

        //TOOLBAR
        mToolbar=findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);              //rende predefinata la toolbar creata
        getSupportActionBar().setTitle("Max number of News to download");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = getSharedPreferences("max_number_news",MODE_PRIVATE);


        totSpace = AvailableSpace.getTotalDiskSpace();   //spazio totale disponibile nel cell
        minSpace=(totSpace/100*2)/(1000000);    //min = 2% del tot
        maxSpace=(totSpace/100*7)/(1000000);    //max = 7%del tot

        minNumberNews = minSpace/SIZE_SAVED_NEWS;
        maxNumberNews = maxSpace/SIZE_SAVED_NEWS;
        int intMinNumberNews = (int) minNumberNews;
        int intMaxNumberNews = (int) maxNumberNews;


        newsChosen = preferences.getInt("max_num_news",newsChosen);

        mTextViewMin=findViewById(R.id.tV_minSpace);
        mTextViewMax=findViewById(R.id.tV_maxSpace);
        mTextViewMax.setText("Max: " + intMaxNumberNews +"News");
        mTextViewMin.setText("Min: " + intMinNumberNews+ "News");


        int_sb = preferences.getInt("seekBarValue", 50);
        str_et = preferences.getString("editTextValue", ""+intMaxNumberNews/2);

        mTextView =  findViewById(R.id.tV_progression_percentage);
        mTextView.setText(str_et);
        mSeekBar =  findViewById(R.id.sk_seekBar);
        mSeekBar.setProgress(int_sb);


        //--------------------------------------------  //per settare la dim del font
        SharedPreferences preferences2 = getSharedPreferences("fontSizeKey",MODE_PRIVATE);
        fontSize = preferences2.getInt("seekBarFontValue", 35);
        if(fontSize<20) {       //very small
            mFontSize=10;
        }
        else if(fontSize<40) {      //small
            mFontSize=15;
        }
        else if(fontSize<60) {      //normal
            mFontSize=20;
        }
        else if(fontSize<80) {      //big
            mFontSize=25;
        }
        else{                        //very big
            mFontSize=30;
        }
        mTextView2 = (TextView) findViewById(R.id.textView2);
        mTextView2.setTextSize(mFontSize);
        mTextView.setTextSize(mFontSize);

    //-------------------------------------------- fino a qua c'è il setting delle dim  del testo


        //cast per dim-- il tmp è il 20% minore della dim
        double tmp=mFontSize-0.2*mFontSize;
        float tmp2 = (float)tmp;
        mTextViewMax.setTextSize(tmp2);
        mTextViewMin.setTextSize(tmp2);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged()");
                int tmp1 =  intMaxNumberNews;
                int tmp2 =  intMinNumberNews;
                newsChosen = (tmp1 - tmp2) * progress / 100 + tmp2;              //IMPORTANTE
                mTextView.setText(" " + newsChosen+ "News");              //mostra la percentuale progresso
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch()");

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch()");

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getSharedPreferences("max_number_news",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        SeekBar mSeekBar = findViewById(R.id.sk_seekBar);
        int_sb = mSeekBar.getProgress();

        editor.putInt("max_num_news",newsChosen);
        editor.putInt("seekBarValue", int_sb);

        mTextView = findViewById(R.id.tV_progression_percentage);
        str_et = mTextView.getText().toString();
        editor.putString("editTextValue", str_et);

        editor.apply();
    }
}
