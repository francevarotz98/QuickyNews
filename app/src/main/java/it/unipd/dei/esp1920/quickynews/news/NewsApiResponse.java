package it.unipd.dei.esp1920.quickynews.news;

import java.util.List;

public class NewsApiResponse {
    private List<Article> articles;

    public NewsApiResponse(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
