package com.example.cutransit.model;

/**
 * Created by JingJin on 2/16/17.
 */

public class NearbyStopInfo extends StopInfo{

    public double distance;

    public NearbyStopInfo(String stop_name, double distance, String id){
        super(stop_name, id, 0);
        this.distance = distance;
    }
}
