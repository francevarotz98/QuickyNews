package it.unipd.dei.esp1920.quickynews.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;
import it.unipd.dei.esp1920.quickynews.news.Article;
import it.unipd.dei.esp1920.quickynews.news.MyRepository;
import it.unipd.dei.esp1920.quickynews.news.NewsApiResponse;
import it.unipd.dei.esp1920.quickynews.news.NewsListAdapter;
//import it.unipd.dei.esp1920.quickynews.settings.CategoriesSettings;

import static android.content.Context.MODE_PRIVATE;

public class ForYou extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG="ForYou";
    private boolean bln_cb_sport,bln_cb_tech,bln_cb_busin,bln_cb_science;    //variabili per salvare lo stato delle categorie
    private MyRepository myRepository;
    private RecyclerView recyclerView;
    private List<Article> newsList;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView textCategory ;

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach()");
        this.context = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView()");

        View view = inflater.inflate(R.layout.fragment_home,container,false);
        //View v= inflater.inflate(R.layout.feed_list_item_foryou,container,false);
        //textCategory = v.findViewById(R.id.item_category);
        myRepository = TopNews.getRepository();
        newsList = new LinkedList<>();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) ;
        swipeRefreshLayout.setOnRefreshListener(this);

        if(myRepository==null)
            Toast.makeText(view.getContext(),"Please, click on Top News(bottom left) then come back",Toast.LENGTH_LONG).show();
        else {
            //getFavoritesArticle();

            SharedPreferences preferences = getActivity().getSharedPreferences("cat",MODE_PRIVATE);
            Log.d(TAG,"textcategory="+textCategory);
            bln_cb_sport = preferences.getBoolean("chBoxSport",bln_cb_sport);
            bln_cb_tech = preferences.getBoolean("chBoxTech",bln_cb_tech);
            bln_cb_busin = preferences.getBoolean("chBoxBus",bln_cb_busin);
            bln_cb_science = preferences.getBoolean("chBoxSc",bln_cb_science);

            if (bln_cb_sport)
                for (Article article : myRepository.getSportArticle())
                    newsList.add(article);

            if (bln_cb_tech)
                for (Article article : myRepository.getTechnologyArticle())
                    newsList.add(article);


            if (bln_cb_busin)
                for (Article article : myRepository.getBusinessArticle())
                    newsList.add(article);


            if (bln_cb_science)
                for (Article article : myRepository.getScienceArticle())
                    newsList.add(article);


            Log.d(TAG,"post getFavoritesArticle() newsList size = "+newsList.size());


            //---------------------


            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(new NewsListAdapter(context, new NewsApiResponse(newsList)));
        }
        return view;
    }


    private void getFavoritesArticle(){
        Log.d(TAG,"getFavoritesArticle()");


        SharedPreferences preferences = getActivity().getSharedPreferences("cat",MODE_PRIVATE);

        bln_cb_sport = preferences.getBoolean("chBoxSport",bln_cb_sport);
        bln_cb_tech = preferences.getBoolean("chBoxTech",bln_cb_tech);
        bln_cb_busin = preferences.getBoolean("chBoxBus",bln_cb_busin);
        bln_cb_science = preferences.getBoolean("chBoxSc",bln_cb_science);

        if (bln_cb_sport)
            for (Article article : myRepository.getSportArticle()) {
                newsList.add(article);

            }

        if (bln_cb_tech)
            for (Article article : myRepository.getTechnologyArticle()) {
                newsList.add(article);

            }

        if (bln_cb_busin)
            for (Article article : myRepository.getBusinessArticle()) {
                newsList.add(article);

            }

        if (bln_cb_science)
            for (Article article : myRepository.getScienceArticle()) {
                newsList.add(article);

            }

        Log.d(TAG,"post getFavoritesArticle() newsList size = "+newsList.size());
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh()");
        if(NetConnectionReceiver.isConnected(context)) {
            swipeRefreshLayout.setRefreshing(true);
            newsList = new LinkedList<>();
            getFavoritesArticle();
        }
        else {
            Toast.makeText(getContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
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