package com.example.cutransit.model;

/**
 * Created by JingJin on 2/18/17.
 */

public class StopInfo {
    public String stop_name;
    public String id;
    public int favorite = 0;

    public StopInfo(String stop_name, String id, int favorite){
        this.stop_name = stop_name;
        this.id = id;
        this.favorite = favorite;
    }
}
