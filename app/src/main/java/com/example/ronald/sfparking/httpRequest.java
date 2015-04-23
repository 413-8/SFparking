package com.example.ronald.sfparking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/*
    makes an http request through the internet and reads the text on that page into a buffer.
*/
public class httpRequest extends AsyncTask{

    private Context context;
    InputStream in;

    public httpRequest(Context context){
        this.context = context;

    }


    // Check Internet connection
    private void checkInternetConnection(){
        ConnectivityManager check = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(check == null)
            Toast.makeText(context, "Not connected to internet", Toast.LENGTH_SHORT).show();
    }


    /*
     * the method accepts the URL to be used as its first paramter.
     * doInBackground opens an internet connection, 
     * reads the text on the resulting page on the url to a buffer.
     * after reading a line, it appends it to the String webPage, 
     * which will hold the text of the webpage
     * @return the String of text that is the webpage.
     */
    @Override
    protected Object doInBackground(Object[] params) {

        try{
            String link = (String) params[0];
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.connect();

            in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String data = null;
            String webPage = "";
            while( (data = reader.readLine()) != null){
                webPage += data + "\n";
            }
            return webPage;



        } catch (Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    protected void onPreExecute(){ checkInternetConnection();}

}
