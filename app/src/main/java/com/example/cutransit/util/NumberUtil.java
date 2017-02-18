package com.example.cutransit.util;

import java.text.DecimalFormat;

/**
 * Created by JingJin on 2/16/17.
 */

public class NumberUtil {

    private static final double FEET_PER_MILE = 5280.0;
    private static final double FEET_UPPER_BOUND = FEET_PER_MILE / 10.0;


    public static String feetToMileDisplay (double ft) {
        if (ft < FEET_UPPER_BOUND) {
            return new DecimalFormat("#").format(ft) + " ft";
        }
        else {
            return new DecimalFormat("#0.00").format(ft / FEET_PER_MILE) + " mi";
        }
    }
}
