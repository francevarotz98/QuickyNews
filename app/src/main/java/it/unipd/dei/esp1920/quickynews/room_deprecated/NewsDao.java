package it.unipd.dei.esp1920.quickynews.room_deprecated;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao
{
    @Query("SELECT * FROM news_table")
    LiveData<List<News>> getNews();

    @Insert(onConflict = OnConflictStrategy.IGNORE) //in case of conflict do nothing
    void insertNews(News n);

    @Query("DELETE FROM news_table WHERE id_News= :id") //delete the row where id_News == parameter
    void deleteNews(int id);                            // of the function id


    //TODO: UPDATE method ??
}
