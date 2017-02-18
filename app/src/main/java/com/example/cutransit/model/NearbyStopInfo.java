package com.example.cutransit.model;

/**
 * Created by JingJin on 2/16/17.
 */

public class NearbyStopInfo {

    public String stop_name;
    public double distance;
    public String id;

    public NearbyStopInfo(String stop_name, double distance, String id){
        this.stop_name = stop_name;
        this.distance = distance;
        this.id = id;
    }
}
