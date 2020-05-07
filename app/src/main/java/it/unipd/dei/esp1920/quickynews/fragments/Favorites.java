package it.unipd.dei.esp1920.quickynews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.unipd.dei.esp1920.quickynews.R;

public class Favorites extends Fragment {

    private TextView messFav;             //per ora dice che non ci sono categorie scelte (sanza far controlli)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fav,container,false);
        /*TextView messFav = (TextView)getView().findViewById(R.id.frag_fav_tx_id);         Questo fra crashare l'app se si va in questo fragment
        messFav.setText("Go to Setting to set your preferences");*/
        return v;
    }

}
