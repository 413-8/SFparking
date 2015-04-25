package com.example.ronald.sfparking;

/**
 * Created by Dylan on 4/24/2015.
 */
public class LocationInfo {
    private double longitude, latitude;
    private String time, street_name, on_off_street;
    private int id;

    public LocationInfo(){};

    public LocationInfo(int _id, double _latitude, double _longitude, String _on_off_street,  String _street_name, String _time){
        time = _time;
        street_name = _street_name;
        on_off_street = _on_off_street;
        latitude = _latitude;
        longitude = _longitude;
        id = _id;
    }
    public int getId(){return id;}
    public String getTime(){return time;}
    public String getStreetname(){return street_name;}
    public String getOn_off_street(){return on_off_street;}
    public Double getLatitude(){return latitude;}
    public Double getLongitude(){return longitude;}

}
