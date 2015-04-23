package com.example.ronald.sfparking;

/**
 * Created by Pedro on 4/21/2015.
 * karl comment, this object stores data about the location from the request made to SFPark
 * onOffStreet is if the parking is on or off of the street.
 * stname is the street name.
 * rates is the pricing information for the off-street parking if available.
 */
public class ParkLocation {
    public String onOffStreet = "No Data";
    public String stName = "No Data";
    public String rates;

    public ParkLocation () {}

    public ParkLocation(String onOffStreet, String stName, String rates) {
        this.onOffStreet = onOffStreet;
        this.stName = stName;
        this.rates = rates;
    }
}
