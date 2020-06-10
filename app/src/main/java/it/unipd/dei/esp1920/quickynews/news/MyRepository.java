package it.unipd.dei.esp1920.quickynews.news;

import android.app.Application;

import java.util.Date;
import java.util.List;

public class MyRepository
{
    private  ArticleDao mArticleDao;
    private List<Article> mAllArticle;
    private int numArticle;


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

    public void setArticleCategory(String url,String category){
        MyRoomDatabase.databaseWriteExecutor.execute( ()-> mArticleDao.setArticleCategory(url,category));
    }

    public List<Article> getSportArticle(){
        return mArticleDao.getSportArticle();
    }

    public List<Article> getTechnologyArticle(){
        return mArticleDao.getTechnologyArticle();
    }

    public List<Article> getBusinessArticle(){
        return mArticleDao.getBusinessArticle();
    }

    public List<Article> getScienceArticle(){
        return mArticleDao.getScienceArticle();
    }

    public String getId(String url){
        return mArticleDao.getId(url);
    }

    public void setId(String url, String id){
        MyRoomDatabase.databaseWriteExecutor.execute( ()-> mArticleDao.setId(url,id));
    }

    public void deleteArticle(String url) {
        MyRoomDatabase.databaseWriteExecutor.execute( ()-> mArticleDao.deleteArticle(url));
    }


}
