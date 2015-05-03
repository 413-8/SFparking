package com.example.ronald.sfparking;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

    static ArrayList<RateInfo> rates = new ArrayList<>();
    //no namespaces
    private static final String ns = null;

    /**
     * sets up the parser to be sent to readfeed method
     * @param in the input stream to be used for the parser
     * @return the return of the readfeed method.
     * @throws XmlPullParserException
     * @throws IOException
     */
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

    /**
     * looks for AVL tag in XML file.  If success, calls readParkLocation to fill out the ParkLocation
     * object entry.
     * @param parser the XmlPullParser to be used
     * @return the new ParkLocation that has been filled in by readParkLocation(parser).
     * @throws XmlPullParserException
     * @throws IOException
     */
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

    /**
     * looks for TYPE, NAME and RATES tags and then sets those fields in a new ParkLocation object.
     * @param parser the XmlPullParser to be used
     * @return A new location object with fields filled in by data from the xml file.
     * @throws XmlPullParserException if the parser fails.
     * @throws IOException if it can't read the file.
     */
    private static ParkLocation readParkLocation(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "AVL");
        String onOffStreet = "";
        String stName = "";
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
                rates = readRates(parser);
            } else {
                skip(parser);
            }
        }
        return new ParkLocation(onOffStreet, stName, rates);
    }

    /**
     * process TYPE tags in the feed.
     * @param parser the XmlPullParser to be used
     * @return the String tagged by TYPE.
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static String readType(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "TYPE");
        String type = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "TYPE");
        return type;
    }

    /**
     * process NAME tags in the feed
     * @param parser the XmlPullParser to be used
     * @return the String tagged by NAME.
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static String readName(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "NAME");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "NAME");
        return name;
    }

    /**
     * process RATES tags in the feed
     * @param parser the XmlPullParser to be used
     * @return an ArrayList of RateInfo objects
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static ArrayList<RateInfo> readRates(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        ArrayList<RateInfo> ratesTemp = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "RATES");
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // looks for the RS tag
            if (name.equals("RS")) {
                ratesTemp.add(readRatesInfo(parser));
            } else {
                skip(parser);
            }
        }
        return ratesTemp;
    }

    /**
     * looks for BEG, END, RATE and RQ tags
     * @param parser the XmlPullParser to be used
     * @return a RateInfo object
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static RateInfo readRatesInfo(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "RS");

        RateInfo temp = new RateInfo();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("BEG")) {
                temp.setBeg(readText(parser));
            } else if (name.equals("END")) {
                temp.setEnd(readText(parser));
            } else if (name.equals("RATE")) {
                temp.setRate(readText(parser));
            } else if (name.equals("RQ")) {
                temp.setRq(readText(parser));
            }else {
                skip(parser);
            }
        }
        return temp;
    }

    /**
     * gets TEXT from the current tag in the feed.
     * @param parser the XmlPullParser to be used
     * @return the String of text.
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**
     * skip unneeded tags.
     * @param parser the XmlPullParser to be used
     * @throws XmlPullParserException
     * @throws IOException
     */
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
