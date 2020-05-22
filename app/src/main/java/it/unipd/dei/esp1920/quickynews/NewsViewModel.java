package it.unipd.dei.esp1920.quickynews;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.unipd.dei.esp1920.quickynews.room.MyRepository;
import it.unipd.dei.esp1920.quickynews.room.News;
import it.unipd.dei.esp1920.quickynews.room.RssNews;

/*
 * N.B.!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *     DO NOT(!!!!) STORE Activity/Fragment/View or
 *     their Contexts in a ViewModel.
 *
 *  I need a ViewModel class to, e.g., save data
 *  when the device is rotated.
 *
 * */

public class NewsViewModel extends AndroidViewModel
{
    private MyRepository mRepository;
    private LiveData<List<News>> mAllNews;
    private LiveData<List<RssNews>> mAllRssNews;

    public NewsViewModel(Application application) {
        super(application);
        mRepository = new MyRepository(application);
        mAllNews = mRepository.getAllNews();
        mAllRssNews = mRepository.getAllRssNews();
    }

    LiveData<List<News>> getAllNews(){
        return mAllNews;
    }

    LiveData<List<RssNews>> getAllRssNews(){
        return mAllRssNews;
    }

    public void insertNews(News n){
        mRepository.insertNews(n);
    }

    public void insertRssNews(RssNews r){
        mRepository.insertRssNews(r);
    }


    //TODO: creare NewListAdapter che estende RecyclerView

}
