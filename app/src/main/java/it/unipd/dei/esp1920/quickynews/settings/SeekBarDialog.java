package it.unipd.dei.esp1920.quickynews.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import it.unipd.dei.esp1920.quickynews.R;
import it.unipd.dei.esp1920.quickynews.storage.AvailableSpace;

public class SeekBarDialog extends Settings {

    private SeekBar  mSeekBar;
    private long totSpace,minSpace,maxSpace;            //serve per settare il testo agli estremi della seekbar
    private int mBChosen,int_sb;                               //mBytes scelti dall'utente
    private TextView mTextView,mTextViewMin,mTextViewMax,mTextView2;
    private String str_et;
    private Toolbar mToolbar;
    private int mFontSize,fontSize;
    private static final String TAG="SeekBarDialog";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_seekbar_settings);



        //TOOLBAR
        mToolbar=findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);              //rende predefinata la toolbar creata
        getSupportActionBar().setTitle("Size to download");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        int_sb = preferences.getInt("seekBarValue", 0);
        str_et = preferences.getString("editTextValue", null);

        mTextView = (TextView) findViewById(R.id.tV_progression_percentage);
        mTextView.setText(str_et);
        mSeekBar = (SeekBar) findViewById(R.id.sk_seekBar);
        mSeekBar.setProgress(int_sb);




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
        mTextView2 = (TextView) findViewById(R.id.textView2);
        mTextView2.setTextSize(mFontSize);
        mTextView.setTextSize(mFontSize);

    //-------------------------------------------- fino a qua c'è il setting delle dim  del testo





        totSpace = AvailableSpace.getTotalDiskSpace();    //spazio totale

        minSpace=(totSpace/100*1)/(1000000);    //min =1% del tot
        maxSpace=(totSpace/100*7)/(1000000);   ///max=7%del tot

        mTextViewMin=(TextView)findViewById(R.id.tV_minSpace);
        mTextViewMax=(TextView)findViewById(R.id.tV_maxSpace);
        mTextViewMax.setText("Max: " + maxSpace +"Mb");                    //serve solo all'aspetto grafico
        mTextViewMin.setText("Min: " + minSpace+ "Mb");                    //serve solo all'aspetto grafico

        //cast per dim-- il tmp è il 20% minore della dim
        double tmp=mFontSize-0.2*mFontSize;
        float tmp2 = (float)tmp;
        mTextViewMax.setTextSize(tmp2);
        mTextViewMin.setTextSize(tmp2);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged()");
                int tmp1 = (int) maxSpace;
                int tmp2 = (int) minSpace;
                mBChosen = (tmp1 - tmp2) * progress / 100 + tmp2;                                             //IMPORTANTE
                mTextView.setText(" " + mBChosen + "Mb");                            //mostra la percentuale in Mb di progresso
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

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        SeekBar mSeekBar = (SeekBar)findViewById(R.id.sk_seekBar);
        int_sb = mSeekBar.getProgress();
        editor.putInt("seekBarValue", int_sb);


        mTextView= (TextView) findViewById(R.id.tV_progression_percentage);
        str_et = mTextView.getText().toString();
        editor.putString("editTextValue", str_et);

        editor.apply();
    }
}
