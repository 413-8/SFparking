package com.example.ronald.sfparking;

import java.util.ArrayList;

/**
 * Created by Pedro on 4/21/2015.
 * karl comment, this object stores data about the location from the request made to SFPark
 * onOffStreet is if the parking is on or off of the street.
 * streetName is the street name.
 * rates is the pricing information for the off-street parking if available.
 */
public class ParkLocation {
    private String onOffStreet = "No Data";
    private String streetName = "No Data";
    private ArrayList<RateInfo> rates;

    public ParkLocation () {}

    public ParkLocation(String onOffStreet, String stName, ArrayList<RateInfo> rates) {
        this.onOffStreet = onOffStreet;
        this.streetName = stName;
        this.rates = rates;
    }

    /**
     *
     * @return the on/off street status of the parking for the ParkLocation object.
     */
    public String getOnOffStreet(){
        return onOffStreet;
    }

    /**
     *
     * @return the street name held by the ParkLocation object.
     */
    public String getStreetName(){
        return streetName;
    }

    /**
     *
     * @return the rates (costs) of parking for the ParkLocation object
     */
    public String getRates(){
        StringBuilder result = new StringBuilder();
        // Loop through elements.
        for (RateInfo obj : rates) {
            result.append(obj.toString());
        }
        return result.toString();
    }
}
