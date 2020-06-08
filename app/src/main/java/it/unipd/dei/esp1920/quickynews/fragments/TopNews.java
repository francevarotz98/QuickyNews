package it.unipd.dei.esp1920.quickynews.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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


import it.unipd.dei.esp1920.quickynews.R;
import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;
import it.unipd.dei.esp1920.quickynews.news.Article;
import it.unipd.dei.esp1920.quickynews.news.MyRepository;
import it.unipd.dei.esp1920.quickynews.news.NewsApiResponse;
import it.unipd.dei.esp1920.quickynews.news.NewsListAdapter;
import it.unipd.dei.esp1920.quickynews.news.Source;


public class TopNews extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = "Top News";
    private int fetchCount = 0;
    private static int onCreateViewCount = 0;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static MyRepository myRepository;

    private List<Article> newsList;
    private Context context;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        Log.d(TAG,"onCreate()");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView()");

        onCreateViewCount++;

        View v = inflater.inflate(R.layout.fragment_home,container,false);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        myRepository = new MyRepository(getActivity().getApplication());

        /*
        for(Article article : myRepository.getAllArticle()){
            if(Integer.parseInt(article.getStringPublishedAt())>20){
                Log.d(TAG,"data article = "+Integer.parseInt(article.getStringPublishedAt()));
                myRepository.deleteArticle(article.getUrl());

            }
        }

        */

        if(onCreateViewCount == 1) return v;
        else if(NetConnectionReceiver.isConnected(context)) {
            fetchCount = 0;
            newsList = new LinkedList<>();
            fetchNews();
        }
        else fetchNewsWithoutInternet();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach()");
        this.context = context;
        super.onAttach(context);
    }

    private void fetchNews() {
        Log.d(TAG, "fetchNews()");
        final SimpleDateFormat FORMATTER1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        final SimpleDateFormat FORMATTER2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", Locale.US);
        final SimpleDateFormat FORMATTER3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ", Locale.US);

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "https://newsapi.org/v2/top-headlines?" +
                "sources=bbc-sport,cnn,bbc-news,al-jazeera-english,techcrunch&pageSize=100&language=en&sortBy=date&apiKey=e8e11922f51241959ab4a38de91061e5",
                response -> {
                    try {
                        Log.d(TAG, "onResponse() for NewsApi");

                        JSONObject obj = new JSONObject(response);

                        JSONArray jsonArticles = obj.getJSONArray("articles");

                        Source source;

                        String cnnLiveLastTitle = "";

                        // looping through all the articles
                        for (int i = 0; i < jsonArticles.length(); i++) {
                            JSONObject jsonArticle = jsonArticles.getJSONObject(i);
                            JSONObject jsonSource = jsonArticle.getJSONObject("source");

                            source = new Source(jsonSource.getString("id"), jsonSource.getString("name"));

                            String urlToImage = jsonArticle.getString("urlToImage");
                            if(urlToImage.equals("null")) continue;

                            String description = jsonArticle.getString("description");
                            if(description.equals("null") || description.equals("")) continue;

                            String url = jsonArticle.getString("url");
                            if(source.getId().equals("cnn")) {
                                if (url.length() >= 17 && url.substring(0, 17).equals("http://cnn.it/go2")) continue;
                                else if (url.length() >= 19 && url.substring(0,19).equals("https://go.cnn.com/")) continue;
                            }

                            String title = jsonArticle.getString("title");
                            if(source.getId().equals("cnn") && url.split("/").length > 4 && url.split("/")[4].equals("live-news")) {
                                if(title.equals(cnnLiveLastTitle)) continue;
                                cnnLiveLastTitle = title;
                            }

                            String date = jsonArticle.getString("publishedAt");
                            Date articleDate = new Date();
                            if(date.substring(date.length()-1).equals("Z"))
                                date = date.substring(0, date.length()-1) + "+00:00";
                            try {
                                articleDate = FORMATTER1.parse(date.trim());
                            } catch (ParseException e) {
                                try {
                                    Date format2 = FORMATTER2.parse(date.trim());
                                    date = FORMATTER1.format(format2);
                                    articleDate = FORMATTER1.parse(date);
                                } catch (ParseException ex) {
                                    try {
                                        Date format3 = FORMATTER3.parse(date.trim());
                                        date = FORMATTER1.format(format3);
                                        articleDate = FORMATTER1.parse(date);
                                    } catch (ParseException exx) {
                                        exx.printStackTrace();
                                    }
                                }
                            }

                            // serve per decidere se l'articolo è stato pubblicato più di 1 giorno fa
                            Date today = new Date();
                            String now = FORMATTER1.format(today);
                            try {
                                Date fetchDate = FORMATTER1.parse(now);
                                long millis = Math.abs(fetchDate.getTime() - articleDate.getTime());
                                int hours = (int) (millis / 1000)  / 3600;
                                if(hours > 19) break;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Article article = new Article(
                                    source,
                                    jsonArticle.getString("author"),
                                    title,
                                    description,
                                    url,
                                    urlToImage,
                                    date,
                                    jsonArticle.getString("content")
                                    );

                            if(!(article.getDescription().contains(article.getTitle()) || date.equals("null"))) {
                                newsList.add(article);
                                //Log.d(TAG,"333 Added to db article with source = "+article.getSource().getId());
                                if(myRepository.getArticle(article.getUrl())!=null && myRepository.isFavoriteArticle(article.getUrl())) {
                                    myRepository.insertArticle(article);
                                    myRepository.setFavorite(article.getUrl(), true);
                                }
                                else
                                    myRepository.insertArticle(article);

                                if(source.getId().equals("techcrunch"))
                                    myRepository.setArticleCategory(url,"technology");
                                else if(source.getId().equals("bbc-sport"))
                                    myRepository.setArticleCategory(url,"sport");

                                myRepository.setId(article.getUrl(),article.getSource().getId());
                            }
                        }
                        Log.d(TAG,"fetchCount = " + fetchCount);
                        fetchCount++;
                        if(fetchCount == 4) {
                            insertionSort(newsList);
                            recyclerView.setAdapter(new NewsListAdapter(new NewsApiResponse(newsList)));
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
                        Toast.makeText(context, "NewsApi server error", Toast.LENGTH_SHORT).show();
                    }
                });

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET,
                "https://api.nytimes.com/svc/topstories/v2/world.json?api-key=7xa69Ge3sdFW8B0HOHaT03AMtEu72b21",
                response -> {
                    try {
                        Log.d(TAG, "onResponse() for NYTimes");

                        JSONObject obj = new JSONObject(response);

                        JSONArray jsonArticles = obj.getJSONArray("results");

                        Source source = new Source("nytimes", "The New York Times");

                        mainLoop: for (int i = 0; i < jsonArticles.length(); i++) {
                            JSONObject jsonArticle = jsonArticles.getJSONObject(i);

                            String url = jsonArticle.getString("url");
                            String[] path = url.split("/");
                            for(String p : path) {
                                if(p.equals("briefing")) continue mainLoop;
                            }

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

                            if(date.equals("null")) continue;

                            String description = jsonArticle.getString("abstract");

                            // serve per poter stampare correttamente le virgolette e gli apostrofi
                            title = new String(title.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                            description = new String(description.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                            // serve per decidere se l'articolo è stato pubblicato più di 1 giorno fa
                            Date today = new Date();
                            String now = FORMATTER1.format(today);
                            try {
                                Date articleDate = FORMATTER1.parse(date);
                                Date fetchDate = FORMATTER1.parse(now);
                                long millis = Math.abs(fetchDate.getTime() - articleDate.getTime());
                                int hours = (int) (millis / 1000)  / 3600;
                                if(hours > 19) continue;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // creo l'oggetto articolo
                            Article article = new Article(
                                    source,
                                    jsonArticle.getString("byline"),
                                    title,
                                    description,
                                    url,
                                    urlToImage,
                                    date,
                                    "No content"
                            );

                            // controllo che il link dell'immagine non sia nullo e che la descrizione non sia uguale al titolo, poi aggiungo l'articolo alla lista
                            if(!(urlToImage == null || article.getDescription().contains(article.getTitle()))){
                                newsList.add(article);
                                //Log.d(TAG,"222 Added to db article with source = "+article.getSource().getId());
                                if(myRepository.getArticle(article.getUrl())!=null && myRepository.isFavoriteArticle(article.getUrl())) {
                                    myRepository.insertArticle(article);
                                    myRepository.setFavorite(article.getUrl(), true);
                                }
                                else
                                    myRepository.insertArticle(article);
                            }
                        }
                        Log.d(TAG,"fetchCount = " + fetchCount);
                        fetchCount++;
                        if(fetchCount == 4) {
                            insertionSort(newsList);
                            recyclerView.setAdapter(new NewsListAdapter(new NewsApiResponse(newsList)));
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
                        Toast.makeText(context, "NYTimes server error", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

        StringRequest stringRequest3 = new StringRequest(Request.Method.GET,
                "https://api.nytimes.com/svc/topstories/v2/science.json?api-key=7xa69Ge3sdFW8B0HOHaT03AMtEu72b21",
                response -> {
                    try {
                        Log.d(TAG, "onResponse() for NYTimesScience");

                        JSONObject obj = new JSONObject(response);

                        JSONArray jsonArticles = obj.getJSONArray("results");

                        Source source = new Source("nytimes-science", "The New York Times");

                        mainLoop: for (int i = 0; i < jsonArticles.length(); i++) {
                            JSONObject jsonArticle = jsonArticles.getJSONObject(i);

                            String url = jsonArticle.getString("url");
                            String[] path = url.split("/");
                            for(String p : path) {
                                if(p.equals("briefing")) continue mainLoop;
                            }

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

                            if(date.equals("null")) continue;

                            String description = jsonArticle.getString("abstract");

                            // serve per poter stampare correttamente le virgolette e gli apostrofi
                            title = new String(title.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                            description = new String(description.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                            // serve per decidere se l'articolo è stato pubblicato più di 1 giorno fa
                            Date today = new Date();
                            String now = FORMATTER1.format(today);
                            try {
                                Date articleDate = FORMATTER1.parse(date);
                                Date fetchDate = FORMATTER1.parse(now);
                                long millis = Math.abs(fetchDate.getTime() - articleDate.getTime());
                                int hours = (int) (millis / 1000)  / 3600;
                                if(hours > 19) continue;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // creo l'oggetto articolo
                            Article article = new Article(
                                    source,
                                    jsonArticle.getString("byline"),
                                    title,
                                    description,
                                    url,
                                    urlToImage,
                                    date,
                                    "No content"
                            );

                            // controllo che il link dell'immagine non sia nullo e che la descrizione non sia uguale al titolo, poi aggiungo l'articolo alla lista
                            if(!(urlToImage == null || article.getDescription().contains(article.getTitle()))) {
                                newsList.add(article);
                              //  Log.d(TAG,"science Added to db article with source id = "+article.getSource().getId());
                                if(myRepository.getArticle(article.getUrl())!=null && myRepository.isFavoriteArticle(article.getUrl())) {
                                    myRepository.insertArticle(article);
                                    myRepository.setFavorite(article.getUrl(), true);
                                }
                                else
                                    myRepository.insertArticle(article);
                                myRepository.setArticleCategory(article.getUrl(),"science");
                                myRepository.setId(article.getUrl(),article.getSource().getId());

                            }
                        }
                        Log.d(TAG,"fetchCount = " + fetchCount);
                        fetchCount++;
                        if(fetchCount == 4) {
                            insertionSort(newsList);
                            recyclerView.setAdapter(new NewsListAdapter(new NewsApiResponse(newsList)));
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
                        Toast.makeText(context, "NYTimes server error", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

        StringRequest stringRequest4 = new StringRequest(Request.Method.GET,
                "https://api.nytimes.com/svc/topstories/v2/business.json?api-key=7xa69Ge3sdFW8B0HOHaT03AMtEu72b21",
                response -> {
                    try {
                        Log.d(TAG, "onResponse() for NYTimesBusiness");

                        JSONObject obj = new JSONObject(response);

                        JSONArray jsonArticles = obj.getJSONArray("results");

                        Source source = new Source("nytimes-business", "The New York Times - Business");

                        mainLoop: for (int i = 0; i < jsonArticles.length(); i++) {
                            JSONObject jsonArticle = jsonArticles.getJSONObject(i);

                            String url = jsonArticle.getString("url");
                            String[] path = url.split("/");
                            for(String p : path) {
                                if(p.equals("briefing")) continue mainLoop;
                            }

                            JSONArray jsonMultimedias;
                            try {
                                jsonMultimedias = jsonArticle.getJSONArray("multimedia");
                            } catch (org.json.JSONException e) {
                                continue;
                            }
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

                            if(date.equals("null")) continue;

                            String description = jsonArticle.getString("abstract");

                            // serve per poter stampare correttamente le virgolette e gli apostrofi
                            title = new String(title.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                            description = new String(description.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                            // serve per decidere se l'articolo è stato pubblicato più di 1 giorno fa
                            Date today = new Date();
                            String now = FORMATTER1.format(today);
                            try {
                                Date articleDate = FORMATTER1.parse(date);
                                Date fetchDate = FORMATTER1.parse(now);
                                long millis = Math.abs(fetchDate.getTime() - articleDate.getTime());
                                int hours = (int) (millis / 1000)  / 3600;
                                if(hours > 19) continue;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //cambio nome del source per problemi con layout (nome di prima troppo lungo)
                            Source newSource = new Source("nytimes-business", "The NYT-Business");

                            // creo l'oggetto articolo
                            Article article = new Article(
                                    newSource,
                                    jsonArticle.getString("byline"),
                                    title,
                                    description,
                                    url,
                                    urlToImage,
                                    date,
                                    "No content"
                            );

                            //Log.d(TAG,"qui, source="+article.getSource().getName());

                            // controllo che il link dell'immagine non sia nullo e che la descrizione non sia uguale al titolo, poi aggiungo l'articolo alla lista
                            if(!(urlToImage == null || article.getDescription().contains(article.getTitle()))) {
                                newsList.add(article);


                                if(myRepository.getArticle(article.getUrl())!=null && myRepository.isFavoriteArticle(article.getUrl())) {
                                    myRepository.insertArticle(article);
                                    myRepository.setFavorite(article.getUrl(), true);
                                }
                                else
                                    myRepository.insertArticle(article);
                                myRepository.setArticleCategory(article.getUrl(),"business");
                                myRepository.setId(article.getUrl(),article.getSource().getId());
                            }
                        }
                        Log.d(TAG,"fetchCount = " + fetchCount);
                        fetchCount++;
                        if(fetchCount == 4) {
                            insertionSort(newsList);
                            recyclerView.setAdapter(new NewsListAdapter(new NewsApiResponse(newsList)));
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
                        Toast.makeText(context, "NYTimes server error", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

        // creo la coda di richieste
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // aggiungo le richieste alla coda
        requestQueue.add(stringRequest1);
        requestQueue.add(stringRequest2);
        requestQueue.add(stringRequest3);
        requestQueue.add(stringRequest4);
        newsList.clear();
    }


    private void fetchNewsWithoutInternet(){
        Log.d(TAG,"fetchNewsWithoutInternet()");
        newsList = new LinkedList<>();
        List<Article>/*LiveData<List<Article>>*/ a= myRepository.getAllArticle();
        Log.d(TAG,"num of saved news = "+myRepository.countArticle());
        Log.d(TAG,"All favorites  = "+myRepository.getFavoritesArticle().size());

        for(Article article : a){
            newsList.add(article);
        }

        recyclerView.setAdapter(new NewsListAdapter(new NewsApiResponse(newsList)));

    }

    private static void insertionSort(List<Article> v) {
        Log.d(TAG, "insertionSort()");
        for (int i = 1; i < v.size(); i++) {
            Article temp = v.get(i);

            int j;

            int toRemove = -1;

            for (j = i; j > 0 && (temp.getPublishedAt().compareTo(v.get(j-1).getPublishedAt()) > 0 || temp.getTitle().equals(v.get(j-1).getTitle())); j--) {
                v.set(j,v.get(j-1));
                if(temp.getTitle().equals(v.get(j-1).getTitle())) toRemove = j;
            }
            v.set(j,temp);
            if(toRemove != -1) {
                v.remove(toRemove);
                i--;
            }
        }
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh()");
        if(NetConnectionReceiver.isConnected(context)) {
            swipeRefreshLayout.setRefreshing(true);
            fetchCount = 0;
            newsList = new LinkedList<>();
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

    public static MyRepository getRepository(){
        return myRepository;
    }



}
