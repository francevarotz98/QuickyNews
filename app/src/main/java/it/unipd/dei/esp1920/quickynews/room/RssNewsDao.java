package it.unipd.dei.esp1920.quickynews.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface RssNewsDao
{
    @Query("SELECT * FROM rssnews_table ORDER BY date")
    LiveData<List<RssNews>> getRssNews();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRssNews(RssNews n);

    @Query("DELETE FROM rssnews_table WHERE id_Rss=:id")
    void deleteRssNews(int id);




    //TODO: UPDATE method ??


}
