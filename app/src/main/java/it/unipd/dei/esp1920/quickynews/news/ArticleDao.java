package it.unipd.dei.esp1920.quickynews.news;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM article_table ORDER BY publishedAt DESC")
    List<Article> getAllArticle();

    @Query("SELECT * FROM article_table WHERE url = :url")
    Article getArticle(String url);

    @Insert(onConflict = OnConflictStrategy.IGNORE) //((replace perchè se ci dovesse essere un aggiornamento ==> news aggiornata))
    void insertArticle(Article a);                 // NO: uso IGNORE sennò mi cambia il valore dell'attributo isFavorite.

    @Query("DELETE FROM article_table WHERE url= :u" )
    void deleteArticle(String u);

    @Query("SELECT count(url) FROM article_table")
    int countArticle();

    @Query("SELECT * FROM article_table WHERE isFavorite=1 ORDER BY publishedAt DESC") //N.B.: in Room's query, in order to confront boolean values, remember
    List<Article> getAllFavorites();                                              //  that : 1(Room) == true(Java) and 0(Room) == false(Java)


    @Query("SELECT isFavorite FROM article_table WHERE url = :url")
    boolean isFavoriteArticle(String url);


    @Query("UPDATE article_table SET isFavorite= :isFavorite WHERE url = :url")
    void setFavoriteArticle(String url, boolean isFavorite);


    @Query("SELECT pageHTML FROM article_table WHERE url = :url")
    String getPageHTML(String url);


    @Query("UPDATE article_table SET pageHTML = :pageHTML WHERE url = :url")
    void setPageHTML(String url,String pageHTML);


    @Query("SELECT * FROM article_table WHERE category = 'sport' ORDER BY publishedAt DESC")
    List<Article> getSportArticle();


    @Query("SELECT * FROM article_table WHERE category = 'technology' ORDER BY publishedAt DESC ")
    List<Article> getTechnologyArticle();


    @Query("SELECT * FROM article_table WHERE category = 'business' ORDER BY publishedAt DESC ")
    List<Article> getBusinessArticle();


    @Query("SELECT * FROM article_table WHERE category = 'science' ORDER BY publishedAt DESC")
    List<Article> getScienceArticle();


    @Query("UPDATE article_table SET category = :category WHERE url = :url")
    void setArticleCategory(String url, String category);


    @Query("UPDATE article_table SET id = :id WHERE url = :url")
    public void setId(String url, String id);

    @Query("SELECT id FROM article_table WHERE url = :url")
    public String getId(String url);



}