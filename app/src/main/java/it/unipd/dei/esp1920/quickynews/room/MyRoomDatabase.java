package it.unipd.dei.esp1920.quickynews.room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {News.class,RssNews.class},version = 1,exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase
{
    abstract NewsDao newsDao();
    abstract RssNewsDao rssNewsDao();

    private static volatile MyRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4; //TODO: capire quanti usarne effettivamente
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS); //singleton creation (singleton is useful
                                                                                                         // when exactly one object is needed)
    static MyRoomDatabase getDatabase(final Context context)
    {
        if(INSTANCE==null){
            synchronized (MyRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            MyRoomDatabase.class,"news_database").addCallback(mRoomDataBaseCallBack).build();
                }
            }
        }

        return INSTANCE;

    }


    private static RoomDatabase.Callback mRoomDataBaseCallBack = new RoomDatabase.Callback(){
      @Override
      public void onOpen(@NonNull SupportSQLiteDatabase db){
          super.onOpen(db);
          Log.d("MyRoomDatabase","onOpen() qui");
          databaseWriteExecutor.execute(() -> {
              RssNewsDao rssDao = INSTANCE.rssNewsDao();
              RssNews rss = new RssNews(3,"www.prova.it","Provemo","me piaxe provare","22.05.20","provetta");
              rssDao.insertRssNews(rss);
          });
      }
    };


}
