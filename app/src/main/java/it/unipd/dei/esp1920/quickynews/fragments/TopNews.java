package it.unipd.dei.esp1920.quickynews.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


import it.unipd.dei.esp1920.quickynews.news.ArticleDao;
import it.unipd.dei.esp1920.quickynews.news.ArticleViewModel;
import it.unipd.dei.esp1920.quickynews.R;
import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;
import it.unipd.dei.esp1920.quickynews.news.Article;
import it.unipd.dei.esp1920.quickynews.news.MyRepository;
import it.unipd.dei.esp1920.quickynews.news.NewsApiResponse;
import it.unipd.dei.esp1920.quickynews.news.NewsListAdapter;
import it.unipd.dei.esp1920.quickynews.news.Source;


public class TopNews extends Fragment implements SwipeRefreshLayout.OnRefreshListener/* implements GetFeedTask.AsyncResponse */ {

    private final static String TAG="Top News";

    // private LinkedList<Item> feedList = new LinkedList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyRepository myRepository;
    private ArticleViewModel mArticleViewModel;
    private List<Article> newsList = new LinkedList<>();
    private String tmpStatus;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        Log.d(TAG,"onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView()");
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        //mArticleViewModel =  new ViewModelProvider().get(ArticleViewModel.class);
        myRepository = new MyRepository(getActivity().getApplication());

        /* if(NetConnectionReceiver.isConnected(getContext())) {
            Log.d(TAG,"GetFeedTask.execute()");
            new GetFeedTask(this).execute("https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml");
            // new GetFeedTask(this).execute("https://www.theguardian.com/international/rss");
        } */
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mArticleViewModel =  new ViewModelProvider(requireActivity()).get(ArticleViewModel.class);
        /*
        * TODO: 1) inserire notizie fetchate nel db;
        *       2) una volta fatto punto 1, "collegare" recycler view al db e non alle news fetchate (prima di
        *               tutto capendo come far funzionare linea 86 viewmodel, cioè creano un suo layout).
        * */

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
        if (NetConnectionReceiver.isConnected(getContext()))
            fetchNews();
        else{
            Toast.makeText(getContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
            fetchNewsWithoutInternet();
        }
    }

    /* @Override
    public void processFinish(LinkedList<Item> output) {
        recyclerView.setAdapter(new FeedListAdapter(getContext(), output));
        recyclerView.getAdapter().notifyDataSetChanged();
    } */

