package it.unipd.dei.esp1920.quickynews;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import it.unipd.dei.esp1920.quickynews.storage.AvailableSpace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;


public class Settings extends AppCompatActivity {

    private String[] listCategories;
    private boolean[] checkedCategories;
    private ArrayList<Integer> mUserCategories=new ArrayList<>();


    private int saveseekbarintP;                         //per salvare lo stato della seekbar
    private String saveseekbarString;
    private Button mSeekbarBtn;


    private SeekBar  mSeekBar;
    private TextView mTextView, mTextViewTitle;
    private Toolbar mToolbar;
    private Button mCategories;
    private int int_sb;
    private String str_et,str_et2;
    private boolean bln_cb_sport,bln_cb_tech,bln_cb_food,bln_cb_mot,bln_cb_econ,bln_cb_pol;         //variabili per salvare lo stato delle categorie
    private TextView mTextViewMin, mTextViewMax;        //textView da Parsare a int, da mettere a fianco
                                                        // alla percentuale della seekbar
    private long totSpace,minSpace,maxSpace;            //serve per settare il testo agli estremi della seekbar
    private int mBChosen;                               //mBytes scelti dall'utente
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
        saveseekbarintP = preferences.getInt("seekBarValue", 0);
        str_et = preferences.getString("editTextValue", null);
        str_et2=preferences.getString("editTextValue2", null);

        //mTextView=(TextView)findViewById(R.id.tV_progression_percentage);
        //mTextView.setText(str_et);
        //mSeekBar=(SeekBar)findViewById(R.id.sk_seekBar);
        /*mSeekBar.setProgress(int_sb);*/

        //parte dedicata all'AlertDialog
        mCategories=(Button)findViewById(R.id.btn_choose_categories);
        listCategories= getResources().getStringArray(R.array.categories_item);
        checkedCategories = new boolean[listCategories.length];

        bln_cb_tech=preferences.getBoolean("chBoxTech",false);
        bln_cb_food = preferences.getBoolean("chBoxFood",false);
        bln_cb_sport = preferences.getBoolean("chBoxSport",false);
        bln_cb_pol = preferences.getBoolean("chBoxPol",false);
        bln_cb_mot= preferences.getBoolean("chBoxMot",false);
        bln_cb_econ= preferences.getBoolean("chBoxEcon",false);
        checkedCategories[0]=(bln_cb_tech);
        checkedCategories[1]=(bln_cb_food);
        checkedCategories[2]=(bln_cb_sport);
        checkedCategories[3]=(bln_cb_pol);
        checkedCategories[4]=(bln_cb_mot);
        checkedCategories[5]=(bln_cb_econ);

    for(int n=0; n<listCategories.length;n++)
        Log.d(TAG,"onCreate: checkedCategories=" + checkedCategories[n]);

        mCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick()");
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Settings.this);
                mBuilder.setTitle("Choose Categories");
                mBuilder.setMultiChoiceItems(listCategories, checkedCategories, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked) {                                                      //se è "checkato"
                            mUserCategories.add(position);                                    //lo aggiungo
                        } else if(mUserCategories.contains(position)){
                                mUserCategories.remove(Integer.valueOf(position));
                            }
                        }

                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"setPositiveButton()");
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        //fine parte AlertDialog


        //--------------------------------------------------------------------------------------------------
        View testView = getLayoutInflater().inflate(R.layout.dialog_seekbar_settings, null);
        mSeekBar=(SeekBar)testView.findViewById(R.id.sk_seekBar);
        mSeekBar.setProgress(saveseekbarintP);

        totSpace = AvailableSpace.getTotalDiskSpace();    //spazio totale

        minSpace=(totSpace/100*1)/(1000000);    //min =1% del tot
        maxSpace=(totSpace/100*7)/(1000000);   ///max=7%del tot

        mSeekbarBtn=(Button)findViewById(R.id.settings_textV_download_size);
        mSeekbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Settings.this);
                mBuilder.setTitle("Choose Size");
                View testView = getLayoutInflater().inflate(R.layout.dialog_seekbar_settings, null);
                mBuilder.setView(testView);
                mTextView=(TextView)testView.findViewById(R.id.tV_progression_percentage);

                mTextView.setText(str_et);
                mTextViewMin=(TextView)testView.findViewById(R.id.tV_minSpace);
                mTextViewMax=(TextView)testView.findViewById(R.id.tV_maxSpace);
                mTextViewMax.setText("Max: " + maxSpace +"Mb");                    //serve solo all'aspetto grafico
                mTextViewMin.setText("Min: " + minSpace+ "Mb");                    //serve solo all'aspetto grafico
                mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Log.d(TAG,"onProgressChanged()");
                        int tmp1 = (int)maxSpace;
                        int tmp2 = (int)minSpace;
                        mBChosen=(tmp1-tmp2)*progress/100+tmp2;                                             //IMPORTANTE
                        mTextView.setText("" + mBChosen + "Mb");                            //mostra la percentuale in Mb di progresso
                        saveseekbarintP=progress;
                        Log.d(TAG,"saveseekbarintP dentro ="+ progress);
                        // della seekbar
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Log.d(TAG,"onStartTrackingTouch()");

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Log.d(TAG,"onStopTrackingTouch()");

                    }
                });
                saveseekbarintP = mSeekBar.getProgress();
                Log.d(TAG,"saveseekbarintP fuori ="+ saveseekbarintP);
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"setPositiveButton()");
                        Log.d(TAG,"saveseekbarintP Positive="+ saveseekbarintP);
                        saveseekbarintP=which;
                        Log.d(TAG,"saveseekbarintP Positive which="+ saveseekbarintP);

                    }
                });
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
            }
        });
        //--------------------------------------------------------------------------------------------------



/*        totSpace = AvailableSpace.getTotalDiskSpace();    //spazio totale

        minSpace=(totSpace/100*1)/(1000000);    //min =1% del tot
        maxSpace=(totSpace/100*7)/(1000000);   ///max=7%del tot

        mTextViewMin=(TextView)findViewById(R.id.tV_minSpace);
        mTextViewMax=(TextView)findViewById(R.id.tV_maxSpace);
        mTextViewMax.setText("Max: " + maxSpace +"Mb");                    //serve solo all'aspetto grafico
        mTextViewMin.setText("Min: " + minSpace+ "Mb");                    //serve solo all'aspetto grafico*/


/*        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG,"onProgressChanged()");
                int tmp1 = (int)maxSpace;
                int tmp2 = (int)minSpace;
                mBChosen=(tmp1-tmp2)*progress/100+tmp2;                                             //IMPORTANTE
                mTextView.setText("" + mBChosen + "Mb");                            //mostra la percentuale in Mb di progresso
                                                                                    // della seekbar
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG,"onStartTrackingTouch()");

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG,"onStopTrackingTouch()");

            }
        });*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause()");

        for(int n=0; n<listCategories.length;n++) {
            Log.d(TAG, "onPause: checkedCategories=" + checkedCategories[n]);
            Log.d(TAG,"saveseekbarintP ="+ saveseekbarintP);
        }
        // Store values between instances here
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


/*        saveseekbarintP=int_sb;
        editor.putInt("seekBarValue", saveseekbarintP);*/
/*
        SeekBar mSeekBar = (SeekBar)findViewById(R.id.sk_seekBar);
        int_sb = mSeekBar.getProgress();
        editor.putInt("seekBarValue", int_sb);
*/

/*        mTextView= (TextView) findViewById(R.id.tV_progression_percentage);
        str_et = mTextView.getText().toString();
        editor.putString("editTextValue", str_et);*/

        bln_cb_tech=checkedCategories[0];
        bln_cb_food= checkedCategories[1];
        bln_cb_sport= checkedCategories[2];
        bln_cb_pol= checkedCategories[3];
        bln_cb_mot=checkedCategories[4];
        bln_cb_econ =checkedCategories[5];

        editor.putBoolean("chBoxSport", bln_cb_sport);
        editor.putBoolean("chBoxTech", bln_cb_tech);
        editor.putBoolean("chBoxFood", bln_cb_food);
        editor.putBoolean("chBoxMot", bln_cb_mot);
        editor.putBoolean("chBoxEcon", bln_cb_econ);
        editor.putBoolean("chBoxPol", bln_cb_pol);

        // Commit to storage synchronously
        editor.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
