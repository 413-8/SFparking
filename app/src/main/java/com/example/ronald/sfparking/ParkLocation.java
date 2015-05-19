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
    private String name = "No Data";
    private ArrayList<RateInfo> rates;
    private String address = "";
    private String phone = "";

    /**
     * Default constructor
     */
    public ParkLocation () {}

    /**
     * Constructor for meters
     * @param onOffStreet Type of parking
     * @param stName Street name
     * @param rates Rates
     */
    public ParkLocation(String onOffStreet, String stName, ArrayList<RateInfo> rates) {
        this.onOffStreet = onOffStreet;
        this.name = stName;
        this.rates = rates;
    }

    /**
     * Constructor for garages
     * @param onOffStreet Type of parking
     * @param garageName Garage name
     * @param rates Rates
     * @param garageAddress Garage address
     * @param garagePhone Garage phone number
     */
    public ParkLocation(String onOffStreet, String garageName, ArrayList<RateInfo> rates,
                        String garageAddress, String garagePhone) {
        this.onOffStreet = onOffStreet;
        this.name = garageName;
        this.rates = rates;
        this.address = garageAddress;
        this.phone = garagePhone;
    }

    /**
     *
     * @return the on/off street status of the parking for the ParkLocation object.
     */
    public String getOnOffStreet(){
        String result = "";
        if(onOffStreet.equals("ON")) {
            result = "Meter";
        }
        else if (onOffStreet.equals("OFF")) {
            result = "Garage";
        }
        return result;
    }

    /**
     *
     * @return the name held by the ParkLocation object.
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return the address held by the ParkLocation object if it is a Garate
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @return the phone number held by the ParkLocation object if it is a Garage
     */
    public String getPhone() {
        return phone;
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
