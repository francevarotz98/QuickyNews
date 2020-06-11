package it.unipd.dei.esp1920.quickynews.news;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "article_table")
public class Article implements Comparable<Article> {
    private static final String TAG = "Article";

    private static final SimpleDateFormat FORMATTER1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

    private Source source;
    private String author;
    private String title;
    private String description;
    @PrimaryKey //autogenerate primary keys TODO: capire se sia giusto URL
    @NonNull
    private String url;
    private String urlToImage;
    private Date publishedAt;
    private String content;
    private String category;
    private boolean isFavorite;
    private String pageHTML;
    private String id;

    //for POJOs
    public Article() {
        this.source = null;
        this.author = null;
        this.title = null;
        this.description = null;
        this.url = null;
        this.urlToImage = null;
        this.publishedAt = null;
        this.content = null;
        this.category = null;
        this.isFavorite=false;
        pageHTML="";
        this.id="";
    }


    public Article(Source source, String author, String title, String description, String url, String urlToImage, String publishedAt, String content /*, String category */) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.isFavorite=false;
        this.pageHTML="";
        this.category=null;

        if(publishedAt.equals("null")) {
            try {
                this.publishedAt = FORMATTER1.parse("0000-00-00T00:00:00+00:00");
            }
            catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            if(publishedAt.substring(publishedAt.length()-1).equals("Z")) {
                publishedAt = publishedAt.substring(0, publishedAt.length() - 1);
                publishedAt += "+00:00";
            }
            try {
                this.publishedAt = FORMATTER1.parse(publishedAt.trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.content = content;

    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getStringPublishedAt() {
        return FORMATTER1.format(this.publishedAt);
    }

    @NonNull
    public Date getPublishedAt() { return this.publishedAt; }  //it was getDatePublishedAt but it was changed because of room

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIsFavorite(){
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite){
        this.isFavorite=isFavorite;
    }

    public String getPageHTML(){
        return pageHTML;
    }

    public void setPageHTML(String pageHTML){
        this.pageHTML=pageHTML;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }


    @Override
    public int compareTo(Article o) {
        return this.publishedAt.compareTo(o.getPublishedAt());
    }


}
