package it.unipd.dei.esp1920.quickynews.news;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MyRepository
{

    private  ArticleDao mArticleDao;
    private List<Article>/*LiveData<List<Article>>*/ mAllArticle;
    private int numArticle;
    //private String mTitle;

    public MyRepository(Application app) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(app);
        mArticleDao = db.articleDao();
        mAllArticle = mArticleDao.getAllArticle();

        numArticle = mArticleDao.countArticle();
    }

    public List<Article> getAllArticle(){
        return mAllArticle;
    }

    public Article getArticle(String url){
        return mArticleDao.getArticle(url);
    }


    public void insertArticle(Article n){
        MyRoomDatabase.databaseWriteExecutor.execute(() -> mArticleDao.insertArticle(n));
    }

    public int countArticle(){
         return numArticle;
    }

    public List<Article>  getFavoritesArticle(){
        return mArticleDao.getAllFavorites();
    }

    public void setFavorite(String url, boolean isFavorite){
        MyRoomDatabase.databaseWriteExecutor.execute( () -> mArticleDao.setFavoriteArticle(url, isFavorite));
    }

    public boolean isFavoriteArticle(String url){
        return mArticleDao.isFavoriteArticle(url);
    }

    public void setPageHTML(String url, String pageHTML){
        MyRoomDatabase.databaseWriteExecutor.execute( () -> mArticleDao.setPageHTML(url, pageHTML));
    }

    public String getPageHTML(String url){
        return  mArticleDao.getPageHTML(url);
    }
     //TODO: CAPIRE COME IMPLEMENTARE STO METODO....




    //TODO: implementare metodo DELETE



}
