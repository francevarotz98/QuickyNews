package it.unipd.dei.esp1920.quickynews.news;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import it.unipd.dei.esp1920.quickynews.fragments.TopNews;
import it.unipd.dei.esp1920.quickynews.news.MyRepository;

public class GetNewsTask extends AsyncTask<String, Void, ArrayList<String>> {
    private final static String TAG="GetNewsTask";

    private GetNewsTask.AsyncResponse delegate;
    private WeakReference<Context> weakContext;
    private static final ViewGroup.LayoutParams LAYOUT_PARAMS = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private MyRepository myRepository= TopNews.getRepository();
    private String url;

    public interface AsyncResponse {
        void processFinish(ArrayList<String> output);
    }

    public GetNewsTask(Context context, AsyncResponse delegate){
        this.weakContext = new WeakReference<>(context);
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        Log.d(TAG, "doInBackground()");
        this.url = params[0];
        String source_id = params[1];
        try {
            ArrayList<String> returned;

            Log.d(TAG,"DEBUGGING: source_id="+source_id);

            switch(source_id) {
                case "nytimes-business":
                case "nytimes":
                case "nytimes-science":
                    returned = fetchNewYorkTimes(url);
                    break;
                case "bbc-news":
                    returned = fetchBbc(url);
                    break;
                case "cnn":
                    returned = fetchCnn(url);
                    break;
                case "al-jazeera-english":
                    returned = fetchAlJazeera(url);
                    break;
                case "bbc-sport":
                    returned = fetchBbc(url);
                    break;
                case "techcrunch":
                    returned = fetchTechcrunch(url);
                    break;
                default: returned = null;
            }
            return returned;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        delegate.processFinish(result);
       // myRepository.setPageHTML(this.url,result);
    }

    private ArrayList<String> fetchNewYorkTimes(String url) throws IOException {
        Log.d(TAG, "fetchNewYokTimes()");

        ArrayList<String> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        // article's title
        String text = doc.select("[data-test-id=headline]").first().text() + '\n';
        returned.add("BOLDDLOB" + text);

        // article's description
        Element d = doc.getElementById("article-summary");
        if(d != null) {
            text = d.text() + '\n';
        }
        else if(doc.selectFirst("[class=css-nxfrgc evys1bk0]") != null) {
            text = doc.selectFirst("[class=css-nxfrgc evys1bk0]").text() +'\n';
        }
        else if (!doc.getElementsByClass("css-h99hf").isEmpty()) {
            text = doc.getElementsByClass("css-h99hf").first().text() + '\n';
        }
        else {
            text = doc.getElementById("interactive-leadin").text() + '\n';
            returned.add("BOLDDLOB" + text);
            Elements paragraphs = doc.getElementsByClass("g-story").first().children();
            for(Element paragraph : paragraphs) {
                if(paragraph.tagName().equals("p")) {
                    returned.add(paragraph.text());
                }
                else if(paragraph.className().equals("g-asset g-table")) {
                    for(Element paragraph_child : paragraph.children()) {
                        if(paragraph_child.tagName().equals("h3") || paragraph_child.tagName().equals("h4")) returned.add("BOLDDLOB" + '\n' + paragraph_child.text());
                    }
                }
            }
            return returned;
        }
        returned.add("BOLDDLOB" + text);

        // article's content
        Elements c = doc.select("[class=css-1fanzo5 StoryBodyCompanionColumn]");
        for(Element element : c) {
            Elements children = element.select("[class=css-53u6y8]").first().children();
            for(Element child : children) {
                if(child.tagName().equals("p")) {
                    text = child.text() + '\n';
                    returned.add(text);
                }
                else if(child.tagName().equals("h2") || child.tagName().equals("h3")) {
                    text = child.text() + '\n';
                    returned.add("BOLDDLOB" + text);
                }
                else if(child.tagName().equals("ul")) {
                    text = '\n' + child.text() + '\n';
                    returned.add(text);
                }
            }
        }
        return returned;
    }

    private ArrayList<String> fetchBbc(String url) throws IOException {
        Log.d(TAG, "fetchBbc()");

        if(url.split("/")[3].equals("sport")) return fetchBbcSport(url);

        ArrayList<String> returned = new ArrayList<>();
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
            returned.add("BOLDDLOB" + text);

            // descrizione + contenuto
            Element dc = doc.getElementsByClass("story-body__inner").first();
            Elements dc_children = dc.children();

            for (Element element : dc_children) {
                if (element.className().equals("story-body__introduction") || element.tagName().equals("h2")) {
                    text = element.text() + '\n';
                    returned.add("BOLDDLOB" + text);
                }
                else if (element.tagName().equals("p")) {
                    text = element.text() + '\n';
                    returned.add(text);
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
                                returned.add("BOLDDLOB" + text);
                                break;
                            case "vxp-media__summary":
                                for (Element e : grandChild.children()) {
                                    if(e.tagName().equals("p")) {
                                        text = e.text() + '\n';
                                        returned.add(text);
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
            returned.add("BOLDDLOB" + text);

            // descrizione (di solito "Edited by" + nomi dei reporter)
            text = header.getElementsByClass("lx-commentary__meta").first().getElementsByClass("lx-commentary__meta-reporter gel-long-primer").first().text() + '\n';
            returned.add(text);

            // lista di tutti gli aggiornamenti live
            Elements updates = t_live.getElementsByClass("lx-commentary__stream").first().getElementsByTag("ol").first().getElementsByTag("li");
            for(Element update : updates) {
                Elements articles = update.getElementsByTag("article");

                for(Element article : articles) {
                    // titolo dell'aggiornamento
                    Elements spans = article.getElementsByTag("header").first().getElementsByTag("h3").first().getElementsByTag("span");
                    for(Element span : spans) {
                        text = span.text() + '\n';
                        returned.add("BOLDDLOB" + text);
                    }

                    // contenuto dell'aggiornamento
                    Element articleContent = article.getElementsByClass("gel-body-copy").first().getElementsByClass("lx-stream-post-body").first();
                    for (Element e : articleContent.children()) {
                        if(e.tagName().equals("p")) {
                            text = e.text() + '\n';
                            returned.add(text);
                        } else if(e.tagName().equals("ul") || e.tagName().equals("ol")) {
                            for(Element li : e.children()) {
                                if(li.tagName().equals("li")) {
                                    text = li.text() + '\n';
                                    returned.add(text);
                                }
                            }
                        }
                    }
                }
            }
        }
        return returned;
    }

    private ArrayList<String> fetchCnn(String url) throws IOException {
        Log.d(TAG, "fetchCnn()");

        // se l'url indirizza a una pagina della CNN contenente un video
        if(url.substring(0,25).equals("https://us.cnn.com/videos")) return fetchCnnVideo(url);

        // se l'url contiene aggiornamenti live
        if(url.split("/")[4].equals("live-news")) return fetchCnnLive(url);

        ArrayList<String> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        String text = doc.getElementsByClass("pg-headline").tagName("h1").first().text();
        returned.add("BOLDDLOB" + text);

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
        returned.add("BOLDDLOB" + author + '\n' + date);

        Elements paragraphs = doc.getElementById("body-text").getElementsByClass("l-container").first().getElementsByClass("zn-body__paragraph");
        for(Element paragraph : paragraphs) {
            text = paragraph.text();
            if(paragraph.childrenSize() != 0 && paragraph.child(0).tagName().equals("h3")) text = "BOLDDLOB" + text;
            returned.add(text);
        }
        return returned;
    }

    private ArrayList<String> fetchCnnLive(String url) throws IOException {
        Log.d(TAG, "fetchCnnLive()");

        ArrayList<String> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        Element title = doc.getElementsByClass("Text-sc-1amvtpj-0-h1-h1").tagName("h1").first();
        returned.add("BOLDDLOB" + title.text());

        String author = title.parent().select("[data-type=byline-area]").tagName("p").first().text();
        returned.add(author);

        // TODO Ho preso solamente l'ultimo aggiornamento pubblicato, ma se si vuole se ne possono prendere anche di più
        Element updates = doc.getElementById("posts");
        Elements lastUpdate_children = updates.getElementsByClass("sc-jqCOkK").tagName("article").first().children();
        for(Element child : lastUpdate_children) {
            if(child.hasClass("render-stellar-contentstyles__Content-sc-9v7nwy-0")) {
                for(Element grandChild : child.children()) {
                    returned.add(grandChild.text());
                }
                break;
            }
            else if(child.tagName().equals("header")) {
                String text = "";
                for(Element grandChild : child.children()) {
                    text = grandChild.text();
                    if(grandChild.tagName().equals("h2")) text = "BOLDDLOB" + text;
                    returned.add(text);
                }
            }
        }
        return returned;
    }

    private ArrayList<String> fetchCnnVideo(String url) throws IOException {
        Log.d(TAG, "fetchCnnVideo()");

        ArrayList<String> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        Element videoDescription = doc.selectFirst("[id^=leaf-video-]");

        Element headline = videoDescription.selectFirst("[id^=js-leaf-video_headline-]");
        returned.add("BOLDDLOB" + headline.text());

        Element description = videoDescription.selectFirst("[id^=js-video_description-]");
        returned.add(description.text());

        return returned;
    }

    private ArrayList<String> fetchAlJazeera(String url) throws IOException {
        Log.d(TAG, "fetchAlJazeera()");

        ArrayList<String> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        // titolo
        Element title = doc.getElementsByClass("post-title").tagName("h1").first();
        returned.add("BOLDDLOB" + title.text());

        // descrizione
        String description = title.parent().getElementsByClass("article-heading-des").first().text();
        returned.add("BOLDDLOB" + description);

        // contenuto
        Elements articleBody = doc.getElementById("main-article-block").getElementsByClass("article-p-wrapper").tagName("div").first().children();
        for(Element paragraph : articleBody) {
            if(paragraph.tagName().equals("p")) {
                returned.add(paragraph.text());
            }
            else if(paragraph.tagName().equals("h2")) {
                returned.add("BOLDDLOB" + '\n' + paragraph.text());
            }
        }
        return returned;
    }

    private ArrayList<String> fetchBbcSport(String url) throws IOException {
        Log.d(TAG, "fetchBbcSport()");

        ArrayList<String> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        Element article = doc.getElementById("responsive-story-page");
        if(article != null) {
            // titolo
            String text = article.getElementsByClass("story-headline").tagName("h1").first().text();
            returned.add("BOLDDLOB" + text);

            // autore
            text = article.getElementsByClass("gel-long-primer").tagName("p").first().text();
            returned.add(text);

            // contenuto
            Elements paragraphs = article.getElementById("story-body").children();
            for (Element paragraph : paragraphs) {
                if((!paragraph.tagName().equals("p")) || (paragraph.className().equals("sp-media-asset__smp-message"))) continue;
                text = paragraph.text();
                if (paragraph.className().equals("sp-story-body__introduction"))
                    text = "BOLDDLOB" + text;
                returned.add(text);
            }
        }
        else if (doc.getElementsByClass("qa-story-headline").tagName("h1").first() != null){
            // titolo
            String text = doc.getElementsByClass("qa-story-headline").tagName("h1").first().text();
            returned.add("BOLDDLOB" + text);

            // contenuto
            Elements paragraphs = doc.getElementsByClass("qa-story-body").tagName("div").first().children();
            for(Element paragraph : paragraphs) {
                if(paragraph.tagName().equals("p") || paragraph.tagName().equals("div")) {
                    text = paragraph.text();
                    if (paragraph.className().equals("qa-introduction"))
                        text = "BOLDDLOB" + text;
                    returned.add(text);
                }
                else if(paragraph.tagName().equals("h3")) {
                    returned.add("BOLDDLOB" + paragraph.text());
                }
            }
        }
        else {
            Element bodyWrapper = doc.getElementById("orb-modules");

            // titolo
            String text = doc.getElementsByClass("gel-trafalgar-bold").tagName("h1").first().text();
            returned.add("BOLDDLOB" + text);

            // contenuto
            Elements paragraphs = doc.getElementsByClass("gel-body-copy").tagName("div").first().child(0).children();
            for(Element paragraph : paragraphs) {
                if(paragraph.tagName().equals("p")) {
                    returned.add(paragraph.text());
                }
            }
        }
        return returned;
    }

    private ArrayList<String> fetchTechcrunch(String url) throws IOException {
        Log.d(TAG, "fetchTechcrunch()");

        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("GUCS","Ab_BzpUS");
        cookies.put("EuConsent","BO0iwqAO0iwqFAOABCITDKuAAAAt56__f_97_8_v2fdvduz_Ov_j_c__3XWcfPZvcELzhK9Meu_2wzd4u9wNRM5wckx87eJrEso5czISsG-RMod_zl__3zif9oxPowEc9rz3nZEw6vs2v-ZzBCGJ9I0g");
        cookies.put("GUC","AQABAQFe2_hfoEIdAARW&s=AQAAALjWSdjz&g=Xtq0Sg");
        cookies.put("A3","d=AQABBEC02l4CEE2iFzR8HFnqAEj3Oa5Cx48FEgABAQH4214pX-dVb2UB_iMAAAYsQVFBQkFRRmUyX2hmb0VJZEFBUlcmcz1BUUFBQUxqV1NkanomZz1YdHEwU2cHCDK02l7KNpEq&S=AQAAAt5Wooz1c53TiIYa6Yngvbw");
        cookies.put("BX","2l49mp9fdld1i&b=3&s=sq");
        cookies.put("A1","d=AQABBEC02l4CEE2iFzR8HFnqAEj3Oa5Cx48FEgABAQH4214pX-dVb2UB_iMAAAYsQVFBQkFRRmUyX2hmb0VJZEFBUlcmcz1BUUFBQUxqV1NkanomZz1YdHEwU2cHCDK02l7KNpEq&S=AQAAAt5Wooz1c53TiIYa6Yngvbw");
        cookies.put("A1S","d=AQABBEC02l4CEE2iFzR8HFnqAEj3Oa5Cx48FEgABAQH4214pX-dVb2UB_iMAAAYsQVFBQkFRRmUyX2hmb0VJZEFBUlcmcz1BUUFBQUxqV1NkanomZz1YdHEwU2cHCDK02l7KNpEq&S=AQAAAt5Wooz1c53TiIYa6Yngvbw");

        ArrayList<String> returned = new ArrayList<>();
        Document doc = Jsoup.connect(url).cookies(cookies).get();

        Element root = doc.getElementById("root");

        // titolo
        String text = root.getElementsByClass("article__title").tagName("h1").first().text();
        returned.add("BOLDDLOB" + text);

        // contenuto
        Elements paragraphs = root.getElementsByClass("article-content").first().children();
        for(Element paragraph : paragraphs) {
            if(paragraph.tagName().equals("p")) {
                returned.add(paragraph.text());
            }
            else if(paragraph.tagName().equals("h2")) {
                returned.add("BOLDDLOB" + paragraph.text());
            }
        }

        return returned;
    }
}