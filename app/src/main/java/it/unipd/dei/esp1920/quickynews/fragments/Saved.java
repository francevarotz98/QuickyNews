package it.unipd.dei.esp1920.quickynews.fragments;

/*
*
* CLASS WHERE I CAN SEE ALL THE NEWS
* WHICH HAVE THE ATTRIBUTE isFavorite
* SET TO true.
*
* */


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.LinkedList;
import java.util.List;

import it.unipd.dei.esp1920.quickynews.R;
import it.unipd.dei.esp1920.quickynews.fragments.TopNews;
import it.unipd.dei.esp1920.quickynews.news.Article;
import it.unipd.dei.esp1920.quickynews.news.MyRepository;
import it.unipd.dei.esp1920.quickynews.news.NewsApiResponse;
import it.unipd.dei.esp1920.quickynews.news.NewsListAdapter;

public class Saved extends Fragment {

    private final static String TAG="Saved";
    private RecyclerView recyclerView;
    private MyRepository myRepository ;
    private Context context;

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach()");
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        Log.d(TAG,"onCreate()");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        View view = inflater.inflate(R.layout.fragment_saved,container,false);
        myRepository = TopNews.getRepository();

        /*
        * SOLUZIONE TEMPORANEA PER ERRORE NullPointerException per variabile
        * myRepository. Possibile soluzione: creare variabile myRepository
        * nel metodo onCreate di MainActivity e NON nel fragment TopNews.
        * 
        * */

        if(myRepository==null)
            Toast.makeText(view.getContext(),"Please, click on Top News(bottom left) then come back",Toast.LENGTH_LONG).show();
        else {
            recyclerView = view.findViewById(R.id.recyclerView_saved);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(new NewsListAdapter(context, new NewsApiResponse(myRepository.getFavoritesArticle())));
        }

        return view;
    }




}
