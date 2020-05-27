package it.unipd.dei.esp1920.quickynews.news;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.unipd.dei.esp1920.quickynews.news.Article;
import it.unipd.dei.esp1920.quickynews.news.MyRepository;

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

public class ArticleViewModel extends AndroidViewModel
{
    private MyRepository mRepository;
    private LiveData<List<Article>> mAllArticle;

    public ArticleViewModel(Application application) {
        super(application);
        mRepository = new MyRepository(application);
        mAllArticle = mRepository.getAllArticle();
    }


    public LiveData<List<Article>> getAllArticle(){
        return mAllArticle;
    }


    public void insertArticle(Article r){
        mRepository.insertArticle(r);
    }



}
