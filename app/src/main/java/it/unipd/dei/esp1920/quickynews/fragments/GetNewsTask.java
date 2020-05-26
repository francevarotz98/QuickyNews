package it.unipd.dei.esp1920.quickynews.fragments;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.LinkedList;

import it.unipd.dei.esp1920.quickynews.fetch.Item;

public class GetNewsTask extends AsyncTask<String, Void, String> {

    private final static String TAG="GetNewsTask";

    private GetNewsTask.AsyncResponse delegate;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public GetNewsTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... urls) {
        Log.d(TAG, "doInBackground()");
        String url = urls[0];
        try {
            //Get Document object after parsing the html from given url.
            return Jsoup.connect(url).get().html();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
