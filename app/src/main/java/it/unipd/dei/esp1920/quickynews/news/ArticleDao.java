package it.unipd.dei.esp1920.quickynews.news;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM article_table ORDER BY publishedAt DESC")
    List<Article>/*LiveData<List<Article>>*/ getAllArticle();

    @Insert(onConflict = OnConflictStrategy.IGNORE) //((replace perchè se ci dovesse essere un aggiornamento ==> news aggiornata))
    void insertArticle(Article a);                 // NO: uso IGNORE sennò mi cambia il valore dell'attributo isFavorite.

    @Query("DELETE FROM article_table WHERE url= :u" )
    void deleteArticle(String u);

    @Query("SELECT count(url) FROM article_table")
    int countArticle();

    @Query("SELECT * FROM article_table WHERE isFavorite=1 ORDER BY publishedAt DESC") //N.B.: in Room's query, in order to confront boolean values, remember
        List<Article> getAllFavorites();                                              //  that : 1(Room) == true(Java) and 0(Room) == false(Java)

    @Query("UPDATE article_table SET isFavorite= :isFavorite WHERE url = :url")
    void setFavoriteArticle(String url, boolean isFavorite);
    /*
    @Query("SELECT title FROM article_table LIMIT 1") //query di debug
    String titleArticle();
    */
    //TODO: update method? Is it necessary?


}




