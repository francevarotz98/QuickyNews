package it.unipd.dei.esp1920.quickynews.fetch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

public class GuardianXmlParser extends BaseFeedParser {

    private static String TAG="NewYorkTimesXmlParser";
    public GuardianXmlParser(String feedUrl) {
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

    // removes unwanted tags from a piece of information
    private static String removeTagsFrom(String text) {
        String returned = "";
        int start = 0; // represents the index of the current '<'
        int end = 0; // represents the index of the current '>'
        int nextStart = 0; // represents the index of the next '<'
        int count = 0;
        while(text.substring(end).contains("<") && text.substring(end).contains(">")) {
            start = text.indexOf("<", end);
            if(count==0 && start!=0)
                returned += text.substring(0,start);
            count++;
            end = text.indexOf(">", start);
            nextStart = text.indexOf("<", end);
            if(nextStart == -1)
                returned += text.substring(end+1);
            else
                returned += text.substring(end+1,nextStart);
        }
        if(start == 0 && end == 0)
            returned = text;
        return returned;
    }

    // searches for a specific tag and returns its content (without removing internal tags)
    private static String getContentOf(String tag, String text) {
        int start = 0; // represents the index of the current '<'
        int end = 0; // represents the index of the current '>'
        int returnedStart = -1;
        int returnedEnd = 0;
        while(text.substring(end).contains("<") && text.substring(end).contains(">")) {
            start = text.indexOf("<", end);
            end = text.indexOf(">", start);
            if(text.substring(start+1,end).equals(tag))
                returnedStart=end+1;
            else if(text.substring(start+1,end).equals("/"+tag) && returnedStart>-1) {
                returnedEnd = start;
                break;
            }
        }
        if (returnedStart == -1)
            return text;
        return returnedEnd + " " + text.substring(returnedStart,returnedEnd);
    }

    // searches for an element with specific start and end tags and returns its content (without removing internal tags)
    private static String getContentOf(String startTag, String endTag, String text) {
        int start = 0; // represents the index of the current '<'
        int end = 0; // represents the index of the current '>'
        int returnedStart = -1;
        int returnedEnd = 0;
        while(text.substring(end).contains("<") && text.substring(end).contains(">")) {
            start = text.indexOf("<", end);
            end = text.indexOf(">", start);
            if(text.substring(start+1,end).equals(startTag))
                returnedStart=end+1;
            else if(text.substring(start+1,end).equals(endTag) && returnedStart>-1) {
                returnedEnd = start;
                break;
            }
        }
        if (returnedStart == -1)
            return text;
        return returnedEnd + " " + text.substring(returnedStart,returnedEnd);
    }

    public LinkedList<Item> parse() throws IOException {
        LinkedList<Item> items = null;
        Log.d(TAG,"inside GuardianXmlParser.parse()");
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
                                String nextText = safeNextText(parser);
                                String currentLink = currentItem.getLink();
                                String[] splittedLink = currentLink.split("/");
                                boolean isLive = false;
                                for(int c=0; c<splittedLink.length;c++) {
                                    if(splittedLink[c].equals("live")) {
                                        isLive = true;
                                        break;
                                    }
                                }
                                if(isLive) {
                                    /* String description = "";
                                    nextText = getContentOf("ul", nextText).substring(getContentOf("ul", nextText).indexOf(" ") + 1);
                                    int start = 0;
                                    int end = 0;
                                    String temp = "";
                                    while(nextText.substring(end).contains("<li>")) {
                                        start = nextText.indexOf("<li>", end);
                                        end = nextText.indexOf("</li>", end);
                                        end += 5;
                                        temp = nextText.substring(start,end);
                                        description += removeTagsFrom(getContentOf("li", temp).substring(getContentOf("li", temp).indexOf(" ") + 1)) + '\n';
                                    }
                                    currentItem.setDescription(description); */
                                    String s = getContentOf("p class=\"block-time published-time\"", "/p", nextText);
                                    int returnedEnd = parseInt(s.substring(0, s.indexOf(" ")));
                                    s = getContentOf("p", nextText.substring(returnedEnd+4));
                                    returnedEnd = parseInt(s.substring(0, s.indexOf(" ")));
                                    String currentP = s.substring(s.indexOf(" ") + 1);
                                    /* while(currentP.contains("<a href=")) {
                                        s = getContentOf("p", nextText.substring(returnedEnd+4));
                                        returnedEnd = parseInt(s.substring(0, s.indexOf(" ")));
                                        currentP = s.substring(s.indexOf(" ") + 1);
                                    } */
                                    currentItem.setDescription("LATEST: " + removeTagsFrom(currentP));
                                }
                                else {
                                    currentItem.setDescription(removeTagsFrom(getContentOf("p", nextText).substring(getContentOf("p", nextText).indexOf(" ") + 1)));
                                }
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
