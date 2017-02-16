package com.example.cutransit.util;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.cutransit.BuildConfig;
import com.example.cutransit.data.DataContract;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private static final String CUMTD_API_KEY_STOPS = "stops";
    private static final String CUMTD_API_KEY_STOP_ID = "stop_id";
    private static final String CUMTD_API_KEY_STOP_NAME = "stop_name";
    private static final String CUMTD_API_KEY_STOP_CODE = "code";
    private static final String CUMTD_API_KEY_STOP_DISTANCE = "distance";

    public static void fetchStopsData(final Context context) {
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
//                Log.d(LOG_TAG, new String(response));

                DataUtils.getStopsDataFromJson(new String(response), context);
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

    private static void getStopsDataFromJson(String s, Context context) {

        try {
            JSONObject jsonObject = new JSONObject(s);

            JSONArray jsonArray = jsonObject.getJSONArray(CUMTD_API_KEY_STOPS);

            // An array of values for bulkInsert
            int n = jsonArray.length();

            ContentValues[] cvArray = new ContentValues[n];

            // For each entry in the array, get essential info and create a ContentValue
            for (int i = 0; i < n; i++) {
                JSONObject stopJsonObject = jsonArray.getJSONObject(i);

                ContentValues cv = new ContentValues();
                cv.put(DataContract.StopEntry.COLUMN_ID, stopJsonObject.getString(CUMTD_API_KEY_STOP_ID));
                cv.put(DataContract.StopEntry.COLUMN_NAME, stopJsonObject.getString(CUMTD_API_KEY_STOP_NAME));
                cv.put(DataContract.StopEntry.COLUMN_CODE, stopJsonObject.getString(CUMTD_API_KEY_STOP_CODE));
                cv.put(DataContract.StopEntry.COLUMN_DISTANCE, stopJsonObject.getString(CUMTD_API_KEY_STOP_DISTANCE));
                cvArray[i] = cv;
                Log.d(LOG_TAG, stopJsonObject.getString(CUMTD_API_KEY_STOP_ID) + " " +
                        stopJsonObject.getString(CUMTD_API_KEY_STOP_NAME) + " " +
                        stopJsonObject.getString(CUMTD_API_KEY_STOP_CODE) + " " +
                        stopJsonObject.getString(CUMTD_API_KEY_STOP_DISTANCE));
            }

            // Insert into database
            if (cvArray.length > 0) {
                int m = context.getContentResolver().bulkInsert(DataContract.StopEntry.CONTENT_URI, cvArray);
                Log.d(LOG_TAG, "Insert " + m + " lines into the table");
            }
        } catch (JSONException e) {
            return;
        }
        return;
    }
}
