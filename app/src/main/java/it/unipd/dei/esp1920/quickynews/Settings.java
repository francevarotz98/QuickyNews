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

    private SeekBar  mSeekBar;
    private TextView mTextView, mTextViewTitle,mItemSelected;
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

        mTextViewTitle= findViewById(R.id.tV_title_name);
        mTextViewTitle.setText("Settings");


        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        int_sb = preferences.getInt("seekBarValue", 0);
        str_et = preferences.getString("editTextValue", null);
        str_et2=preferences.getString("editTextValue2", null);

        mTextView=(TextView)findViewById(R.id.tV_progression_percentage);
        mTextView.setText(str_et);
        mSeekBar=(SeekBar)findViewById(R.id.sk_seekBar);
        mSeekBar.setProgress(int_sb);
        mItemSelected=(TextView)findViewById(R.id.tV_categories_list);
        mItemSelected.setText(str_et2);

/*        bln_cb_sport = preferences.getBoolean("chBoxSport",false);
        bln_cb_tech = preferences.getBoolean("chBoxTech",false);
        bln_cb_food = preferences.getBoolean("chBoxFood",false);
        bln_cb_mot = preferences.getBoolean("chBoxMot",false);
        bln_cb_econ = preferences.getBoolean("chBoxEcon",false);
        bln_cb_pol = preferences.getBoolean("chBoxPol",false);
        CheckBox mSelectedSport=(CheckBox)findViewById(R.id.checkBox_sport);
        CheckBox mSelectedTech=(CheckBox)findViewById(R.id.checkBox_tech);
        CheckBox mSelectedFood=(CheckBox)findViewById(R.id.checkBox_food);
        CheckBox mSelectedMot=(CheckBox)findViewById(R.id.checkBox_mot);
        CheckBox mSelectedEcon=(CheckBox)findViewById(R.id.checkBox_econ);
        CheckBox mSelectedPol=(CheckBox)findViewById(R.id.checkBox_pol);
        mSelectedSport.setChecked(bln_cb_sport);
        mSelectedTech.setChecked(bln_cb_tech);
        mSelectedFood.setChecked(bln_cb_food);
        mSelectedMot.setChecked(bln_cb_mot);
        mSelectedEcon.setChecked(bln_cb_econ);
        mSelectedPol.setChecked(bln_cb_pol);*/


//--------------------------------------------------------------------------------------------------

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
                //mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"setPositiveButton()");
                        String item = "";
                        for(int i =0; i<mUserCategories.size();i++){                            //loopiamo attraverso l'array list
                            item=item + listCategories[mUserCategories.get(i)];                 //item inserito nella lista
                            if(i!=mUserCategories.size()-1){                                    //se sono NON nell'uttimo item aggiungo una virgola dopo
                                item=item+", ";
                            }
                        }
                        mItemSelected.setText(item);
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

//---------------------------------------------------------------------------------------------------

        totSpace = AvailableSpace.getTotalDiskSpace();    //spazio totale

        minSpace=(totSpace/100*1)/(1000000);    //min =1% del tot
        maxSpace=(totSpace/100*7)/(1000000);   ///max=7%del tot

        mTextViewMin=(TextView)findViewById(R.id.tV_minSpace);
        mTextViewMax=(TextView)findViewById(R.id.tV_maxSpace);
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

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause()");

        for(int n=0; n<listCategories.length;n++)
            Log.d(TAG,"onPause: checkedCategories=" + checkedCategories[n]);
        // Store values between instances here
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        SeekBar mSeekBar = (SeekBar)findViewById(R.id.sk_seekBar);
        int_sb = mSeekBar.getProgress();
        editor.putInt("seekBarValue", int_sb);

        mTextView= (TextView) findViewById(R.id.tV_progression_percentage);
        str_et = mTextView.getText().toString();
        editor.putString("editTextValue", str_et);

        mItemSelected=(TextView)findViewById(R.id.tV_categories_list);
        str_et2 = mItemSelected.getText().toString();
        editor.putString("editTextValue2", str_et2);

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

        //------SALVATAGGIO STATO
/*        CheckBox mSelectedSport=(CheckBox)findViewById(R.id.checkBox_sport);
        CheckBox mSelectedTech=(CheckBox)findViewById(R.id.checkBox_tech);
        CheckBox mSelectedFood=(CheckBox)findViewById(R.id.checkBox_food);
        CheckBox mSelectedMot=(CheckBox)findViewById(R.id.checkBox_mot);
        CheckBox mSelectedEcon=(CheckBox)findViewById(R.id.checkBox_econ);
        CheckBox mSelectedPol=(CheckBox)findViewById(R.id.checkBox_pol);
        bln_cb_sport=mSelectedSport.isChecked();
        bln_cb_tech=mSelectedTech.isChecked();
        bln_cb_food=mSelectedFood.isChecked();
        bln_cb_mot=mSelectedMot.isChecked();
        bln_cb_econ=mSelectedEcon.isChecked();
        bln_cb_pol=mSelectedPol.isChecked();

        editor.putBoolean("chBoxSport", bln_cb_sport);
        editor.putBoolean("chBoxTech", bln_cb_tech);
        editor.putBoolean("chBoxFood", bln_cb_food);
        editor.putBoolean("chBoxMot", bln_cb_mot);
        editor.putBoolean("chBoxEcon", bln_cb_econ);
        editor.putBoolean("chBoxPol", bln_cb_pol);*/

        // Commit to storage synchronously
        editor.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        mItemSelected=(TextView)findViewById(R.id.tV_categories_list);
        str_et2 = mItemSelected.getText().toString();
        editor.putString("editTextValue2", str_et2);

    }
}
