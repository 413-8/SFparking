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
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

    // map handlers
    private static GoogleMap theMap;
    private static ParkedDbAccessor parkedDbAccessor;
    private static SavedDbAccessor savedDbAccessor;
    /**
     * Stores the current instantiation of the location client in this object
     */
    private GoogleApiClient mGoogleApiClient; //
    private boolean mUpdatesRequested = false;
    /**
     * the LatLng object that keeps the latitude and longitude of the view center.
     */
    private LatLng latLngAtCameraCenter;
    private List<Address> addressesFromGeocoder;

    private ParkLocation parkLoc;
    private String sfparkQueryUrl;
    private final String radius = "0.010"; // approx. 53ft for accuracy (0.010 miles)
    private String streetName = "";
    private String onOffSt = "";
    private String rates = "";
    private String address = "";
    private String phone = "";
    private boolean isParked;
    private boolean isDataAtCenter;


    //Timer layout and reference variables
    private static LinearLayout timer_layout;
    private static TimerPicker timerPicker;
    private Intent countDownTimerIntent;


    // UI element reference variables
    private static CustomMapFragment customMapFragment;
    private static SlidingUpPanelLayout sliding_layout_container;
    private static ScrollView sliding_up_layout_scrollview;
    private static LinearLayout park_save_history_button_bar_layout;
    private static TextView park_data_text_view;
    private static TextView addressAtCenterPin;
    // private static LinearLayout hover_layout; //not needed unless we want to hide it sometimes
    private static ToggleButton parkButton;
    private static Button saveButton;
    private static Button historyButton;

    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // %%%%%%%%%%%%%%%%%%%%%%      ONCREATE     %%%%%%%%%%%%%%%%%%%%%%
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        customMapFragment = (CustomMapFragment) getSupportFragmentManager().findFragmentById(id.map);
        sliding_layout_container = (SlidingUpPanelLayout) findViewById(id.sliding_layout_container);
        sliding_up_layout_scrollview = (ScrollView) findViewById(id.sliding_up_layout_scrollview);
        park_save_history_button_bar_layout = (LinearLayout) findViewById(id.ParkSaveHist_Layout);
        park_data_text_view = (TextView) findViewById(id.park_data_text_view);
        parkButton = (ToggleButton) findViewById(id.park_button);
        saveButton = (Button) findViewById(id.save_button);
        historyButton = (Button) findViewById(id.history_button);
        // hover_layout = (LinearLayout) findViewById(id.hoverPin); // not needed unless we want to hide it sometimes


        //create timePicker
        timer_layout = (LinearLayout) findViewById(id.timer_layout);
        timerPicker = new TimerPicker(this);

        parkedDbAccessor = new ParkedDbAccessor(this);
        parkedDbAccessor.write();
        parkedDbAccessor.read();

        savedDbAccessor = new SavedDbAccessor(this);
        savedDbAccessor.write();
        savedDbAccessor.read();

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
            /*
      A request to connect to Location Services
     */
            LocationRequest mLocationRequest = LocationRequest.create();

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

    protected void onStart() {
        super.onStart();

    }

    /**
     * Builds the google API client to add the API to the app build.
     */
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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


            theMap = customMapFragment.getMap();
            theMap.setIndoorEnabled(false);


            // Enabling MyLocation in Google Map
            theMap.setMyLocationEnabled(true);
            if (mLastLocation != null) {
                latLong = new LatLng(mLastLocation
                        .getLatitude(), mLastLocation
                        .getLongitude());


            } else {
                latLong = new LatLng(37.751864, -122.445840);
            }
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15).build();

            theMap.setMyLocationEnabled(true);
            theMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Clears all the existing markers
            // mGoogleMap.clear();
            // latLngAtCameraCenter = theMap.getCameraPosition().target;

            customMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
                @Override
                public void onDrag(MotionEvent motionEvent) {
                    //Log.d("ON_DRAG", String.format("ME: %s", motionEvent));
                    // On ACTION_DOWN, switch to default sliding_layout_container
                    // On ACTION_UP, proceed to request and display SFPark data
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN: //ACTION_DOWN
                            //     setUpPanelDefault();
                            //     park_data_text_view.setVisibility(View.GONE);
                            //    addressAtCenterPin.setText(" Getting location ");
                            break;
                        case MotionEvent.ACTION_UP: //ACTION_UP
                            latLngAtCameraCenter = theMap.getCameraPosition().target;
                            queryAndDisplayGoogleData();
                            queryAndDisplaySfparkData();
                            sliding_up_layout_scrollview.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                }
            });


            /*theMap.setOnCameraChangeListener(new OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition arg0) {

                    latLngAtCameraCenter = theMap.getCameraPosition().target;
                    //  markerLayout.setVisibility(View.VISIBLE);

                    //setupPanelWithoutData();

                    // request and parse sfpark api
                    // display appropriate results to user


                }
            });*/

          /*  markerLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {

                        LatLng latLng1 = new LatLng(latLngAtCameraCenter.latitude,
                                latLngAtCameraCenter.longitude);


                        markerLayout.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }

                }
            }); */
            latLngAtCameraCenter = latLong;
            queryAndDisplayGoogleData();
            queryAndDisplaySfparkData();
            sliding_up_layout_scrollview.setVisibility(View.VISIBLE);
            parkedMarkerLoader();
            if (savedDbAccessor.isEmpty()) {
                historyButton.setEnabled(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up sliding_layout_container for a location that has no data form SFPark
     */
    private void setupPanelWithData() {
        parkButton.setEnabled(true);
        saveButton.setEnabled(true);
        int height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 145, getResources().getDisplayMetrics());
        int height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 195, getResources().getDisplayMetrics());
        sliding_up_layout_scrollview.getLayoutParams().height = height1;
        sliding_layout_container.setPanelHeight(height2);
        sliding_layout_container.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        sliding_layout_container.setTouchEnabled(false);
    }

    /**
     * Sets up sliding_layout_container for a location that contains data from SFPark
     */
    private void setupPanelWithoutData() {
        parkButton.setEnabled(true);
        saveButton.setEnabled(false);
        int height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        sliding_up_layout_scrollview.getLayoutParams().height = height1;
        sliding_layout_container.setPanelHeight(height2);
        sliding_layout_container.setTouchEnabled(false);
        sliding_layout_container.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    /**
     * Sets up the timer layout in a window with default values
     */
    private void setupPanelWithTimerPickerLayout() {
        int height1;
        if (isDataAtCenter) {
            height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 395, getResources().getDisplayMetrics());
        } else {
            height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        }
        int height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayHeight = size.y;
        int displayWidth = size.x;
        System.out.println("h " + displayHeight + " w " + displayWidth);
        System.out.println("h2 " + height2);
        //sliding_layout_container.setAnchorPoint(0.7f);
        //sliding_layout_container.setPanelHeight(height1);
        sliding_up_layout_scrollview.getLayoutParams().height = displayHeight - height2 - 50;
        park_save_history_button_bar_layout.setVisibility(LinearLayout.GONE);
        timer_layout.setVisibility(LinearLayout.VISIBLE);
        sliding_layout_container.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        sliding_layout_container.setTouchEnabled(false);

        timerPicker.defaultValues();

    }

    /**
     * retrieves data about the center location asynchronously
     */
    private void queryAndDisplayGoogleData() {
        try {
            new GetLocationAsync(latLngAtCameraCenter.latitude,
                    latLngAtCameraCenter.longitude)
                    .execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the information panel by sending a query to the
     * SFPark server.
     */
    private void queryAndDisplaySfparkData() {
        try {
            makeURLString(Double.toString(latLngAtCameraCenter.latitude),
                    Double.toString(latLngAtCameraCenter.longitude),
                    radius);
            parkLoc = new TheHttpRequest(getApplicationContext()).execute(sfparkQueryUrl).get();
            streetName = parkLoc.getName();
            if (streetName.equals("No Data")) {
                park_data_text_view.setText("\tNo Data");
                // Set up panel with No data
                setupPanelWithoutData();
                isDataAtCenter = false;
            } else {
                onOffSt = parkLoc.getOnOffStreet();
                rates = parkLoc.getRates();

                if (onOffSt.equals("Meter")) {
                    park_data_text_view.setText("\tStreet Name: "
                            + streetName + "\n\tType: "
                            + onOffSt
                            + "\n\tRates:\n"
                            + rates);
                } else {
                    address = parkLoc.getAddress();
                    phone = parkLoc.getPhone();
                    park_data_text_view.setText("\tName: " + streetName +
                            "\n\tType: " + onOffSt +
                            "\n\tAddress: " + address +
                            "\n\tPhone: " + phone +
                            "\n\tRates:\n" + rates);
                }
                // Set up panel when info is available
                setupPanelWithData();
                isDataAtCenter = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up sliding_layout_container for a default view
     */
    /*public void setUpPanelDefault() {
        parkButton.setEnabled(false);
        saveButton.setEnabled(false);
        sliding_up_layout_scrollview.setVisibility(View.GONE);
        sliding_layout_container.setPanelHeight(100);
        sliding_layout_container.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        sliding_layout_container.setTouchEnabled(false);
    }*/


    /**
     * If the user parked in the app and then closed it, this puts their parked location back on the
     * map when they re-open the app.
     */
    private void parkedMarkerLoader() {
        theMap.clear();
        isParked = !parkedDbAccessor.isEmpty();
        if (isParked) {
            LatLng parkedLatLng = new LatLng(parkedDbAccessor.getParkedLocation().getLatitude(),
                    parkedDbAccessor.getParkedLocation().getLongitude());
            theMap.addMarker(new MarkerOptions()
                            .position(parkedLatLng)
                            .title("I parked here")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            );
            parkButton.setText("Unpark");
            parkButton.setChecked(true);
        } else {
            isParked = false;
        }
    }


    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // %%%%%%%%%%%%%%%%%%%%%%  BUTTON HANDLERS  %%%%%%%%%%%%%%%%%%%%%%

    /**
     * Park Button.  creates a location object with the given latLngAtCameraCenter and stores it
     * into an SQLite database on the device.  Also adds a new marker on theMap after clearing any
     * other markers placed.
     */
    public void parkButton(View view) {
        theMap.clear();
        if (!isParked) {
            if (latLngAtCameraCenter != null) {

                setupPanelWithTimerPickerLayout();

                Calendar calendar = Calendar.getInstance();
                Date d = calendar.getTime();
                String str = d.toString();
                ParkLocationInfo parkLocationInfo = new ParkLocationInfo();
                parkLocationInfo.setLatitude(latLngAtCameraCenter.latitude);
                parkLocationInfo.setLongitude(latLngAtCameraCenter.longitude);
                parkLocationInfo.setOnOffStreet("On");
                parkLocationInfo.setStreetName(parkLoc.getName());
                parkLocationInfo.setTime(str);
                parkLocationInfo.setRates("");
                parkedDbAccessor.createLocationInfo(parkLocationInfo);

                theMap.addMarker(new MarkerOptions()
                                .position(latLngAtCameraCenter)
                                .title("I parked here")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );

                isParked = true;
                parkButton.setText("Unpark");
                parkButton.setChecked(true);
            } else {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Current Location Not Available!!!", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            theMap.clear();
            parkedDbAccessor.clear();
            isParked = false;
            parkButton.setText("Park");
            parkButton.setChecked(false);

            if (countDownTimerIntent != null) {
                Context context = getApplicationContext();
                stopService(countDownTimerIntent);
                countDownTimerIntent = null;
                Toast toast = Toast.makeText(context, "Timer Cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }

        }

    }

    /**
     * Hides the timer if the option to set it was cancelled
     *
     * @param view the current view.
     */
    public void cancelTimer(View view) {
        if (isDataAtCenter)
            setupPanelWithData();
        else
            setupPanelWithoutData();

        park_save_history_button_bar_layout.setVisibility(LinearLayout.VISIBLE);
        timer_layout.setVisibility(LinearLayout.GONE);

    }

    /**
     * Sets the timer to start in the background and removes the display of it from the view.
     *
     * @param view the view the timer is in.
     */
    public void setTimer(View view) {

        countDownTimerIntent = new Intent(this, ParkingTimer.class);
        countDownTimerIntent.putExtra("millis", timerPicker.getMilliseconds());

        startService(countDownTimerIntent);
        cancelTimer(view);
    }

    /**
     * Save Button
     * saves a location's data to the SQLite database on the device.
     */
    public void saveButton(View view) {
        //Location c = m;    // m is a data field in the MapsActivity class

        if (latLngAtCameraCenter != null) {
            Calendar calendar = Calendar.getInstance();
            Date d = calendar.getTime();
            String time = d.toString();
            ParkLocationInfo parkLocationInfo = new ParkLocationInfo();
            parkLocationInfo.setLatitude(latLngAtCameraCenter.latitude);
            parkLocationInfo.setLongitude(latLngAtCameraCenter.longitude);
            parkLocationInfo.setOnOffStreet(parkLoc.getOnOffStreet());
            parkLocationInfo.setStreetName(parkLoc.getName());
            parkLocationInfo.setTime(time);
            parkLocationInfo.setRates(parkLoc.getRates());
            savedDbAccessor.createLocationInfo(parkLocationInfo);
            Context context = getApplicationContext();
            CharSequence message = "Location Saved \uD83D\uDC4D";
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            //toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            historyButton.setEnabled(true);

        } else {
            Context context = getApplicationContext();
            CharSequence text = "Location Not Available";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    /**
     * History Button
     * starts the history table activity .SavedLocationsActivity
     */
    public void historyButton(View view) {

        startActivity(new Intent(".SavedLocationsActivity"));
    }

    /**
     * Allows the user to quickly zoom in or out for a more precise view an overview.
     *
     * @param view
     */
    public void zoomButton(View view) {
        float zoom = theMap.getCameraPosition().zoom;
        if (zoom == 18.0f) {
            theMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        } else
            theMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
    }

    /**
     * gives sfparkQueryUrl a query string from URLMaker using the given params
     *
     * @param latitude  the latitude to include in the string
     * @param longitude the longitude to include in the string
     * @param radius    the radius to include in the string.
     */
    private void makeURLString(String latitude, String longitude, String radius) {
        URLMaker temp = URLMaker.getInstance();
        sfparkQueryUrl = temp.makeURL(latitude, longitude, radius);
    }


    //empty inherited methods
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        setupMap();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //@Override
    /*public void onDisconnected() {

    }*/
    //end empty inherited methods

    /**
     * Uses a Geocoder object to get latitude and longitude of the current location asynchronously.
     */
    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;

        //constructor
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
         * In this case, it is retrieving addresses from a Geocoder object and getting more detailed
         * information about the location and returning it as a string..
         *
         * @param params the parameters of the asynchronous task.
         * @return null for now.
         */
        @Override
        protected String doInBackground(String... params) {

            try {
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.ENGLISH);
                addressesFromGeocoder = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (Geocoder.isPresent()) {
                    Address returnAddress = addressesFromGeocoder.get(0);

                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipCode = returnAddress.getPostalCode();

                    str.append(localityString).append("");
                    str.append(city).append("").append(region_code).append("");
                    str.append(zipCode).append("");

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
                        + "\n" + addressesFromGeocoder.get(0).getAddressLine(1) + " ");
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
         * @param values default
         */
        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}