    private void fetchNews() {
        Log.d(TAG, "fetchNews()");

        newsList = new LinkedList<>();

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "https://newsapi.org/v2/top-headlines?" +
                "sources=cnn,bbc-news,al-jazeera-english&language=en&sortBy=date&apiKey=e8e11922f51241959ab4a38de91061e5",
                response -> {
                    try {
                        Log.d(TAG, "onResponse() for NewsApi");
                        boolean wasEmpty = true;
                        if(!newsList.isEmpty())
                            wasEmpty = false;

                        JSONObject obj = new JSONObject(response);

                        String status = obj.getString("status");
                        tmpStatus=status;

                        JSONArray jsonArticles = obj.getJSONArray("articles");

                        Source source;

                        // looping through all the articles
                        for (int i = 0; i < jsonArticles.length(); i++) {
                            JSONObject jsonArticle = jsonArticles.getJSONObject(i);

                            JSONObject jsonSource = jsonArticle.getJSONObject("source");

                            source = new Source(jsonSource.getString("id"), jsonSource.getString("name"));

                            String description = jsonArticle.getString("description");

                            String date = jsonArticle.getString("publishedAt");

                            if (description.equals("")) continue;

                            Article article = new Article(
                                    source,
                                    jsonArticle.getString("author"),
                                    jsonArticle.getString("title"),
                                    description,
                                    jsonArticle.getString("url"),
                                    jsonArticle.getString("urlToImage"),
                                    date,
                                    jsonArticle.getString("content")
                                    );

                            if(!(article.getUrlToImage().equals("null") ||
                                     article.getDescription().contains(article.getTitle()) || date.equals("null"))){
                                newsList.add(article);
                                Log.d(TAG,"Added to db article with title = "+article.getTitle());
                                myRepository.insertArticle(article);

                            }
                        }
                        if(!wasEmpty) {
                            insertionSort(newsList);
                            //Log.d(TAG,"entro nell'if, status = "+status);
                            recyclerView.setAdapter(new NewsListAdapter(new NewsApiResponse(status, newsList)));
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // displaying the error in a toast if occurs
                        Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                    }
                });

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET,
                "https://api.nytimes.com/svc/topstories/v2/world.json?api-key=7xa69Ge3sdFW8B0HOHaT03AMtEu72b21",
                response -> {
                    try {
                        Log.d(TAG, "onResponse() for NYTimes");
                        // final SimpleDateFormat NYT_FORMATTER = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
                        final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

                        boolean wasEmpty = true;
                        if(!newsList.isEmpty())
                            wasEmpty = false;

                        JSONObject obj = new JSONObject(response);

                        String status = obj.getString("status");

                        JSONArray jsonArticles = obj.getJSONArray("results");

                        Source source = new Source("nytimes", "The New York Times");

                        for (int i = 0; i < jsonArticles.length(); i++) {
                            JSONObject jsonArticle = jsonArticles.getJSONObject(i);

                            JSONArray jsonMultimedias = jsonArticle.getJSONArray("multimedia");
                            JSONObject jsonMultimedia;
                            String urlToImage = null;
                            if(jsonMultimedias.length() != 0) {
                                jsonMultimedia = jsonMultimedias.getJSONObject(0);
                                urlToImage = jsonMultimedia.getString("url");
                            }

                            // serve per decidere se l'articolo contiene aggiornamenti live,
                            // così da sapere quale data controllare ("updated_date" o "published_date")
                            String title = jsonArticle.getString("title");
                            String date;
                            if(title.contains("Live") && (title.contains("Updates") || title.contains("Live")))
                                date = jsonArticle.getString("updated_date");
                            else
                                date = jsonArticle.getString("published_date");

                            String description = jsonArticle.getString("abstract");

                            // serve per poter stampare correttamente le virgolette e gli apostrofi
                            title = new String(title.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                            description = new String(description.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                            // serve per decidere se l'articolo è stato pubblicato più di 1 giorno fa
                            Date today = new Date();
                            String now = FORMATTER.format(today);
                            try {
                                Date articleDate = FORMATTER.parse(date);
                                Date fetchDate = FORMATTER.parse(now);
                                long millis = Math.abs(fetchDate.getTime() - articleDate.getTime());
                                int hours = (int) (millis / 1000)  / 3600;
                                if(hours > 13) continue;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // creo l'oggetto articolo
                            Article article = new Article(
                                    source,
                                    jsonArticle.getString("byline"),
                                    title,
                                    description,
                                    jsonArticle.getString("url"),
                                    urlToImage,
                                    date,
                                    "No content"
                            );

                            // controllo che il link dell'immagine non sia nullo e che la descrizione non sia uguale al titolo, poi aggiungo l'articolo alla lista
                            if(!(urlToImage == null || article.getDescription().contains(article.getTitle()) || date.equals("null"))){
                                newsList.add(article);
                                Log.d(TAG,"Added to db article with title = "+article.getTitle());
                                myRepository.insertArticle(article);
                            }
                        }
                        // se la lista conteneva già articoli presi da NewsApi allora devo ordinare la lista in base alle date degli articoli
                        if(!wasEmpty) {
                            insertionSort(newsList);
                            recyclerView.setAdapter(new NewsListAdapter(new NewsApiResponse(status, newsList)));
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // displaying the error in a toast if occurs
                        Log.d(TAG,"onErrorResponse()");
                        Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

        // creo la coda di richieste
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // aggiungo le due richieste alla coda
        requestQueue.add(stringRequest1);
        requestQueue.add(stringRequest2);
        newsList.clear();
    }


    private void fetchNewsWithoutInternet(){
        Log.d(TAG,"fetchNewsWithoutInternet()");
        List<Article>/*LiveData<List<Article>>*/ a= myRepository.getAllArticle();
        Log.d(TAG,"num of saved news = "+myRepository.countArticle());
        for(Article article : a){
            newsList.add(article);
        }

        if(!newsList.isEmpty())
            insertionSort(newsList);
        recyclerView.setAdapter(new NewsListAdapter(new NewsApiResponse(tmpStatus, newsList)));
        tmpStatus="";

    }

    private static void insertionSort(List<Article> v) {
        for (int i = 1; i < v.size(); i++) {
            Article temp = v.get(i);

            int j;

            for (j = i; j > 0 && temp.getPublishedAt().compareTo(v.get(j-1).getPublishedAt()) >= 0; j--) {
                v.set(j,v.get(j-1));
            }
            v.set(j,temp);
        }
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh()");
        if(NetConnectionReceiver.isConnected(getContext())) {
            swipeRefreshLayout.setRefreshing(true);
            fetchNews();
        }
        else {
            Toast.makeText(getContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            fetchNewsWithoutInternet();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"onStop()");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d(TAG,"onDestroyView()");
    }



}
