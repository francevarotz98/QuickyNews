package it.unipd.dei.esp1920.quickynews;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class Settings extends AppCompatActivity {


    private SeekBar  mSeekBar;
    private TextView mTextView;
    private long freeSpace;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_toolbar);

        mTextView=(TextView)findViewById(R.id.sk_progression);
        mSeekBar=(SeekBar)findViewById(R.id.sk_seekBar);


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextView.setText("" + progress + "%");                             //mostra la percentuale di progresso
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
}
