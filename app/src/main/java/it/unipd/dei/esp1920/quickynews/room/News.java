package it.unipd.dei.esp1920.quickynews.room;



import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
//import androidx.room.PrimaryKey;


@Entity(tableName = "news_table"
        ,foreignKeys = {@ForeignKey(onDelete = CASCADE,entity = RssNews.class,parentColumns = "id_Rss",childColumns = "id_News")})
public class News
{

    //@ForeignKey(onDelete = CASCADE,entity = RssNews.class,parentColumns = "id_Rss",childColumns = "id_News")
    @PrimaryKey //in teoria è da mettere (letto su codelabs)
    @NonNull
    private int id_News;
    private String text;
    //private final String TAG="News";


    public News(@NonNull int id_News,String text){
        this.id_News=id_News;
        this.text=text;
    }

    @NonNull
    public int getId_News(){
        //Log.d("News","getId_News()");
        return this.id_News;
    }


    public String getText(){
        //Log.d("News","getText()");
        return this.text;
    }



}

