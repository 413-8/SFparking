package com.example.ronald.sfparking;

/**
 * Created by Dylan on 4/24/2015.
 * This class holds data about the current location.
 */
public class LocationInfo {
    private double longitude, latitude;
    private String time, street_name, on_off_street;
    private long id;

    public LocationInfo(){}

    public LocationInfo(int _id, double _latitude, double _longitude, String _on_off_street,  String _street_name, String _time){
        time = _time;
        street_name = _street_name;
        on_off_street = _on_off_street;
        latitude = _latitude;
        longitude = _longitude;
        id = _id;
    }
    public long getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getTime(){return time;}
    public String getStreetname(){return street_name;}
    public String getOn_off_street(){return on_off_street;}
    public Double getLatitude(){return latitude;}
    public Double getLongitude(){return longitude;}

    public void setTime() {this.time = time;}
    public void setStreet_name(){this.street_name = street_name;}
    public void setOn_off_street(){this.on_off_street = on_off_street;}
    public void setLatitude(){this.latitude = latitude;}
    public void setLongitude(){this.longitude = longitude;}

}
