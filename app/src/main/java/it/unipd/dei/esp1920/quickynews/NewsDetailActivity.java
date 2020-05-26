package it.unipd.dei.esp1920.quickynews;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;

import java.io.IOException;

import it.unipd.dei.esp1920.quickynews.fetch.FeedListAdapter;
import it.unipd.dei.esp1920.quickynews.fragments.GetNewsTask;

public class NewsDetailActivity extends AppCompatActivity implements GetNewsTask.AsyncResponse {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        /*
        mWebView = findViewById(R.id.news_detail_webView);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if(getIntent() != null) {
            mWebView.loadUrl(getIntent().getStringExtra("url"));
        }
        */

        if(getIntent() != null) {
            mWebView = findViewById(R.id.news_detail_text);
            new GetNewsTask(this).execute(getIntent().getStringExtra("url"));
        }
    }

    public void processFinish(String output) {
        mWebView.loadDataWithBaseURL(null, output, "text/html", "utf-8", null);
    }
}
