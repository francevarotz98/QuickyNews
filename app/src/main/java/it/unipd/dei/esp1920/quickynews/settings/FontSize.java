package it.unipd.dei.esp1920.quickynews.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import it.unipd.dei.esp1920.quickynews.R;

public class FontSize extends Settings {
    private SeekBar mSeekBar;
    private final String TAG="FontSize";
    private Toolbar mToolbar;
    private String str_et;
    private int mFontSize,int_sb;
    private TextView mTextView,mTextViewTextThatDoesn,mTextView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.settings_font_size);

        //TOOLBAR
        mToolbar=findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);              //rende predefinata la toolbar creata
        getSupportActionBar().setTitle("Choose font size");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        SharedPreferences preferences = getSharedPreferences("fontSizeKey",MODE_PRIVATE);
        int_sb = preferences.getInt("seekBarFontValue", 35);
        str_et = preferences.getString("editTextFontValue", "Small");



        if(int_sb<20) { //very small
            mFontSize=12;
        }
        else if(int_sb<40) {
            mFontSize=17; //small
        }
        else if(int_sb<60) {
            mFontSize=22; //normal
        }
        else if(int_sb<80) {
            mFontSize=27; //big
        }
        else{
            mFontSize=32; //very big
        }

        mTextView = (TextView) findViewById(R.id.tV_progression_font);
        mTextView.setText(str_et);

        mSeekBar = (SeekBar) findViewById(R.id.sk_seekBarFontSize);
        mSeekBar.setProgress(int_sb);
        mTextViewTextThatDoesn = (TextView) findViewById(R.id.tV_size_font);

        mTextView.setTextSize(mFontSize);
        mTextViewTextThatDoesn.setTextSize(mFontSize);


        mTextView2 = (TextView) findViewById(R.id.tV_back_font_inst);
        double tmp=mFontSize-0.3*mFontSize;
        float tmp2 = (float)tmp;
        mTextView2.setTextSize(tmp2);


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<20) {
                    mTextView.setText("Very Small");
                    mTextView.setTextSize(10);
                    }
                else if(progress<40) {
                    mTextView.setText("Small");
                    mTextView.setTextSize(15);
                    }
                else if(progress<60) {
                    mTextView.setText("Normal");
                    mTextView.setTextSize(20);
                }
                else if(progress<80) {
                    mTextView.setText("Big");
                    mTextView.setTextSize(25);
                }
                else{
                    mTextView.setText("Very Big");
                    mTextView.setTextSize(30);
                }

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

        SharedPreferences preferences = getSharedPreferences("fontSizeKey",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        SeekBar mSeekBar = (SeekBar)findViewById(R.id.sk_seekBarFontSize);
        int_sb = mSeekBar.getProgress();
        editor.putInt("seekBarFontValue", int_sb);


        mTextView= (TextView) findViewById(R.id.tV_progression_font);
        str_et = mTextView.getText().toString();
        editor.putString("editTextFontValue", str_et);

        editor.apply();

    }
}
