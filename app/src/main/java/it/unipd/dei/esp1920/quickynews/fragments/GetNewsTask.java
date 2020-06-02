package it.unipd.dei.esp1920.quickynews.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class GetNewsTask extends AsyncTask<String, Void, ArrayList<View>> {
    private final static String TAG="GetNewsTask";

    private GetNewsTask.AsyncResponse delegate;
    private WeakReference<Context> weakContext;
    private static final ViewGroup.LayoutParams LAYOUT_PARAMS = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public interface AsyncResponse {
        void processFinish(ArrayList<View> output);
    }

    public GetNewsTask(Context context, AsyncResponse delegate){
        this.weakContext = new WeakReference<>(context);
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<View> doInBackground(String... params) {
        Log.d(TAG, "doInBackground()");
        String url = params[0];
        String source_id = params[1];
        try {
            ArrayList<View> returned;
            switch(source_id) {
                case "nytimes": returned = fetchNewYorkTimes(url);
                break;
                case "bbc-news": returned = fetchBbc(url);
                break;
                case "cnn": returned = fetchCnn(url);
                break;
                case "al-jazeera-english": returned = fetchAlJazeera(url); // TODO: Ancora da implementare
                break;
                case "bbc-sport": returned = fetchBbcSport(url); // TODO: Ancora da implementare
                break;
                default: returned = null;
            }
            return returned;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<View> result) {
        delegate.processFinish(result);
    }

    private ArrayList<View> fetchNewYorkTimes(String url) throws IOException {
        Log.d(TAG, "fetchNewYokTimes()");

        ArrayList<View> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        // article's title
        String text = doc.select("[data-test-id=headline]").first().text() + '\n';
        TextView textView = new TextView(weakContext.get());
        textView.setLayoutParams(LAYOUT_PARAMS);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(text);
        returned.add(textView);

        // article's description
        Element d = doc.getElementById("article-summary");
        if(d != null) {
            text = d.text() + '\n';
        }
        else if(doc.selectFirst("[class=css-nxfrgc evys1bk0]") != null) {
            text = doc.selectFirst("[class=css-nxfrgc evys1bk0]").text() +'\n';
        }
        else {
            text = doc.getElementsByClass("css-h99hf").first().text() + '\n';
        }
        textView = new TextView(weakContext.get());
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(text);
        returned.add(textView);

        // article's content
        Elements c = doc.select("[class=css-1fanzo5 StoryBodyCompanionColumn]");
        for(Element element : c) {
            Elements children = element.select("[class=css-53u6y8]").first().children();
            for(Element child : children) {
                if(child.tagName().equals("p")) {
                    text = child.text() + '\n';
                    textView = new TextView(weakContext.get());
                    textView.setText(text);
                    returned.add(textView);
                }
                else if(child.tagName().equals("h2") || child.tagName().equals("h3")) {
                    text = child.text() + '\n';
                    textView = new TextView(weakContext.get());
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setText(text);
                    returned.add(textView);
                }
                else if(child.tagName().equals("ul")) {
                    text = '\n' + child.text() + '\n';
                    textView = new TextView(weakContext.get());
                    textView.setText(text);
                    returned.add(textView);
                }
            }
        }
        return returned;
    }

    private ArrayList<View> fetchBbc(String url) throws IOException {
        Log.d(TAG, "fetchBbc()");

        ArrayList<View> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        // vari tipi di articolo
        Element t_article = doc.selectFirst("[class=story-body__h1]");
        Element t_video = doc.getElementsByClass("vxp-media__container").first();
        Element t_live = doc.getElementById("lx-commentary-top");

        String text = "";
        TextView textView = new TextView(weakContext.get());
        textView.setLayoutParams(LAYOUT_PARAMS);

        if(t_article != null) {
            // titolo
            text = t_article.text() + '\n';
            textView.setTypeface(null, Typeface.BOLD);
            textView.setText(text);
            returned.add(textView);

            // descrizione + contenuto
            Element dc = doc.getElementsByClass("story-body__inner").first();
            Elements dc_children = dc.children();

            for (Element element : dc_children) {
                if (element.className().equals("story-body__introduction") || element.tagName().equals("h2")) {
                    text = element.text() + '\n';
                    textView = new TextView(weakContext.get());
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setText(text);
                    returned.add(textView);
                }
                else if (element.tagName().equals("p")) {
                    text = element.text() + '\n';
                    textView = new TextView(weakContext.get());
                    textView.setText(text);
                    returned.add(textView);
                }
            }
        }
        else if (t_video != null) {
            Element t2e = t_video.getElementsByClass("vxp-column--single").first();
            for(Element child : t2e.children()) {
                if(child.className().equals("vxp-media__body")) {
                    for(Element grandChild : child.children()) {
                        switch(grandChild.className()) {
                            case "vxp-media__headline":
                                text = grandChild.text() + '\n';
                                textView = new TextView(weakContext.get());
                                textView.setTypeface(null, Typeface.BOLD);
                                textView.setText(text);
                                returned.add(textView);
                                break;
                            case "vxp-media__summary":
                                for (Element e : grandChild.children()) {
                                    if(e.tagName().equals("p")) {
                                        text = e.text() + '\n';
                                        textView = new TextView(weakContext.get());
                                        textView.setText(text);
                                        returned.add(textView);
                                    }
                                }
                                break;
                            default:
                        }
                    }
                }
            }
        }
        // TODO: se si vuole si può aggiungere l'ora in cui è stato pubblicato il signolo update (sopra il titolo)
        else if(t_live != null) {
            // titolo (di solito "Live reporting")
            Element header = t_live.getElementsByTag("header").first();
            text = header.getElementById("lx-commentary-title").text() + '\n';
            textView.setTypeface(null, Typeface.BOLD);
            textView.setText(text);
            returned.add(textView);
            // descrizione (di solito "Edited by" + nomi dei reporter)
            text = header.getElementsByClass("lx-commentary__meta").first().getElementsByClass("lx-commentary__meta-reporter gel-long-primer").first().text() + '\n';
            textView = new TextView(weakContext.get());
            textView.setText(text);
            returned.add(textView);
            // lista di tutti gli aggiornamenti live
            Elements updates = t_live.getElementsByClass("lx-commentary__stream").first().getElementsByTag("ol").first().getElementsByTag("li");
            for(Element update : updates) {
                Elements articles = update.getElementsByTag("article");
                for(Element article : articles) {
                    // titolo dell'aggiornamento
                    Elements spans = article.getElementsByTag("header").first().getElementsByTag("h3").first().getElementsByTag("span");
                    for(Element span : spans) {
                        text = span.text() + '\n';
                        textView = new TextView(weakContext.get());
                        textView.setTypeface(null, Typeface.BOLD);
                        textView.setText(text);
                        returned.add(textView);
                    }
                    // contenuto dell'aggiornamento
                    Element articleContent = article.getElementsByClass("gel-body-copy").first().getElementsByClass("lx-stream-post-body").first();
                    for (Element e : articleContent.children()) {
                        if(e.tagName().equals("p")) {
                            text = e.text() + '\n';
                            textView = new TextView(weakContext.get());
                            textView.setText(text);
                            returned.add(textView);
                        } else if(e.tagName().equals("ul") || e.tagName().equals("ol")) {
                            for(Element li : e.children()) {
                                if(li.tagName().equals("li")) {
                                    text = li.text() + '\n';
                                    textView = new TextView(weakContext.get());
                                    textView.setText(text);
                                    returned.add(textView);
                                }
                            }
                        }
                    }
                }
            }
        }
        return returned;
    }

    private ArrayList<View> fetchCnn(String url) throws IOException {
        Log.d(TAG, "fetchCnn()");

        // se l'url indirizza a una pagina della CNN contenente un video
        if(url.substring(0,25).equals("https://us.cnn.com/videos")) return fetchCnnVideo(url);

        // se l'url contiene aggiornamenti live
        if(url.split("/")[4].equals("live-news")) return fetchCnnLive(Jsoup.connect(url).get());

        ArrayList<View> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        String text = doc.getElementsByClass("pg-headline").tagName("h1").first().text();
        TextView textView = new TextView(weakContext.get());
        textView.setLayoutParams(LAYOUT_PARAMS);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(text + '\n');
        returned.add(textView);

        String author = "";
        String date = "";

        Element by = doc.getElementsByClass("metadata__info").tagName("div").first();
        if(by != null) {
            author = by.getElementsByClass("metadata__byline__author").tagName("span").first().text();
            date = by.getElementsByClass("update-time").tagName("p").first().text();
        }
        else {
            Element articleBody = doc.selectFirst("[itemprop=articleBody]");
            author = articleBody.getElementsByClass("metadata__byline__author").tagName("span").first().text();
            date = articleBody.getElementsByClass("update-time").tagName("p").first().text();
        }
        textView = new TextView(weakContext.get());
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(author + '\n' + date + '\n');
        returned.add(textView);

        Elements paragraphs = doc.getElementById("body-text").getElementsByClass("l-container").first().getElementsByClass("zn-body__paragraph");
        for(Element paragraph : paragraphs) {
            textView = new TextView(weakContext.get());
            if(paragraph.childrenSize() != 0 && paragraph.child(0).tagName().equals("h3")) textView.setTypeface(null, Typeface.BOLD);
            textView.setText(paragraph.text() + '\n');
            returned.add(textView);
        }
        return returned;
    }

    private ArrayList<View> fetchCnnLive(Document doc) throws IOException {
        Log.d(TAG, "fetchCnnLive()");

        ArrayList<View> returned = new ArrayList<>();

        Element title = doc.getElementsByClass("Text-sc-1amvtpj-0-h1-h1").tagName("h1").first();
        TextView textView = new TextView(weakContext.get());
        textView.setLayoutParams(LAYOUT_PARAMS);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(title.text() + '\n');
        returned.add(textView);

        String author = title.parent().select("[data-type=byline-area]").tagName("p").first().text();
        textView = new TextView(weakContext.get());
        textView.setText(author + '\n');
        returned.add(textView);

        // TODO Ho preso solamente l'ultimo aggiornamento pubblicato, ma se si vuole se ne possono prendere anche di più
        Elements lastUpdate_children = doc.selectFirst("[class=sc-cJSrbW poststyles__PostBox-sc-1egoi1-0 tzojb]").children();
        for(Element child : lastUpdate_children) {
            if(child.hasClass("render-stellar-contentstyles__Content-sc-9v7nwy-0")) {
                for(Element grandChild : child.children()) {
                    textView = new TextView(weakContext.get());
                    textView.setText(grandChild.text() + '\n');
                    returned.add(textView);
                }
                break;
            }
            else if(child.tagName().equals("header")) {
                for(Element grandChild : child.children()) {
                    textView = new TextView(weakContext.get());
                    if(grandChild.tagName().equals("h2")) textView.setTypeface(null, Typeface.BOLD);
                    textView.setText(grandChild.text());
                    returned.add(textView);
                }
            }
        }
        return returned;
    }

    private ArrayList<View> fetchCnnVideo(String url) throws IOException {
        Log.d(TAG, "fetchCnnVideo()");

        ArrayList<View> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        Element videoDescription = doc.selectFirst("[id^=leaf-video-]");

        Element headline = videoDescription.selectFirst("[id^=js-leaf-video_headline-]");
        TextView textView = new TextView(weakContext.get());
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(headline.text() + '\n');
        returned.add(textView);

        Element description = videoDescription.selectFirst("[id^=js-video_description-]");
        textView = new TextView(weakContext.get());
        textView.setText(description.text() + '\n');
        returned.add(textView);

        return returned;
    }

    private ArrayList<View> fetchAlJazeera(String url) throws IOException {
        Log.d(TAG, "fetchAlJazeera()");

        ArrayList<View> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        // titolo
        Element title = doc.getElementsByClass("post-title").tagName("h1").first();
        TextView textView = new TextView(weakContext.get());
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(title.text() + '\n');
        returned.add(textView);

        // descrizione
        String description = title.parent().getElementsByClass("article-heading-des").first().text();
        textView = new TextView(weakContext.get());
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(description + '\n');
        returned.add(textView);

        // contenuto
        Elements articleBody = doc.getElementById("main-article-block").getElementsByClass("article-p-wrapper").tagName("div").first().children();
        for(Element paragraph : articleBody) {
            if(paragraph.tagName().equals("p")) {
                textView = new TextView(weakContext.get());
                textView.setText(paragraph.text());
                returned.add(textView);
            }
            else if(paragraph.tagName().equals("h2")) {
                textView = new TextView(weakContext.get());
                textView.setTypeface(null, Typeface.BOLD);
                textView.setText('\n' + paragraph.text() + '\n');
                returned.add(textView);
            }
        }
        return returned;
    }

    private ArrayList<View> fetchBbcSport(String url) throws IOException {
        Log.d(TAG, "fetchBbcSport()");

        ArrayList<View> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        return returned;
    }
}