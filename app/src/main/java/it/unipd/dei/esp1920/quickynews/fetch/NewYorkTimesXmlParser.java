package it.unipd.dei.esp1920.quickynews.fetch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Locale;

import it.unipd.dei.esp1920.quickynews.news.Article;

public class NewYorkTimesXmlParser extends BaseFeedParser {
    private static String TAG="NewYorkTimesXmlParser";

    public NewYorkTimesXmlParser(String feedUrl) {
        super(feedUrl);
    }

    // safe alternative to nextText() method
    private String safeNextText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = parser.nextText();

        if (parser.getEventType() != XmlPullParser.END_TAG) {
            parser.nextTag();
        }
        return result;
    }

    public LinkedList<Item> parse() throws IOException {
        LinkedList<Item> items = null;
        Log.d(TAG,"inside NewYorkTimesXmlParser.parse()");
        XmlPullParser parser = Xml.newPullParser();
        InputStream stream = this.getInputStream();
        try {
            // auto-detect the encoding from the stream
            parser.setInput(stream, null);
            int eventType = parser.getEventType();
            Item currentItem = null;
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        items = new LinkedList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM)) {
                            currentItem = new Item();
                        }
                        else if (currentItem != null && parser.getPrefix() == null) {
                            if (name.equalsIgnoreCase(LINK)) {
                                currentItem.setLink(safeNextText(parser));
                            }
                            else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                currentItem.setDescription(safeNextText(parser));
                            }
                            else if (name.equalsIgnoreCase(PUB_DATE)) {
                                currentItem.setDate(safeNextText(parser));
                            }
                            else if (name.equalsIgnoreCase(TITLE)) {
                                currentItem.setTitle(safeNextText(parser));
                            }
                            else if (name.equalsIgnoreCase(CATEGORY)) {
                                currentItem.setCategory(safeNextText(parser));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM) && currentItem!= null) {
                            String itemIitle = currentItem.getTitle();
                            if(itemIitle == null)
                                currentItem.setTitle("No title");
                            String itemCategory = currentItem.getCategory();
                            if(itemCategory == null)
                                currentItem.setCategory("No category");
                            String itemDate = currentItem.getDate();
                               if(itemDate == null)
                                   currentItem.setDate("No date");
                            String itemDescription = currentItem.getDescription();
                            if(itemDescription == null)
                                    currentItem.setDescription("No description");
                            /* String itemLink = currentItem.getLink();
                            if(itemLink == null)
                                currentItem.setLink("No link"); */
                            items.add(currentItem);
                        }
                        else if (name.equalsIgnoreCase(CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (stream != null)
            stream.close();
        return items;
    }
}
