package it.unipd.dei.esp1920.quickynews.news;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM article_table ORDER BY publishedAt")
    LiveData<List<Article>> getArticle();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertArticle(Article a);

    @Query("DELETE FROM article_table WHERE url= :u" )
    void deleteArticle(String u);

    //TODO: update method? Is it necessary?


}




