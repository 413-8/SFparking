package com.example.ronald.sfparking;

import java.util.ArrayList;

/**
 * Created by Dylan on 4/24/2015.
 * This class holds data about the current location.
 */
public class ParkLocationInfo {
    private double longitude, latitude;
    private String time;
    private String streetName;
    private String onOffStreet;
    private String rates;
    private int id;

    public ParkLocationInfo(){}

    public ParkLocationInfo(int _id, double _latitude, double _longitude, String _on_off_street, String _street_name, String _time, String _rates){
        time = _time;
        streetName = _street_name;
        onOffStreet = _on_off_street;
        latitude = _latitude;
        longitude = _longitude;
        rates = _rates;
        id = _id;
    }

    /**
     * Returns the id of the current ParkLocationInfo object.
     * @return id of the ParkLocationInfo object.
     */
    public long getId(){return id;}

    /**
     * Returns the time stored in the current ParkLocationInfo object.
     * @return the value of time stored in the ParkLocationInfo object.
     */
    public String getTime(){return time;}

    /**
     * Returns the street name of the current ParkLocationInfo object.
     * @return the street name of the ParkLocationInfo object.
     */
    public String getStreetName(){return streetName;}

    /**
     * Returns the on/off street status of the current ParkLocationInfo object.
     * @return the on/off street status of the ParkLocationInfo object.
     */
    public String getOnOffStreet(){return onOffStreet;}

    /**
     * Returns the latitude of the current ParkLocationInfo object.
     * @return the latitude of the ParkLocationInfo object.
     */
    public Double getLatitude(){return latitude;}

    /**
     * Returns the longitude of the current ParkLocationInfo object.
     * @return the longitude of the ParkLocationInfo object.
     */
    public Double getLongitude(){return longitude;}

    /**
     * Returns the rates of the current ParkLocationInfo object.
     * @return a formatted string of rates and times
     */
    public String getRates(){
        return rates;
    }

    /**
     * Sets the id of the current ParkLocationInfo object to the id passed in the method call.
     * @param newId what the ParkLocationInfo object's id will be set to.
     */
    public void setId(int newId){this.id = newId;}

    /**
     * Sets the time of the current ParkLocationInfo object to the time passed in the method call.
     * @param newTime the new time of the ParkLocationInfo object.
     */
    public void setTime(String newTime) {this.time = newTime;}

    /**
     * Sets the street name of the current ParkLocationInfo object to the street name passed in the
     * method call.
     * @param newStreetName the new street name of the ParkLocationInfo object.
     */
    public void setStreetName(String newStreetName){this.streetName = newStreetName;}

    /**
     * Sets the on/off street status of the current ParkLocationInfo object to the on/off street status
     * passed in the method call.
     * @param newOnOffStreet the on/off street status of the ParkLocationInfo object.
     */
    public void setOnOffStreet(String newOnOffStreet){this.onOffStreet = newOnOffStreet;}

    /**
     * Sets the latitude of the current ParkLocationInfo object to the latitude passed in the
     * method call.
     * @param newLatitude the new latitude of the ParkLocationInfo object.
     */
    public void setLatitude(double newLatitude){this.latitude = newLatitude;}

    /**
     * Sets the longitude of the current ParkLocationInfo object to the longitude passed in the
     * method call.
     * @param newLongitude the new longitude of the ParkLocationInfo object.
     */
    public void setLongitude(double newLongitude){this.longitude = newLongitude;}

    /**
     * Sets the rates of the current ParkLocationInfo object to the rates passed in the method call.
     * @param newRates ArrayList of new RateInfo objects (contain rates and time intervals)
     */
    public void setRates(String newRates) {this.rates = newRates;}

}
