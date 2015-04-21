package com.example.ronald.sfparking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
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

public class httpRequest extends AsyncTask{

    private Context context;

    public httpRequest(Context context){
        this.context = context;

    }


    // Check Internet connection
    private void checkInternetConnection(){
        ConnectivityManager check = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(check == null)
            Toast.makeText(context, "Not connected to internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try{
            String link = (String) params[0];
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.connect();

            InputStream in = conn.getInputStream();
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