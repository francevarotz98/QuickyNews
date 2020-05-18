package it.unipd.dei.esp1920.quickynews.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import it.unipd.dei.esp1920.quickynews.R;
import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;
import it.unipd.dei.esp1920.quickynews.fetch.Item;

public class TopNews extends Fragment implements GetFeedTask.AsyncResponse {

    private final static String TAG="Top News";

    private LinkedList<Item> feedList = new LinkedList<>();
    private RecyclerView recyclerView;
    private FeedListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView()");
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = v.findViewById(R.id.recyclerView);
        adapter = new FeedListAdapter(getContext(), feedList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(NetConnectionReceiver.isConnected(getContext())) {
            Log.d(TAG,"GetFeedTask.execute()");
            // new GetFeedTask(this).execute("https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml");
            new GetFeedTask(this).execute("https://www.theguardian.com/international/rss");
        }
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void processFinish(LinkedList<Item> output) {
        recyclerView.setAdapter(new FeedListAdapter(getContext(), output));
        adapter.notifyDataSetChanged();
    }
}
