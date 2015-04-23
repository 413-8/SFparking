package com.example.ronald.sfparking;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Pedro on 4/21/2015.
 * The parser class that will parse the response from the SFPark server.
 * it will search for NAME, TYPE, and RATES id tags and store information 
 *  parse returns a ParkLocation object, with relevent data fields filled if applicable.
 * readFeed takes an XMLparser as an input and if it finds an entry, it will call the readParkLocation method.
 * -readParkLocation looks for the tags for different information in the xml file and sets the data fields 
 * of the ParkLocation Object.  @return the ParkLocation object.
 */
public class SFParkXmlParser {

    //no namespaces
    private static final String ns = null;

    public static ParkLocation parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    // looks for AVL tag
    private static ParkLocation readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ParkLocation entry = new ParkLocation();

        parser.require(XmlPullParser.START_TAG, ns, "SFP_AVAILABILITY");
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // looks for the AVL tag
            if (name.equals("AVL")) {
                entry = readParkLocation(parser);
            } else {
                skip(parser);
            }
        }

        return entry;
    }

    // looks for TYPE, NAME and RATES tags
    private static ParkLocation readParkLocation(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "AVL");
        String onOffStreet = "";
        String stName = "";
        String rates = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("TYPE")) {
                onOffStreet = readType(parser);
            } else if (name.equals("NAME")) {
                stName = readName(parser);
            } else if (name.equals("RATES")) {
                //rates = readRates(parser);
            } else {
                skip(parser);
            }
        }
        return new ParkLocation(onOffStreet, stName, rates);
    }

    // process TYPE tags in the feed
    private static String readType(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "TYPE");
        String type = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "TYPE");
        return type;
    }

    // process TYPE tags in the feed
    private static String readName(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "NAME");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "NAME");
        return name;
    }

    // get TEXT from tags we need
    private static String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // skip tags we don't need
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
