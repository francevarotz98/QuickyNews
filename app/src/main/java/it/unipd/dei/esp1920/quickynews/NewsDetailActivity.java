package it.unipd.dei.esp1920.quickynews;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import it.unipd.dei.esp1920.quickynews.fragments.GetNewsTask;

public class NewsDetailActivity extends AppCompatActivity implements GetNewsTask.AsyncResponse {
    private static final String TAG = "NewsDetailActivity";

    private ScrollView scrollView;
    private LinearLayout linearLayout;

    private static final ViewGroup.LayoutParams MM_LAYOUT_PARAMS = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(MM_LAYOUT_PARAMS);

        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(MM_LAYOUT_PARAMS);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        if(getIntent() != null) {
            String url = getIntent().getStringExtra("url");
            String source_id = getIntent().getStringExtra("source_id");

            // se l'url non utilizza il protocollo https...
            if(url.substring(0,5).equals("http:"))
                url = "https" + url.substring(4);
            Log.d(TAG, "url = " + url);

            if(url.substring(0,25).equals("https://us.cnn.com/videos")) {
                VideoView vv = new VideoView(this);
                MediaController mc = new MediaController(this);
                vv.setMediaController(mc);
                vv.setVideoURI(Uri.parse("https://cnnios-f.akamaihd.net/i/cnn/big/" + url.substring(26) + "_ios_,440,650,840,1240,3000,5500,.mp4.csmil/master.m3u8"));
                linearLayout.addView(vv);
                mc.setAnchorView(linearLayout);
                vv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,650));
            }
            new GetNewsTask(this, this).execute(url,source_id);
        }
    }

    // viene invocato dopo che GetNewsTask è stato completato
    public void processFinish(ArrayList<View> result) {

        while(!result.isEmpty()) {
            View v = result.remove(0);
            if(v.getParent() != null) {
                ((ViewGroup)v.getParent()).removeView(v);
            }
            linearLayout.addView(v);
        }
        scrollView.addView(linearLayout);
        setContentView(scrollView);
    }
}
