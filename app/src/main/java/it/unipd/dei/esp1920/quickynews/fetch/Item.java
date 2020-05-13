package it.unipd.dei.esp1920.quickynews.fetch;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Item {
    static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);

    private String title;
    private URL link;
    private String description;
    private String category;
    private Date date;

    public Item(String title, URL link, String description, String category, Date date) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public Item() {
        this.title = null;
        this.link = null;
        this.description = null;
        this.category = null;
        this.date = null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() { return this.title; }

    public void setLink(String link) {
        try {
            this.link = new URL(link);
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLink() { return this.link.toString(); }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() { return this.description; }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() { return this.category; }

    public void setDate(String date) {
        // pad the date if necessary
        /* while (!date.endsWith("00")) {
            date += "0";
        } */
        try {
            this.date = FORMATTER.parse(date.trim());
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDate() { return FORMATTER.format(this.date); }
}
