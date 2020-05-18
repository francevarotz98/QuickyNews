package it.unipd.dei.esp1920.quickynews.fragments;

import android.os.AsyncTask;
import android.util.Log;


import java.io.IOException;
import java.security.Guard;
import java.util.LinkedList;

import it.unipd.dei.esp1920.quickynews.fetch.GuardianXmlParser;
import it.unipd.dei.esp1920.quickynews.fetch.Item;
import it.unipd.dei.esp1920.quickynews.fetch.NewYorkTimesXmlParser;

class GetFeedTask extends AsyncTask<String, Void, LinkedList<Item>> {

    private final static String TAG="GetFeedTask";

    private AsyncResponse delegate;

    public interface AsyncResponse {
        void processFinish(LinkedList<Item> output);
    }

    public GetFeedTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected LinkedList<Item> doInBackground(String... urls) {
        String url = urls[0];
        // NewYorkTimesXmlParser parser = new NewYorkTimesXmlParser(url);
        GuardianXmlParser parser = new GuardianXmlParser(url);
        try {
            // Log.d(TAG,"new NewYorkTimesXmlParser(url).parse()");
            Log.d(TAG,"new GuardianXmlParser(url).parse()");
            return parser.parse();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(LinkedList<Item> result) {
        delegate.processFinish(result);
    }
}
