package com.example.ronald.sfparking;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.example.ronald.sfparking.R.id;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// %%%%%%%%%%%%%%%%%%%%%%   MAPS ACTIVITY   %%%%%%%%%%%%%%%%%%%%%%
public class MapsActivity extends FragmentActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // A request to connect to Location Services
    private LocationRequest mLocationRequest;
    GoogleMap mGoogleMap;


    private SlidingUpPanelLayout slideUp_Layout;
    private LinearLayout noPin_layout;
    private LinearLayout hover_layout;
    private Park_LocationDataSource dataSource;
    private TextView dataTexview;



    public static String ShopLat;
    public static String ShopPlaceId;
    public static String ShopLong;
    // Stores the current instantiation of the location client in this object
    private FusedLocationProviderApi mLocationClient;
    public GoogleApiClient mGoogleApiClient;
    boolean mUpdatesRequested = false;
    private TextView markerText;
    private LatLng center;
    private LinearLayout markerLayout;
    private Geocoder geocoder;
    private List<Address> addresses;
    private TextView Address;


    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // %%%%%%%%%%%%%%%%%%%%%%      ONCREATE     %%%%%%%%%%%%%%%%%%%%%%
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        slideUp_Layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        noPin_layout = (LinearLayout) findViewById(R.id.layout_no_pin);
        dataTexview = (TextView) findViewById(R.id.data_textview);
        hover_layout = (LinearLayout) findViewById(id.hoverPin);

        dataSource = new Park_LocationDataSource(this);
        dataSource.write();
        dataSource.read();

        setUpPanelNoPin();
        //markerText = (TextView) findViewById(R.id.locationMarkertext);
        Address = (TextView) findViewById(id.addressText);
        markerLayout = (LinearLayout) findViewById(R.id.locationMarker);
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

    public void setUpPanel() {

        slideUp_Layout.setPanelHeight(150);
        slideUp_Layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        slideUp_Layout.setTouchEnabled(false);
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected GoogleApiClient getLocationApiClient(){
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void setupMap() {
        try {

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            LatLng latLong;
            mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // Enabling MyLocation in Google Map
            mGoogleMap.setMyLocationEnabled(true);
            if (mLastLocation != null) {
                latLong = new LatLng(mLastLocation
                        .getLatitude(), mLastLocation
                        .getLongitude());
                ShopLat = mLastLocation.getLatitude() + "";
                ShopLong = mLastLocation.getLongitude()
                        + "";


            } else {
                latLong = new LatLng(37.751864, -122.445840);
            }
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15).build();

            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Clears all the existing markers
            mGoogleMap.clear();

            mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition arg0) {
                    // TODO Auto-generated method stub
                    center = mGoogleMap.getCameraPosition().target;

                    // markerText.setText(" Set your Location ");
                    mGoogleMap.clear();
                    /*mGoogleMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                    .position(arg0.target)
                    );*/
                    markerLayout.setVisibility(View.VISIBLE);
                  //  dataTexview.setText("\tNo Data");


                    try {
                        new GetLocationAsync(center.latitude, center.longitude)
                                .execute();


                    } catch (Exception e) {
                    }
                }
            });

            markerLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    try {

                        LatLng latLng1 = new LatLng(center.latitude,
                                center.longitude);

                        /*Marker m = mGoogleMap.addMarker(new MarkerOptions()
                                .position(latLng1)
                               // .title(" Set your Location ")
                                .snippet("")
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        //    .fromResource(R.drawable.add_marker)));
                        m.setDraggable(true);*/

                        markerLayout.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Set up panel to Park here and History buttons. */
    public void setUpPanelNoPin(){
        //if(noPin_layout.getVisibility() == LinearLayout.GONE)
          //  noPin_layout.setVisibility(LinearLayout.VISIBLE);

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

    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // %%%%%%%%%%%%%%%%%%%%%%  BUTTON HANDLERS  %%%%%%%%%%%%%%%%%%%%%%
    /* Parked Button */
    public void parkButton(View view){

        if(center != null) {
            LocationInfo locationInfo = new LocationInfo();
            locationInfo.setLatitude(center.latitude);
            locationInfo.setLongitude(center.longitude);
            locationInfo.setOn_off_street("On");
            locationInfo.setStreet_name("Garfield");
            locationInfo.setTime("8:00");
            dataSource.createLocationInfo(locationInfo);


            mGoogleMap.addMarker(new MarkerOptions()
                            .position(center)
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
    public void historyButton(View view) {

        startActivity(new Intent(".SavedLocations"));
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

    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub

            x = latitude;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {
            Address.setText(" Getting location ");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(MapsActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (geocoder.isPresent()) {
                    Address returnAddress = addresses.get(0);

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

        @Override
        protected void onPostExecute(String result) {
            try {
                Address.setText(addresses.get(0).getAddressLine(0)
                        + addresses.get(0).getAddressLine(1) + " ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}

