package it.unipd.dei.esp1920.quickynews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import it.unipd.dei.esp1920.quickynews.storage.AvailableSpace;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Settings extends AppCompatActivity {


    private SeekBar  mSeekBar;
    private TextView mTextView, mTextViewTitle;
    private Toolbar mToolbar;
    private int int_sb;
    private String str_et;
    private TextView mTextViewMin, mTextViewMax;        //textView da Parsare a int, da mettere a fianco
                                                        // alla percentuale della seekbar
    private long totSpace,minSpace,maxSpace;            //serve per settare il testo agli estremi della seekbar
    private int mBChosen;                               //mBytes scelti dall'utente
    private static String TAG="SETTINGS";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");
        setContentView(R.layout.settings_main);

        //TOOLBAR
        mToolbar=findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);              //rende predefinata la toolbar creata
        mTextViewTitle= findViewById(R.id.tV_title_name);
        mTextViewTitle.setText("Settings");

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        int_sb = preferences.getInt("seekBarValue", 0);
        str_et = preferences.getString("editTextValue", null);

        mTextView=(TextView)findViewById(R.id.tV_progression_percentage);
        mTextView.setText(str_et);
        mSeekBar=(SeekBar)findViewById(R.id.sk_seekBar);
        mSeekBar.setProgress(int_sb);

        totSpace = AvailableSpace.getTotalDiskSpace();    //spazio totale

        minSpace=(totSpace/100*1)/(1000000);    //min =1% del tot
        maxSpace=(totSpace/100*7)/(1000000);   ///max=7%del tot

        mTextViewMin=(TextView)findViewById(R.id.tV_minSpace);
        mTextViewMax=(TextView)findViewById(R.id.tV_maxSpace);
        mTextViewMax.setText("Max: " + maxSpace +" Mb");                    //serve solo all'aspetto grafico
        mTextViewMin.setText("Min: " + minSpace+ " Mb");                    //serve solo all'aspetto grafico


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int tmp1 = (int)maxSpace;
                int tmp2 = (int)minSpace;
                mBChosen=(tmp1-tmp2)*progress/100+tmp2;
                mTextView.setText("" + mBChosen + "Mb");                            //mostra la percentuale in Mb di progresso
                                                                                    // della seekbar
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Store values between instances here
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        SeekBar mSeekBar = (SeekBar)findViewById(R.id.sk_seekBar);
        int_sb = mSeekBar.getProgress();
        editor.putInt("seekBarValue", int_sb);

        mTextView= (TextView) findViewById(R.id.tV_progression_percentage);
        str_et = mTextView.getText().toString();
        editor.putString("editTextValue", str_et);

        // Commit to storage synchronously
        editor.commit();
    }
}
