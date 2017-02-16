package com.example.cutransit.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cutransit.BuildConfig;
import com.example.cutransit.R;
import com.example.cutransit.adapter.DepartureArrayAdapter;
import com.example.cutransit.model.DepartureInfo;
import com.example.cutransit.util.DataUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class StopDepartureActivity extends AppCompatActivity {
    private static final String LOG_TAG = StopDepartureActivity.class.getSimpleName();

    View rootView;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_departure);

        String stopName;
        String stopId;

        rootView = findViewById(R.id.activity_stop_departure);
        Intent intent = getIntent();
        stopName = intent.getStringExtra(MainActivity.INTENT_EXTRA_STOP_NAME);
        stopId = intent.getStringExtra(MainActivity.INTENT_EXTRA_STOP_ID);

        setTitle(stopName);

        DepartureArrayAdapter adapter = new DepartureArrayAdapter(this);
        listView = (ListView) rootView.findViewById(R.id.list_all_departures);
        listView.setAdapter(adapter);
//        listView.setEmptyView(rootView.findViewById(R.id.empty_list_all_departures));

        FetchStopDepartureData(stopId, adapter);
    }

    public void FetchStopDepartureData(String id, final ArrayAdapter<DepartureInfo> adapter) {
        final ArrayList<DepartureInfo> infos = new ArrayList<>();

        Uri uri = Uri.parse(DataUtils.QUEST_URL + "/" + DataUtils.PATH_GET_STOP_DEPARTURE).buildUpon()
                .appendQueryParameter(DataUtils.CUMTD_API_KEY_PARAM, BuildConfig.CUMTD_API_KEY)
                .appendQueryParameter(DataUtils.CUMTD_API_KEY_STOP_ID, id)
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
                Log.d(LOG_TAG, "Get departure data success");
                Log.d(LOG_TAG, new String(response));
                DataUtils.parseStopDepartureDataFromJson(infos, new String(response));
                adapter.clear();
                for (DepartureInfo info:infos) {
                    adapter.add(info);
                }

                // Delay the empty view binding to avoid flashing
                listView.setEmptyView(rootView.findViewById(R.id.empty_list_all_departures));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(LOG_TAG, "Get departure data failure");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

}
