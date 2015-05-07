package com.example.ronald.sfparking;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.ronald.sfparking.R.id;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// %%%%%%%%%%%%%%%%%%%%%%   MAPS ACTIVITY   %%%%%%%%%%%%%%%%%%%%%%

/**
 * The initial activity of the app.
 * displays a google map with information about the current location.
 */
public class MapsActivity extends FragmentActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // %%%%%%%%%%%%%%%%%%%%%%    DATA FIELDS    %%%%%%%%%%%%%%%%%%%%%%
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;
    //  GoogleMap mGoogleMap;
    GoogleMap map;
    CustomMapFragment customMapFragment;


    private SlidingUpPanelLayout sliding_layout_container;
    private LinearLayout sliding_up_layout;
    //  private LinearLayout hover_layout; //not needed unless we want to hide it sometimes
    private Park_LocationDataSource dataSource;
    private ParkLocation parkLoc;
    private TextView park_data_text_view;
    private String sfparkQueryUrl;
    String radius = "0.010"; // approx. 37ft for accuracy 0.007
    String streetName = "";
    String onOffSt = "";
    String rates = "";

    // Stores the current instantiation of the location client in this object
    private FusedLocationProviderApi mLocationClient;
    public GoogleApiClient mGoogleApiClient;
    boolean mUpdatesRequested = false;
    private TextView markerText;
    private LatLng latlngAtCameraCenter;
    //  private LinearLayout markerLayout;
    private Geocoder geocoder;
    private List<Address> addressesFromGeocoder;
    private TextView addressAtCenterPin;
    private Button saveButton;


    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // %%%%%%%%%%%%%%%%%%%%%%      ONCREATE     %%%%%%%%%%%%%%%%%%%%%%
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        customMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        sliding_layout_container = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout_container);
        sliding_up_layout = (LinearLayout) findViewById(R.id.sliding_up_layout);
        park_data_text_view = (TextView) findViewById(R.id.park_data_text_view);
        saveButton = (Button) findViewById(R.id.save_button);
        // hover_layout = (LinearLayout) findViewById(id.hoverPin); // not needed unless we want to hide it sometimes

        dataSource = new Park_LocationDataSource(this);
        dataSource.write();
        dataSource.read();

        setUpPanelDefault();
        //markerText = (TextView) findViewById(R.id.locationMarkertext);
        addressAtCenterPin = (TextView) findViewById(id.addressText);
        //    markerLayout = (LinearLayout) findViewById(R.id.locationMarker);
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            // Create a new global location parameters object
            mLocationRequest = LocationRequest.create();

            /*
             * Set the update interval
             */
            mLocationRequest.setInterval(10000);

            // Use high accuracy
            mLocationRequest
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            // Set the interval ceiling to one minute
            mLocationRequest
                    .setFastestInterval(60000);

            // Note that location updates are off until the user turns them on
            mUpdatesRequested = false;

            /*
             * Create a new location client, using the enclosing class to handle
             * callbacks.
             */
            buildGoogleApiClient();
            mGoogleApiClient.connect();

        }


    }

    /**
     * Builds the google API client to add the API to the app build.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * @return the GoogleApiClient object built for the app.
     */
    protected GoogleApiClient getLocationApiClient() {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Creates the google map to be used as the initial view.
     */
    private void setupMap() {
        try {

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            LatLng latLong;


            map = customMapFragment.getMap();


            //mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
            //        R.id.map)).getMap();

            // Enabling MyLocation in Google Map
            map.setMyLocationEnabled(true);
            if (mLastLocation != null) {
                latLong = new LatLng(mLastLocation
                        .getLatitude(), mLastLocation
                        .getLongitude());


            } else {
                latLong = new LatLng(37.751864, -122.445840);
            }
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15).build();

            map.setMyLocationEnabled(true);
            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Clears all the existing markers
            // mGoogleMap.clear();
            // latlngAtCameraCenter = map.getCameraPosition().target;

            customMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
                @Override
                public void onDrag(MotionEvent motionEvent) {
                    //Log.d("ON_DRAG", String.format("ME: %s", motionEvent));
                    // On ACTION_DOWN, switch to default sliding_layout_container
                    // On ACTION_UP, proceed to request and display SFPark data
                    switch (motionEvent.getAction()) {
                        // case 0: //ACTION_DOWN
                        //     setUpPanelDefault();
                        //     park_data_text_view.setVisibility(View.GONE);

                        case 1: //ACTION_UP
                            queryAndDisplayGoogleData();
                            queryAndDisplaySfparkData();
                            park_data_text_view.setVisibility(View.VISIBLE);
                    }
                }
            });


            map.setOnCameraChangeListener(new OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition arg0) {

                    latlngAtCameraCenter = map.getCameraPosition().target;
                    //  markerLayout.setVisibility(View.VISIBLE);

                    //setUpPanelWithoutData();

                    // request and parse sfpark api
                    // display appropirate results to user


                }
            });

          /*  markerLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {

                        LatLng latLng1 = new LatLng(latlngAtCameraCenter.latitude,
                                latlngAtCameraCenter.longitude);


                        markerLayout.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }

                }
            }); */
            latlngAtCameraCenter = latLong;
            queryAndDisplayGoogleData();
            setUpPanelDefault();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryAndDisplayGoogleData() {
        try {
            new GetLocationAsync(latlngAtCameraCenter.latitude,
                    latlngAtCameraCenter.longitude)
                    .execute();

        } catch (Exception e) {
        }
    }

    public void queryAndDisplaySfparkData() {
        try {
            makeURLString(Double.toString(latlngAtCameraCenter.latitude),
                    Double.toString(latlngAtCameraCenter.longitude),
                    radius);
            parkLoc = new httpRequest(getApplicationContext()).execute(sfparkQueryUrl).get();
            streetName = parkLoc.getStreetName();
            if (streetName.equals("No Data")) {
                park_data_text_view.setText("\tNo Data");
                // Set up panel when pin is dropped with No data
                setUpPanelWithoutData();
            } else {
                onOffSt = parkLoc.getOnOffStreet();
                rates = parkLoc.getRates();
                park_data_text_view.setText("\tStreet Name: "
                        + streetName + "\n\tType: "
                        + onOffSt
                        + " street."
                        + "\n\tRates:\n"
                        + rates);
                // Set up panel when pin is dropped with data
                setUpPanelWithData();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up sliding_layout_container for a default view
     */
    public void setUpPanelDefault() {

        park_data_text_view.setVisibility(View.GONE);
        sliding_layout_container.setPanelHeight(110);
        sliding_layout_container.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        sliding_layout_container.setTouchEnabled(false);
    }

    /**
     * Sets up sliding_layout_container for a location that has no data form SFPark
     */
    public void setUpPanelWithData() {
        //if(sliding_up_layout.getVisibility() == LinearLayout.GONE)
        //  sliding_up_layout.setVisibility(LinearLayout.VISIBLE);

        saveButton.setEnabled(true);
        sliding_layout_container.setPanelHeight(300);
        sliding_layout_container.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        sliding_layout_container.setTouchEnabled(true);
    }

    /**
     * Sets up sliding_layout_container for a location that contains data from SFPark
     */
    public void setUpPanelWithoutData() {
        saveButton.setEnabled(false);
        sliding_layout_container.setPanelHeight(190);
        sliding_layout_container.setTouchEnabled(true);
        sliding_layout_container.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // %%%%%%%%%%%%%%%%%%%%%%  BUTTON HANDLERS  %%%%%%%%%%%%%%%%%%%%%%
    ////////// Park Button
    public void parkButton(View view) {
        map.clear();
        if (latlngAtCameraCenter != null) {
            Calendar calendar = Calendar.getInstance();
            Date d = calendar.getTime();
            String str = d.toString();
            LocationInfo locationInfo = new LocationInfo();
            locationInfo.setLatitude(latlngAtCameraCenter.latitude);
            locationInfo.setLongitude(latlngAtCameraCenter.longitude);
            locationInfo.setOn_off_street("On");
            locationInfo.setStreet_name(parkLoc.getStreetName());
            locationInfo.setTime(str);
            dataSource.createLocationInfo(locationInfo);


            map.addMarker(new MarkerOptions()
                            .position(latlngAtCameraCenter)
                            .title("I parked here")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            );
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Current Location Not Available!!!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    ////////// Save Button
    public void saveButton(View view) {
        //Location c = m;    // m is a data field in the MapsActivity class

        if (latlngAtCameraCenter != null) {
            Calendar calendar = Calendar.getInstance();
            Date d = calendar.getTime();
            String str = d.toString();
            LocationInfo locationInfo = new LocationInfo();
            locationInfo.setLatitude(latlngAtCameraCenter.latitude);
            locationInfo.setLongitude(latlngAtCameraCenter.longitude);
            locationInfo.setOn_off_street(parkLoc.getOnOffStreet());
            locationInfo.setStreet_name(parkLoc.getStreetName());
            locationInfo.setTime(str);
            dataSource.createLocationInfo(locationInfo);
            Context context = getApplicationContext();
            CharSequence message = "Location Saved \uD83D\uDC4D";
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
            toast.show();

        } else {
            Context context = getApplicationContext();
            CharSequence text = "Location Not Available";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    ////////// History Button
    public void historyButton(View view) {

        startActivity(new Intent(".SavedLocations"));
    }

    private void makeURLString(String latitude, String longitude, String radius) {
        URLMaker temp = URLMaker.getInstance();
        sfparkQueryUrl = temp.makeURL(latitude, longitude, radius);
    }


    //empty inherited methods
    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // TODO Auto-generated method stub
        setupMap();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //@Override
    public void onDisconnected() {
        // TODO Auto-generated method stub

    }
    //end empty inherited methods

    /**
     * Uses a Geocoder object to get latitude and longitude of the current location asynchronously.
     */
    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;


        public GetLocationAsync(double latitude, double longitude) {
            x = latitude;
            y = longitude;
        }

        /**
         * invoked on the UI thread before the task is executed. This step is normally used to setup
         * the task, for instance by showing a progress bar in the user interface.
         */
        @Override
        protected void onPreExecute() {
            addressAtCenterPin.setText(" Getting location ");
        }

        /**
         * invoked on the background thread immediately after onPreExecute() finishes executing.
         * This step is used to perform background computation that can take a long time. The
         * parameters of the asynchronous task are passed to this step. The result of the
         * computation must be returned by this step and will be passed back to the last step. This
         * step can also use publishProgress(Progress...) to publish one or more units of progress.
         * These values are published on the UI thread, in the onProgressUpdate(Progress...) step.
         *
         * @param params the parameters of the asynchronous task.
         * @return null for now.
         */
        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(MapsActivity.this, Locale.ENGLISH);
                addressesFromGeocoder = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (geocoder.isPresent()) {
                    Address returnAddress = addressesFromGeocoder.get(0);

                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");

                } else {
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return null;

        }

        /**
         * invoked on the UI thread after the background computation finishes. The result of the
         * background computation is passed to this step as a parameter.
         *
         * @param result The result of the background computation.
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                addressAtCenterPin.setText(addressesFromGeocoder.get(0).getAddressLine(0)
                        + addressesFromGeocoder.get(0).getAddressLine(1) + " ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * invoked on the UI thread after a call to publishProgress(Progress...). The timing of the
         * execution is undefined. This method is used to display any form of progress in the user
         * interface while the background computation is still executing. For instance, it can be
         * used to animate a progress bar or show logs in a text field.
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}

