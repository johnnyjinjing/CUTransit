package com.example.cutransit.util;

import android.net.Uri;
import android.util.Log;

import com.example.cutransit.BuildConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

/**
 * Created by JingJin on 2/14/17.
 */

public class DataUtils {
    private static final String LOG_TAG = DataUtils.class.getSimpleName();

    private static final String BASE_URL = "https://developer.cumtd.com/api";
    private static final String VERSION = "v2.2";
    private static final String FORMAT = "json";
    private static final String QUEST_URL = BASE_URL + "/" + VERSION + "/" + FORMAT;

    private static final String PATH_GET_STOPS = "GetStops";
    private static final String API_KEY_PARAM = "key";


    public static void fetchStopsData() {
//        https://developer.cumtd.com/api/v2.2/json/GetStops?key=KEY

        Uri uri = Uri.parse(QUEST_URL + "/" + PATH_GET_STOPS).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.CUMTD_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "URL is: " + url);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url.toString(), new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d(LOG_TAG, "Get stop data success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

}
