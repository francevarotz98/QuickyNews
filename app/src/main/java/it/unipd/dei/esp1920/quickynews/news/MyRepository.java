package it.unipd.dei.esp1920.quickynews.news;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MyRepository
{

    //private NewsDao mNewsDao;
    private  ArticleDao mArticleDao;
    //private LiveData<List<News>> mAllNews;
    private LiveData<List<Article>> mAllArticle;

    public MyRepository(Application app) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(app);
        //  mNewsDao = db.newsDao();
        mArticleDao = db.articleDao();
        //get all Livedata News and Rssnews
        // mAllNews = mNewsDao.getNews();
        mAllArticle = mArticleDao.getArticle();
    }

    public LiveData<List<Article>> getAllArticle(){
        return mAllArticle;
    }


    public void insertArticle(Article n){
        MyRoomDatabase.databaseWriteExecutor.execute(()-> mArticleDao.insertArticle(n));
    }

    //TODO: implementare metodi DELETE?



}
