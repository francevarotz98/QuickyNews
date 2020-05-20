package it.unipd.dei.esp1920.quickynews.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MyRepository
{

    private NewsDao mNewsDao;
    private  RssNewsDao mRssNewsDao;
    private LiveData<List<News>> mAllNews;
    private LiveData<List<RssNews>> mAllRssNews;

    MyRepository(Application app)
    {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(app);
        mNewsDao = db.newsDao();
        mRssNewsDao = db.rssNewsDao();
        //get all Livedata News and Rssnews
        mAllNews = mNewsDao.getNews();
        mAllRssNews = mRssNewsDao.getRssNews();
    }


    LiveData<List<News>> getAllNews(){
        return mAllNews;
    }

    LiveData<List<RssNews>> getAllRssNews(){
        return mAllRssNews;
    }

    void insertNews(News n){
        MyRoomDatabase.databaseWriteExecutor.execute(()->{
            mNewsDao.insertNews(n);
        });
    }

    void insertRssNews(RssNews n){
        MyRoomDatabase.databaseWriteExecutor.execute(()->{
            mRssNewsDao.insertRssNews(n);
        });
    }



}
