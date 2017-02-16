package com.example.cutransit.model;

/**
 * Created by JingJin on 2/15/17.
 */

public class DepartureInfo {
    public String headSign;
    public String routeColor;
    public String expectedMins;

    public DepartureInfo(String headSign, String routeColor, String expectedMins){
        this.headSign = headSign;
        this.routeColor = routeColor;
        this.expectedMins = expectedMins;
    }
}
