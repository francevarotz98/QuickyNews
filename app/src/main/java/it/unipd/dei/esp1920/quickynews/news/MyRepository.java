package it.unipd.dei.esp1920.quickynews.news;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MyRepository
{

    private  ArticleDao mArticleDao;
    private LiveData<List<Article>> mAllArticle;
    //private int numArticle;
    //private String mTitle;

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
         return mArticleDao.countArticle();
    }
    /*
    public String titleArticle(){
        return mArticleDao.titleArticle();
    }
    */

    //TODO: implementare metodi DELETE?



}
