package it.unipd.dei.esp1920.quickynews.news;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Article.class},version = 1,exportSchema = false)
@TypeConverters({Converters.class}) //in order to manage Source and ArrayList types
public abstract class MyRoomDatabase extends RoomDatabase
{
    abstract ArticleDao articleDao();

    private static volatile MyRoomDatabase INSTANCE; //create one and only one instance of MyRoomDatabase because of that volatile
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS); //singleton creation (singleton is useful
    // when exactly one object is needed)
    static MyRoomDatabase getDatabase(final Context context)
    {
        if(INSTANCE==null){
            synchronized (MyRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            MyRoomDatabase.class,"article_database").allowMainThreadQueries().addCallback(mRoomDataBaseCallBack).build();
                }
            }
        }

        return INSTANCE;

    }

    //Here we should put the code of the xml parser in order
    //to fulfill the screen with the news
    private static RoomDatabase.Callback mRoomDataBaseCallBack = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            Log.d("MyRoomDatabase","onOpen()");

        }
    };


}
