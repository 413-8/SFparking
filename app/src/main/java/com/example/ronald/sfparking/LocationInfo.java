package com.example.ronald.sfparking;

/**
 * Created by Dylan on 4/24/2015.
 * This class holds data about the current location.
 */
public class LocationInfo {
    private double longitude, latitude;
    private String time, street_name, on_off_street;
    private int id;

    public LocationInfo(){}

    public LocationInfo(int _id, double _latitude, double _longitude, String _on_off_street,  String _street_name, String _time){
        time = _time;
        street_name = _street_name;
        on_off_street = _on_off_street;
        latitude = _latitude;
        longitude = _longitude;
        id = _id;
    }

    /**
     *
     * @return id of the LocationInfo object.
     */
    public long getId(){return id;}

    /**
     *
     * @param id what the LocationInfo object's id will be set to.
     */
    public void setId(int id){this.id = id;}

    /**
     *
     * @return the value of time stored in the LocationInfo object.
     */
    public String getTime(){return time;}

    /**
     *
     * @return the street name of the LocationInfo object.
     */
    public String getStreetname(){return street_name;}

    /**
     *
     * @return the on/off street status of the LocationInfo object.
     */
    public String getOn_off_street(){return on_off_street;}

    /**
     *
     * @return the latitude of the LocationInfo object.
     */
    public Double getLatitude(){return latitude;}

    /**
     *
     * @return the longitude of the LocationInfo object.
     */
    public Double getLongitude(){return longitude;}

    /**
     *
     * @param time the new time of the LocationInfo object.
     */
    public void setTime(String time) {this.time = time;}

    /**
     *
     * @param street_name the new street name of the LocationInfo object.
     */
    public void setStreet_name(String street_name){this.street_name = street_name;}

    /**
     *
     * @param on_off_street the on/off street status of the LocationInfo object.
     */
    public void setOn_off_street(String on_off_street){this.on_off_street = on_off_street;}

    /**
     *
     * @param latitude the new latitude of the LocationInfo object.
     */
    public void setLatitude(double latitude){this.latitude = latitude;}

    /**
     *
     * @param longitude the new longitude of the LocationInfo object.
     */
    public void setLongitude(double longitude){this.longitude = longitude;}

}
