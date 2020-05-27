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
    private int numArticle;

    public MyRepository(Application app) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(app);
        mArticleDao = db.articleDao();
        mAllArticle = mArticleDao.getAllArticle();

        //numArticle = mArticleDao.countArticle();
    }

    public LiveData<List<Article>> getAllArticle(){
        return mAllArticle;
    }


    public void insertArticle(Article n){
        MyRoomDatabase.databaseWriteExecutor.execute(()-> mArticleDao.insertArticle(n));
    }

    public int countArticle(){
         return numArticle;
    }

    //TODO: implementare metodi DELETE?



}
