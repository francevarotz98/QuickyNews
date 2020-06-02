package it.unipd.dei.esp1920.quickynews.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.unipd.dei.esp1920.quickynews.R;
import it.unipd.dei.esp1920.quickynews.news.MyRepository;
import it.unipd.dei.esp1920.quickynews.news.NewsApiResponse;
import it.unipd.dei.esp1920.quickynews.news.NewsListAdapter;
import it.unipd.dei.esp1920.quickynews.settings.CategoriesSettings;

public class ForYou extends Fragment {

    private final static String TAG="ForYou";
    private boolean bln_cb_sport,bln_cb_tech,bln_cb_food,bln_cb_mot,bln_cb_econ,bln_cb_pol;         //variabili per salvare lo stato delle categorie
    private MyRepository myRepository;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        myRepository = TopNews.getRepository();
        Log.d(TAG,"pref_sport="+CategoriesSettings.getPreferenceSport());
        Log.d(TAG,"pref_Tech="+CategoriesSettings.getPreferenceTech());
        Log.d(TAG,"pref_Science="+CategoriesSettings.getPreferenceScience());
        Log.d(TAG,"pref_Business="+CategoriesSettings.getPreferenceBusiness());
        /*
         * SOLUZIONE TEMPORANEA PER ERRORE NullPointerException per variabile
         * myRepository. Possibile soluzione: creare variabile myRepository
         * nel metodo onCreate di MainActivity e NON nel fragment TopNews.
         *
         * */

        if(myRepository==null)
            Toast.makeText(view.getContext(),"Please, click on Top News(bottom left) then come back",Toast.LENGTH_LONG).show();
        else {
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(new NewsListAdapter(new NewsApiResponse("", myRepository.getFavoritesArticle())));
        }

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause() ");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop()");

    }

}