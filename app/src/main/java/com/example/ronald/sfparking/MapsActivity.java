package com.example.ronald.sfparking;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.location.LocationManager;
import android.location.Criteria;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * The main method of the application.
 */
public class MapsActivity extends FragmentActivity implements OnMapLongClickListener, OnMapClickListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker mymarker;
    private Location m = new Location("Marker");
    private ParkLocation parkLoc;
    private String url;

    private SlidingUpPanelLayout slideUp_Layout;
    private LinearLayout noPin_layout;
    private TextView dataTexview;
    private Park_LocationDataSource dataSource;

    String longitude;
    String latitude;
    String radius = "0.007"; // approx. 37ft for accuracy

    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        dataSource = new Park_LocationDataSource(this);
        dataSource.write();
        dataSource.read();
        slideUp_Layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        noPin_layout = (LinearLayout) findViewById(R.id.layout_no_pin);
        dataTexview = (TextView) findViewById(R.id.data_textview);
        setUpPanelNoPin();
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

        /* Center Map on Current Location */
        centerMapOnLocation(getMyLocation());

        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);
    }


    /* Set up panel to Park here and History buttons. */
    public void setUpPanelNoPin(){
        if(noPin_layout.getVisibility() == LinearLayout.GONE)
            noPin_layout.setVisibility(LinearLayout.VISIBLE);

        slideUp_Layout.setPanelHeight(150);
        slideUp_Layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        slideUp_Layout.setTouchEnabled(false);
    }

    /* Set up panel to show information where pin is dropped + Save Pin and Remove Pin buttons */
    public void setUpPanelPin(int size){
        noPin_layout.setVisibility(LinearLayout.GONE);
        slideUp_Layout.setPanelHeight(size);
        slideUp_Layout.setTouchEnabled(true);
        slideUp_Layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }


    /**
     * on a click-and-hold, a marker shall be placed on the map.
     * the device will then make a request from the SFPark server for information about
     * the location.
     * this information will appear in an info window above the location.
     * @param latLng the object holding data for latitude and longitude.
     */
    @Override
    public void onMapLongClick(LatLng latLng) {

        /*center map on dropped marker */
        m.setLatitude(latLng.latitude);
        m.setLongitude(latLng.longitude);
        centerMapOnLocation(m);


        /* removes previous marker if exists */
        if(mymarker != null)
            mymarker.remove();

        longitude = Double.toString(latLng.longitude);
        latitude = Double.toString(latLng.latitude);

        makeURLString(latitude, longitude, radius);

        String StreetName = "";
        String OnOffSt = "";
        String rates = "";

        /* request and parse sfpark api */
        try{
            parkLoc = new httpRequest(getApplicationContext()).execute(url).get();
            StreetName = parkLoc.getStreetName();
            OnOffSt = parkLoc.getOnOffStreet();
            rates = parkLoc.getRates();
        } catch (Exception e){e.printStackTrace();}


        /* add marker to the map: green if info available, red otherwise */
        if (StreetName.equals("No Data")) {
            mymarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng));

            dataTexview.setText("\tNo Data");


        /*Set up panel when pin is dropped with No data*/
            setUpPanelPin(160);
        }
        else {
            mymarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            dataTexview.setText("\tStreet Name: " + StreetName + "\n\tType: " + OnOffSt + " street." + "\n\tRates:\n" + rates);


        /*Set up panel when pin is dropped with data */
            setUpPanelPin(300);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(slideUp_Layout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)
            slideUp_Layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    /* onClick function for each button */

    /* Save Pin Button */
    public void savePinButton (View view){
        Location c = m;    // m is a data field in the MapsActivity class

        if(c != null) {
            LatLng l = new LatLng(c.getLatitude(), c.getLongitude());
            LocationInfo locationInfo = new LocationInfo();
            locationInfo.setLatitude(c.getLatitude());
            locationInfo.setLongitude(c.getLongitude());
            locationInfo.setOn_off_street(parkLoc.getOnOffStreet());
            locationInfo.setStreet_name(parkLoc.getStreetName());
            locationInfo.setTime("12:00");
            dataSource.createLocationInfo(locationInfo);


            mMap.addMarker(new MarkerOptions()
                            .position(l)
                            .title("I park here!!!")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            );
        } else{
            Context context = getApplicationContext();
            CharSequence text = "Current Location Not Available!!!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    /* Remove Pin Button */
    public void removePinButton(View view){
        mymarker.remove();
        setUpPanelNoPin();
    }

    /* I Parked Here Button */
    public void parkButton(View view){
        Location c = getMyLocation();

        if(c != null) {
            LatLng l = new LatLng(c.getLatitude(), c.getLongitude());
            LocationInfo locationInfo = new LocationInfo();
            locationInfo.setLatitude(c.getLatitude());
            locationInfo.setLongitude(c.getLongitude());
            locationInfo.setOn_off_street("On");
            locationInfo.setStreet_name("Garfield");
            locationInfo.setTime("8:00");
            dataSource.createLocationInfo(locationInfo);


            mMap.addMarker(new MarkerOptions()
                            .position(l)
                            .title("I park here!!!")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            );
        } else{
            Context context = getApplicationContext();
            CharSequence text = "Current Location Not Available!!!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    /* History Button */
    public void historyButton(View view){

        startActivity(new Intent(".SavedLocations"));
    }

    /**
     * Re-centers the google map view on the user's location.
     */
    public void centerMapOnLocation(Location location){

     if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
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

//    private Location getPinLocation(Location pin) {
//        String longitude = Double.toString(pin.getLongitude());
//        String latitude = Double.toString(pin.getLatitude());
//
//        makeURLString(latitude, longitude, radius);
//
//        String StreetName = "";
//        String OnOffSt = "";
//        String rates = "";
//
//        /* request and parse sfpark api */
//        try{
//            ParkLocation parkLoc = new httpRequest(getApplicationContext()).execute(url).get();
//            StreetName = parkLoc.stName;
//            OnOffSt = parkLoc.onOffStreet;
//            rates = parkLoc.rates;
//        } catch (Exception e){e.printStackTrace();}
//    }

    private void makeURLString(String latitude, String longitude, String radius) {
        URLMaker temp = URLMaker.getInstance();
        url = temp.makeURL(latitude, longitude, radius);
    }



}
