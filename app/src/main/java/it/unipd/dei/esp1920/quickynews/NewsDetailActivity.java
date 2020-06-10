package it.unipd.dei.esp1920.quickynews;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.content.ContextCompat;

import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;
import it.unipd.dei.esp1920.quickynews.fragments.TopNews;
import it.unipd.dei.esp1920.quickynews.news.MyRepository;

public class NewsDetailActivity extends AppCompatActivity /* implements GetNewsTask.AsyncResponse */ {
    private static final String TAG = "NewsDetailActivity";

    private MyRepository myRepository;
    private int fontSize,mFontSize;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate()");

        super.onCreate(savedInstanceState);

        SharedPreferences preferences2 = getSharedPreferences("fontSizeKey",MODE_PRIVATE);
        fontSize = preferences2.getInt("seekBarFontValue", 35);
        if(fontSize<20) {       //very small
            mFontSize=12;
        }
        else if(fontSize<40) {      // small
            mFontSize=17;
        }
        else if(fontSize<60) {      //normal
            mFontSize=22;
        }
        else if(fontSize<80) {      //big
            mFontSize=27;
        }
        else{                       //very big
            mFontSize=32;
        }
        myRepository = TopNews.getRepository();

        String pageHTML = "";
        if (getIntent() != null) {
            //String pageHTML = "";
            setContentView(R.layout.news_detail_offline);
            TextView textViewPageHTML = findViewById(R.id.pageHTML);
            TextView  textViewTitlePageHTML = findViewById(R.id.title_pageHTML);
            String url = getIntent().getStringExtra("url");
            try {
                pageHTML = myRepository.getPageHTML(url);
                Log.d(TAG,"article="+myRepository.getArticle(url).getTitle());
                Log.d(TAG,"pageHTML="+pageHTML);
                if(pageHTML.length()==0)
                    textViewTitlePageHTML.setText("Sorry, this news was not downloaded.\nRetry when you will be connected to internet.");
                else {
                    textViewTitlePageHTML.setText(myRepository.getArticle(url).getTitle());
                    textViewPageHTML.setText(pageHTML);
                }
                textViewTitlePageHTML.setTextSize((float)(mFontSize+0.2*mFontSize));
                textViewPageHTML.setTextSize(mFontSize);
            }
            catch(NullPointerException e) {
                e.printStackTrace();
                textViewPageHTML.setText(R.string.not_saved);
                textViewPageHTML.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textViewPageHTML.setTextSize(mFontSize);
            }
        }
    }
}
