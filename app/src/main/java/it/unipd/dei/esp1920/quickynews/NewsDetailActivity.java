package it.unipd.dei.esp1920.quickynews;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;
import it.unipd.dei.esp1920.quickynews.fragments.TopNews;
import it.unipd.dei.esp1920.quickynews.news.Article;
import it.unipd.dei.esp1920.quickynews.news.GetNewsTask;
import it.unipd.dei.esp1920.quickynews.news.MyRepository;

public class NewsDetailActivity extends AppCompatActivity implements GetNewsTask.AsyncResponse {
    private static final String TAG = "NewsDetailActivity";

    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private MyRepository myRepository;
    private static final ViewGroup.LayoutParams MM_LAYOUT_PARAMS = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    //private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");

        myRepository = TopNews.getRepository();
        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(MM_LAYOUT_PARAMS);

        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(MM_LAYOUT_PARAMS);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        if(NetConnectionReceiver.isConnected(this)) {
            if (getIntent() != null) {
                String url = getIntent().getStringExtra("url");
                String source_id = getIntent().getStringExtra("source_id");

                // se l'url non utilizza il protocollo https...
                if (url.substring(0, 5).equals("http:"))
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
                new GetNewsTask(this, this).execute(url, source_id);
            }
        }

        else{ //mobile phone not connected
            String pageHTML="";
            if(getIntent()!=null){
                Log.d(TAG,"------------DEBUG---------");
                String url = getIntent().getStringExtra("url");
                Article article = myRepository.getArticle(url);
                pageHTML = article.getPageHTML();
                Log.d(TAG,"article title= "+article.getTitle());
            }

            setContentView(R.layout.news_detail_offline);
            TextView t = null ;
            int start=0;
            int end=8;
            while(end<pageHTML.length()){
                t =  findViewById(R.id.pageHTML);
                if(pageHTML.length() >= 8 && pageHTML.substring(start,end).equals("BOLDDLOB")) {
                    t.setText(pageHTML.substring(start,end));
                    t.setTypeface(null, Typeface.BOLD);
                    Log.d(TAG,"IF :pageHTML.substring(start,end+8) = "+pageHTML.substring(start,end));
                    start+=8;
                    end+=8;
                }
                else{
                    t.setTypeface(null,Typeface.NORMAL);
                    t.setText(pageHTML.substring(start,end));
                    Log.d(TAG,"ELSE: pageHTML.substring(start,end+8) = "+pageHTML.substring(start,end));
                    start+=8;
                    end+=8;
                    }

                }
                try {
                    t.setText(pageHTML);
                    t.setTextSize(18);
                }
                catch(NullPointerException e){
                    e.printStackTrace();
                    // mi serve questo try catch per quando non ho internet
                    // e tento di entrare nella pag HTML di una news NON presente
                    // nel db
                }
                /*TODO: finire parsing fatto bene*/

                Log.d(TAG,"------------END DEBUG---------");
                //da finire
            }


    }

    // viene invocato dopo che GetNewsTask è stato completato
    public void processFinish(ArrayList<String> result) {
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
    }









}
