package com.example.ronald.sfparking;

/**
 * Created by Pedro on 4/21/2015.
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
