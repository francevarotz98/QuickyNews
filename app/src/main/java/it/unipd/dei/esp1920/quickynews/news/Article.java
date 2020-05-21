package it.unipd.dei.esp1920.quickynews.news;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Article implements Comparable<Article> {
    private final SimpleDateFormat FORMATTER1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
    private final SimpleDateFormat FORMATTER2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", Locale.US);

    private Source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private Date publishedAt;
    private String content;
    // private String category;

    public Article() {
        this.source = null;
        this.author = null;
        this.title = null;
        this.description = null;
        this.url = null;
        this.urlToImage = null;
        this.publishedAt = null;
        this.content = content;
        // this.category = category;
    }

    public Article(Source source, String author, String title, String description, String url, String urlToImage, String publishedAt, String content /*, String category */) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
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
            }
            catch (ParseException e) {
                try {
                    Date format2 = FORMATTER2.parse(publishedAt.trim());
                    publishedAt = FORMATTER1.format(format2);
                    this.publishedAt = FORMATTER1.parse(publishedAt);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
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

    public String getStringPublishedAt() {
        return FORMATTER1.format(this.publishedAt);
    }

    public Date getDatePublishedAt() { return this.publishedAt; }

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

    @Override
    public int compareTo(Article o) {
        return this.publishedAt.compareTo(o.getDatePublishedAt());
    }
}
