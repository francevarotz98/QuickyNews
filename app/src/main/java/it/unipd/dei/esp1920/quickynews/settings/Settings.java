package it.unipd.dei.esp1920.quickynews.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import it.unipd.dei.esp1920.quickynews.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;


public class Settings extends AppCompatActivity {

    private String[] listCategories;
    private boolean[] checkedCategories;
    private ArrayList<Integer> mUserCategories=new ArrayList<>();


    private static int saveseekbarintP;                         //per salvare lo stato della seekbar
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
        str_et2=preferences.getString("editTextValue2", null);


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


        mSeekbarBtn=(Button)findViewById(R.id.settings_textV_download_size);
        mSeekbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings.this, SeekBarDialog.class);
                Settings.this.startActivity(myIntent);
            }
        });

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
        editor.apply();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
