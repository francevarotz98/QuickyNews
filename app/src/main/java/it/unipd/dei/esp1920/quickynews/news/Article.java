package it.unipd.dei.esp1920.quickynews.news;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Article {
    static SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

    private Source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private Date publishedAt;
    private String content;
    // private String category;

    public Article(Source source, String author, String title, String description, String url, String urlToImage, String publishedAt, String content /*, String category */) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        if(publishedAt.equals("null")) {
            try {
                this.publishedAt = FORMATTER.parse("0000-00-00T00:00:00+0000");
            }
            catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            publishedAt = publishedAt.substring(0,publishedAt.length()-1);
            if(publishedAt.length() > 19) {
                publishedAt = publishedAt.substring(0,19);
            }
            publishedAt += "+0000";
            try {
                this.publishedAt = FORMATTER.parse(publishedAt.trim());
            }
            catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        this.content = content;
        // this.category = category;
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

    public String getPublishedAt() {
        return FORMATTER.format(this.publishedAt);
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /* public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    } */
}
