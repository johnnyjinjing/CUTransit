package com.example.cutransit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.cutransit.R;
import com.example.cutransit.adapter.DepartureArrayAdapter;
import com.example.cutransit.util.DataUtils;

public class StopDepartureActivity extends AppCompatActivity {
    private static final String LOG_TAG = StopDepartureActivity.class.getSimpleName();

    View rootView;

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
        ListView listView = (ListView) rootView.findViewById(R.id.list_all_departures);
        listView.setAdapter(adapter);
        listView.setEmptyView(rootView.findViewById(R.id.empty_list_all_departures));

        DataUtils.FetchStopDepartureData(stopId, adapter);
    }


}
