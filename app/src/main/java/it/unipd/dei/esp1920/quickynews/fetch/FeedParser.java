package it.unipd.dei.esp1920.quickynews.fetch;

import java.io.IOException;
import java.util.List;

import it.unipd.dei.esp1920.quickynews.news.Article;

public interface FeedParser {
    List<Item> parse() throws IOException;
}
