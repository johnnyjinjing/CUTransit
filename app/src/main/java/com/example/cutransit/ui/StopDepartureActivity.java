package com.example.cutransit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.cutransit.R;
import com.example.cutransit.adapter.DepartureArrayAdapter;
import com.example.cutransit.model.DepartureInfo;
import com.example.cutransit.util.DataUtils;

import java.util.ArrayList;

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

        ArrayList<DepartureInfo> infos = DataUtils.FetchStopDepartureData(stopId);
        Log.d(LOG_TAG, "Create new adapter");
        DepartureArrayAdapter adapter = new DepartureArrayAdapter(this, infos);
        ListView listView = (ListView) rootView.findViewById(R.id.list_all_departures);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}
