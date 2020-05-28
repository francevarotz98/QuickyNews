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
    List<Article>/*LiveData<List<Article>>*/ getAllArticle();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertArticle(Article a);

    @Query("DELETE FROM article_table WHERE url= :u" )
    void deleteArticle(String u);

    @Query("SELECT count(url) FROM article_table")
    int countArticle();

    @Query("SELECT * FROM article_table WHERE isFavorite='True'")
    List<Article> getAllFavorites();
    /*
    @Query("SELECT title FROM article_table LIMIT 1") //query di debug
    String titleArticle();
    */
    //TODO: update method? Is it necessary?


}




