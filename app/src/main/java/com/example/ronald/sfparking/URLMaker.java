package com.example.ronald.sfparking;

/**
 * Created by Ronald on 4/18/2015.
 */

import android.net.Uri;
public class URLMaker {

    private static URLMaker instance = new URLMaker();
    private URLMaker() {}

    public static URLMaker getInstance(){
        return instance;
    }

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
                .appendQueryParameter("response", response);
        String myUrl = builder.build().toString();

        return myUrl;
    }
}
