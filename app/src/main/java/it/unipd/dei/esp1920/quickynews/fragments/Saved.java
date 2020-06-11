package it.unipd.dei.esp1920.quickynews.fragments;

/*
*
* CLASS WHERE I CAN SEE ALL THE NEWS
* WHICH HAVE THE ATTRIBUTE isFavorite
* SET TO true.
*
* */


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import it.unipd.dei.esp1920.quickynews.R;
import it.unipd.dei.esp1920.quickynews.news.Article;
import it.unipd.dei.esp1920.quickynews.news.MyRepository;
import it.unipd.dei.esp1920.quickynews.news.NewsApiResponse;
import it.unipd.dei.esp1920.quickynews.news.NewsListAdapter;
import it.unipd.dei.esp1920.quickynews.storage.AvailableSpace;

import static android.content.Context.MODE_PRIVATE;

public class Saved extends Fragment {

    private final static String TAG="Saved";
    private RecyclerView recyclerView;
    private MyRepository myRepository ;
    private Context context;
    private int max_num_news;
    private final static double SIZE_SAVED_NEWS = 0.76;

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

        SharedPreferences preferences = getActivity().getSharedPreferences("max_number_news",MODE_PRIVATE);
        max_num_news = preferences.getInt("max_num_news",max_num_news);
        if(max_num_news==0) //primissimo accesso all'app
            max_num_news = (int)(((AvailableSpace.getTotalDiskSpace())/100*4.5/(1000000))/SIZE_SAVED_NEWS); //circa metà del valore massimo di news che posso salvare

        Log.d(TAG,"max_num_news="+max_num_news);

        if(myRepository==null)
            Toast.makeText(view.getContext(),"Please, click on Top News(bottom left) then come back",Toast.LENGTH_LONG).show();
        else {


           if(max_num_news>10) {

               /*
                * eliminazione di notizie saved
                *
                * */
               if (myRepository.getFavoritesArticle().size() >= max_num_news) {
                /*
                 elimino il 20% (rispetto a max_num_news)
                 delle news in saved...quali? Quelle con data MENO recente.


                 */
                   List<Article> favoriteArticles = new LinkedList<>();
                   favoriteArticles = myRepository.getFavoritesArticle();
                   Log.d(TAG, "number of saved news=" + favoriteArticles.size());
                   Log.d(TAG, "max_num_news=" + max_num_news);


                   for (int i = 0; i < 0.2 * max_num_news; i++)
                       myRepository.deleteArticle(favoriteArticles.get(favoriteArticles.size() - i - 1).getUrl());

               }

               /*
                * Allerta eliminazione notize da saved a
                * partire da quando mancano 10 news al numero massimo            *
                * */
               else if (myRepository.getFavoritesArticle().size() >= max_num_news - 10) {
                   //final CharSequence[] items = {"Ok"};
                   AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                   builder.setTitle("                !!! ATTENTION !!!");
                   builder.setMessage("If you don't increase the number of news " +
                           "the app can save on Saved, you will lose them." +
                           "\nGo in Settings, click the first voice and then set the parameter.\nThank you.");

                   builder.show();
               }

           }

           else{ //max_num_news < 10 ----> caso ESTREMO ....

               if (myRepository.getFavoritesArticle().size() >= max_num_news) {
                /*
                 elimino il 20% (rispetto a max_num_news)
                 delle news in saved...quali? Quelle con data MENO recente.


                 */
                   List<Article> favoriteArticles = new LinkedList<>();
                   favoriteArticles = myRepository.getFavoritesArticle();
                   Log.d(TAG, "number of saved news=" + favoriteArticles.size());
                   Log.d(TAG, "max_num_news=" + max_num_news);


                   for (int i = 0; i < 0.2 * max_num_news; i++)
                       myRepository.deleteArticle(favoriteArticles.get(favoriteArticles.size() - i - 1).getUrl());

               }


               /*
                * Allerta eliminazione notize da saved a
                * partire da quando mancano 10 news al numero massimo            *
                * */
               else if (myRepository.getFavoritesArticle().size() >= max_num_news - 1) {
                   final CharSequence[] items = {"Ok"};
                   AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                   builder.setTitle("                !!! ATTENTION !!!");
                   builder.setMessage("If you don't increase the number of news " +
                           "the app can save on Saved, you will lose them." +
                           "\nGo in Settings, click the first voice and then set the parameter.\nThank you.");

                   builder.show();
               }

           }

            recyclerView = view.findViewById(R.id.recyclerView_saved);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(new NewsListAdapter(context, new NewsApiResponse(myRepository.getFavoritesArticle())));
        }

        return view;
    }




}
