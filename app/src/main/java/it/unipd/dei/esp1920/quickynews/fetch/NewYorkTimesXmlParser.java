package it.unipd.dei.esp1920.quickynews.fetch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
//import java.util.ArrayList;
//import java.util.List;

import it.unipd.dei.esp1920.quickynews.connections.NetConnectionReceiver;


public class NewYorkTimesXmlParser extends BaseFeedParser {

    private static String TAG="NewYorkTimesXmlParser";
    public NewYorkTimesXmlParser(String feedUrl) {
        super(feedUrl);
    }

    private String safeNextText(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        String result = parser.nextText();
        if (parser.getEventType() != XmlPullParser.END_TAG) {
            parser.nextTag();
        }
        return result;
    }

    public LinkedList<Item> parse() throws IOException {
        LinkedList<Item> items = null;
        if(!NetConnectionReceiver.isConnected())
            return items; //problema: bisogna cliccare una volta su top news per vedere le info, se si è connessi
        Log.d(TAG,"parse()");
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
                        else if (currentItem != null) {
                            if (name.equalsIgnoreCase(LINK)) {
                                String text = safeNextText(parser);
                                if(text.length() != 0)
                                    currentItem.setLink(text);
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
                            /* String itemDate = currentItem.getDate();
                               if(itemDate == null)
                                currentItem.setDate("No date"); */
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
