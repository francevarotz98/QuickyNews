package it.unipd.dei.esp1920.quickynews.news;

import java.util.List;

public class NewsApiResponse {
    private String status;
    private List<Article> articles;

    public NewsApiResponse(String status, List<Article> articles) {
        this.status = status;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
