package com.example.ronald.sfparking;

/**
 * Created by Ronald on 4/18/2015.
 */

import android.net.Uri;
//singleton design pattern.
public class URLMaker {

    private static URLMaker instance = new URLMaker();
    //constructor
    private URLMaker() {}

    /**
     * retrieves the instance of URLMaker
     * @return the instance of the URLMaker.
     */
    public static URLMaker getInstance(){
        return instance;
    }

    /**
     * Creates a URL string for the SFPark API
     * @param latitude the latitude of the location.
     * @param longitude the longitude of the location.
     * @param radius The radius of the search.
     * @return The URL string for the SFPark API.
     */
    public String makeURL (String latitude, String longitude, String radius) {

        String mile = "mile";
        String response = "XML";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.sfpark.org")
                .appendPath("sfpark")
                .appendPath("rest")
                .appendPath("availabilityservice")
                .appendQueryParameter("lat", latitude)
                .appendQueryParameter("long", longitude)
                .appendQueryParameter("radius", radius)
                .appendQueryParameter("uom", mile)
                .appendQueryParameter("pricing", "yes")
                .appendQueryParameter("response", response);
        String myUrl = builder.build().toString();

        return myUrl;
    }
}
