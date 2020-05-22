package it.unipd.dei.esp1920.quickynews.room;



import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rssnews_table")
public class RssNews
{

    @PrimaryKey(autoGenerate = true) //autogenerate primary keys
    @NonNull
    private int id_Rss;
    private String URL;
    private String title;
    private String description;
    private String date;
    private String category;
    //private final String  TAG="RssNews";

    public RssNews(@NonNull int id_Rss, String URL,String title,String description,String date,String category){
        this.id_Rss=id_Rss;
        this.URL=URL;
        this.title=title;
        this.description=description;
        this.date=date;
        this.category=category;
    }

    @NonNull
    public int getId_Rss(){
        //Log.d("RssNews","getId_Rss()");
        return this.id_Rss;
    }

    public String getURL(){
        //Log.d("RssNews","getURL()");
        return this.URL;
    }

    public String getTitle(){
       // Log.d("RssNews","getTitle()");
        return this.title;
    }

    public String getDescription(){
       // Log.d("RssNews","getDescription()");
        return this.description;
    }

    public String getDate(){
       // Log.d("RssNews","getDate()");
        return this.date;
    }

    public String getCategory(){
       // Log.d("RssNews","getCategory()");
        return this.category;
    }



}
