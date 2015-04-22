package com.example.ronald.sfparking;

import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.location.LocationManager;
import android.location.Criteria;
import android.content.Context;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


public class MapsActivity extends FragmentActivity implements OnMapLongClickListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Marker mymarker;

    String longitude;
    String latitude;
    String radius = "0.007"; // aprox. 37ft for accuracy

    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();


            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {


        mMap.setMyLocationEnabled(true);
        final UiSettings uiSettings = mMap.getUiSettings();

        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        /* setting up an invisible marker in order to remove it on the first call of onMapLongClick */
        mymarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0,0))
                .visible(false));

        mMap.setOnMapLongClickListener(this);


    //the i parked here button
    Button b1 = (Button) findViewById(R.id.park);

        /**
         * gets the location of the device and places a marker at that location once the button
         * is pressed.
         * If unable to get location, prints toast message to notify user.
         */
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Location c = getMyLocation();

                if(c != null) {
                    LatLng l = new LatLng(c.getLatitude(), c.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                         .position(l)
                         .title("I park here!!!")

                    );
                } else{
                    Context context = getApplicationContext();
                    CharSequence text = "Current Location Not Available!!!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });
        centerMapOnMyLocation();
    }

    /**
     * on a click-and-hold, a marker shall be placed on th map.
     * the device will then make a request from the SFPark server for information about
     * the location.
     * this information will appear in an info window above the location.
     * @param latLng the object holding data for latitude and longitude.
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        /* removes previous marker */
        if(mymarker.isVisible())
            mymarker.remove();

        longitude = Double.toString(latLng.longitude);
        latitude = Double.toString(latLng.latitude);


        URLMaker temp = URLMaker.getInstance();
        String url = temp.makeURL(latitude, longitude, radius);

        Log.d("mytag ", url);

        /* request sfpark api */
        AsyncTask task = new httpRequest(this).execute(url);
        InputStream stream = null;
        SFParkXmlParser sfparkParser = new SFParkXmlParser();
        ParkLocation parkLoc = null;

        try{
            response = task.get().toString();

        } catch (Exception e){e.printStackTrace();}



        String StreetName = "";
        String OnOffSt = "";


        stream = new ByteArrayInputStream(response.getBytes());
        try {

            parkLoc = sfparkParser.parse(stream);
            StreetName = parkLoc.stName;
            OnOffSt = parkLoc.onOffStreet;

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        mymarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(StreetName)
                .snippet("Street: " + OnOffSt));
    }



    /**
     * recenters the google map view on the user's location.
     */
    public void centerMapOnMyLocation(){

        Location currentLocation = getMyLocation();

       if (currentLocation != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }


    }

    /**
     * retrieves data about the user's location from the GPS provider on the device's phone.
     * if location wasn't found, check the next most accurate place for the current location.
     * Otherwise, try GoogleMaps Location.
     * @return the device's current location
     */
    private Location getMyLocation() {

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider = lm.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            myLocation = lm.getLastKnownLocation(provider);

            myLocation = mMap.getMyLocation();
        }

        return myLocation;
    }


}


