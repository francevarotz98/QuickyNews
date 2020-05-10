package it.unipd.dei.esp1920.quickynews.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.unipd.dei.esp1920.quickynews.MainActivity;
import it.unipd.dei.esp1920.quickynews.R;

public class Categories extends Fragment {

    private final static String TAG="CATEGORIES";
    private boolean bln_cb_sport,bln_cb_tech,bln_cb_food,bln_cb_mot,bln_cb_econ,bln_cb_pol;         //variabili per salvare lo stato delle categorie


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG,"onCreateView()");
        View v = inflater.inflate(R.layout.fragment_categories,container,false);

        //------------PER SALVATAGGIO STATO---------
        SharedPreferences preferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        bln_cb_sport = preferences.getBoolean("chBoxSport",false);
        bln_cb_tech = preferences.getBoolean("chBoxTech",false);
        bln_cb_food = preferences.getBoolean("chBoxFood",false);
        bln_cb_mot = preferences.getBoolean("chBoxMot",false);
        bln_cb_econ = preferences.getBoolean("chBoxEcon",false);
        bln_cb_pol = preferences.getBoolean("chBoxPol",false);
        CheckBox mSelectedSport=(CheckBox) v.findViewById(R.id.checkBox_sport);
        CheckBox mSelectedTech=(CheckBox) v.findViewById(R.id.checkBox_tech);
        CheckBox mSelectedFood=(CheckBox) v.findViewById(R.id.checkBox_food);
        CheckBox mSelectedMot=(CheckBox) v.findViewById(R.id.checkBox_mot);
        CheckBox mSelectedEcon=(CheckBox) v.findViewById(R.id.checkBox_econ);
        CheckBox mSelectedPol=(CheckBox) v.findViewById(R.id.checkBox_pol);
        mSelectedSport.setChecked(bln_cb_sport);
        mSelectedTech.setChecked(bln_cb_tech);
        mSelectedFood.setChecked(bln_cb_food);
        mSelectedMot.setChecked(bln_cb_mot);
        mSelectedEcon.setChecked(bln_cb_econ);
        mSelectedPol.setChecked(bln_cb_pol);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause() ");

        SharedPreferences preferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //------SALVATAGGIO STATO
        CheckBox mSelectedSport=(CheckBox)getView().findViewById(R.id.checkBox_sport);
        CheckBox mSelectedTech=(CheckBox)getView().findViewById(R.id.checkBox_tech);
        CheckBox mSelectedFood=(CheckBox)getView().findViewById(R.id.checkBox_food);
        CheckBox mSelectedMot=(CheckBox)getView().findViewById(R.id.checkBox_mot);
        CheckBox mSelectedEcon=(CheckBox)getView().findViewById(R.id.checkBox_econ);
        CheckBox mSelectedPol=(CheckBox)getView().findViewById(R.id.checkBox_pol);
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
        editor.putBoolean("chBoxPol", bln_cb_pol);
        editor.commit();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop()");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}