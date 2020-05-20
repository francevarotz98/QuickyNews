package it.unipd.dei.esp1920.quickynews.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import it.unipd.dei.esp1920.quickynews.MainActivity;
import it.unipd.dei.esp1920.quickynews.R;
import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;
import it.unipd.dei.esp1920.quickynews.fetch.Item;
import it.unipd.dei.esp1920.quickynews.news.Article;
import it.unipd.dei.esp1920.quickynews.news.NewsApiResponse;
import it.unipd.dei.esp1920.quickynews.news.NewsListAdapter;
import it.unipd.dei.esp1920.quickynews.news.Source;

public class TopNews extends Fragment implements GetFeedTask.AsyncResponse {

    private final static String TAG="Top News";

    // private LinkedList<Item> feedList = new LinkedList<>();
    private NewsApiResponse newsApiResponse;
    private List<Article> newsList;
    private RecyclerView recyclerView;
    private NewsListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView()");
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = v.findViewById(R.id.recyclerView);
        newsList = new LinkedList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new NewsListAdapter(getContext(), new NewsApiResponse("undefined", newsList)));
        fetchNews();
        /* if(NetConnectionReceiver.isConnected(getContext())) {
            Log.d(TAG,"GetFeedTask.execute()");
            // new GetFeedTask(this).execute("https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml");
            new GetFeedTask(this).execute("https://www.theguardian.com/international/rss");
        } */
        return v;
    }

    @Override
    public void processFinish(LinkedList<Item> output) {
        recyclerView.setAdapter(new FeedListAdapter(getContext(), output));
        adapter.notifyDataSetChanged();
    }

    private void fetchNews() {
        Log.d(TAG, "fetchNews()");
        // creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://newsapi.org/v2/top-headlines?sources=ansa,google-news-it,il-sole-24-ore&apiKey=e8e11922f51241959ab4a38de91061e5",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");

                            JSONArray jsonArticles = obj.getJSONArray("articles");

                            // source that will be used inside the loop
                            Source source;

                            // now looping through all the elements of the json array
                            for (int i = 0; i < jsonArticles.length(); i++) {
                                JSONObject jsonArticle = jsonArticles.getJSONObject(i);

                                source = new Source("bbc-news", "BBC News");

                                Article article = new Article(
                                        source,
                                        jsonArticle.getString("author"),
                                        jsonArticle.getString("title"),
                                        jsonArticle.getString("description"),
                                        jsonArticle.getString("url"),
                                        jsonArticle.getString("urlToImage"),
                                        jsonArticle.getString("publishedAt"),
                                        "undefined"
                                        );

                                // adding the article to the
                                newsList.add(article);
                            }

                            newsApiResponse = new NewsApiResponse(status, newsList);

                            adapter = new NewsListAdapter(getContext(), newsApiResponse);

                            // adding the adapter
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // displaying the error in a toast if occurs
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}
