package com.example.cutransit.util;

import android.graphics.Color;

/**
 * Created by JingJin on 2/18/17.
 */

public class ColorUtils {

    public static int getContrastColor(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        double lum = (((0.299 * red) + ((0.587 * green) + (0.114 * blue))));
        return lum > 186 ? 0xFF000000 : 0xFFFFFFFF;
    }
}
