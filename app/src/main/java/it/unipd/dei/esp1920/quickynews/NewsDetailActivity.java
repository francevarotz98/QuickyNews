package it.unipd.dei.esp1920.quickynews;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;
import it.unipd.dei.esp1920.quickynews.fragments.TopNews;
import it.unipd.dei.esp1920.quickynews.news.GetNewsTask;
import it.unipd.dei.esp1920.quickynews.news.MyRepository;

public class NewsDetailActivity extends AppCompatActivity /* implements GetNewsTask.AsyncResponse */ {
    private static final String TAG = "NewsDetailActivity";

    private WebView webView;
    private ProgressBar progressBar;

    // private ScrollView scrollView;
    // private LinearLayout linearLayout;
    // private static final ViewGroup.LayoutParams MM_LAYOUT_PARAMS = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    private MyRepository myRepository;
    private int fontSize,mFontSize;
    //private Context context;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate()");

        super.onCreate(savedInstanceState);
        /*
        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(MM_LAYOUT_PARAMS);

        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(MM_LAYOUT_PARAMS);
        linearLayout.setOrientation(LinearLayout.VERTICAL);*/

        if(NetConnectionReceiver.isConnected(this) && getIntent() != null) {
            String mUrl = getIntent().getStringExtra("url");

            setContentView(R.layout.news_detail);
            // String source_id = getIntent().getStringExtra("source_id");

            // se l'url non utilizza il protocollo https...
            /*if (url.substring(0, 5).equals("http:"))
                url = "https" + url.substring(4);
            Log.d(TAG, "url = " + url);

            if (url.substring(0, 25).equals("https://us.cnn.com/videos")) {
                VideoView vv = new VideoView(this);
                MediaController mc = new MediaController(this);
                vv.setMediaController(mc);
                vv.setVideoURI(Uri.parse("https://cnnios-f.akamaihd.net/i/cnn/big/" + url.substring(26) + "_ios_,440,650,840,1240,3000,5500,.mp4.csmil/master.m3u8"));
                linearLayout.addView(vv);
                mc.setAnchorView(linearLayout);
                vv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 650));
            }
            new GetNewsTask(this, this).execute(url, source_id);*/

            progressBar =  findViewById(R.id.progressBar);
            webView = findViewById(R.id.webView);

            WebSettings webSettings = webView.getSettings();

            webSettings.setJavaScriptEnabled(true);
            webSettings.setAppCacheEnabled(false);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);


            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress >= 80) progressBar.setVisibility(View.GONE);
                }
            });

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

            webView.loadUrl(mUrl);
        }
        else { //mobile phone not connected
            SharedPreferences preferences2 = getSharedPreferences("fontSizeKey",MODE_PRIVATE);
            fontSize = preferences2.getInt("seekBarFontValue", 40);
            if(fontSize<20) {
                mFontSize=12;
            }
            else if(fontSize<40) {
                mFontSize=17;
            }
            else if(fontSize<60) {
                mFontSize=22;
            }
            else if(fontSize<80) {
                mFontSize=27;
            }
            else{
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
                    textViewPageHTML.setText(R.string.not_connected);
                    textViewPageHTML.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textViewPageHTML.setTextSize(mFontSize);

                }

                //da finire
            }
        }
    }

    // viene invocato dopo che GetNewsTask è stato completato
    /*public void processFinish(ArrayList<String> result) {
        if(result==null) {
            Log.d(TAG,"ATTENTION ++++ Null result on processFinish()");
            //result: blank view
        }
        else {
            Log.d(TAG,"processFinish()");
            TextView view;
            String viewed;
            while (!result.isEmpty()) {
                viewed = result.remove(0);
                view = new TextView(this);
                if(viewed.length() >= 8 && viewed.substring(0,8).equals("BOLDDLOB")) {
                    view.setText(viewed.substring(8));
                    view.setTypeface(null, Typeface.BOLD);
                }
                else view.setText(viewed);
                linearLayout.addView(view);
            }
            scrollView.addView(linearLayout);
            setContentView(scrollView);
        }
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        if(webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.removeAllViews();
            webView.destroy();
        }
    }
}
