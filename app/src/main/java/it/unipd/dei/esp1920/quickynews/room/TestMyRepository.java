package it.unipd.dei.esp1920.quickynews.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TestMyRepository
{

    //private NewsDao mNewsDao;
    private  RssNewsDao mRssNewsDao;
    //private LiveData<List<News>> mAllNews;
    private LiveData<List<RssNews>> mAllRssNews;

    public TestMyRepository(Application app) {
        TestMyRoomDatabase db = TestMyRoomDatabase.getDatabase(app);
      //  mNewsDao = db.newsDao();
        mRssNewsDao = db.rssNewsDao();
        //get all Livedata News and Rssnews
       // mAllNews = mNewsDao.getNews();
        mAllRssNews = mRssNewsDao.getRssNews();
    }


   /* public LiveData<List<News>> getAllNews(){
        return mAllNews;
    }*/

    public LiveData<List<RssNews>> getAllRssNews(){
        return mAllRssNews;
    }

  /*  public void insertNews(News n){
        MyRoomDatabase.databaseWriteExecutor.execute(()-> mNewsDao.insertNews(n));
    }
  */
    public void insertRssNews(RssNews n){
        TestMyRoomDatabase.databaseWriteExecutor.execute(()-> mRssNewsDao.insertRssNews(n));
    }

    //TODO: implementare metodi DELETE?



}
