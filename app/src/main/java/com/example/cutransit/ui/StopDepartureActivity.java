package com.example.cutransit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cutransit.R;
import com.example.cutransit.adapter.DepartureArrayAdapter;
import com.example.cutransit.model.DepartureInfo;
import com.example.cutransit.util.DataUtils;

import java.util.ArrayList;

public class StopDepartureActivity extends AppCompatActivity {
    private static final String LOG_TAG = StopDepartureActivity.class.getSimpleName();
    private String stopName;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_departure);

        rootView = findViewById(R.id.activity_stop_departure);
        Intent intent = getIntent();
        stopName = intent.getStringExtra(MainActivity.INTENT_EXTRA_STOP_ID);

        TextView stopNameTextView = (TextView) rootView.findViewById(R.id.stop_name);
        stopNameTextView.setText(stopName);

        ArrayList<DepartureInfo> infos = DataUtils.FetchStopDepartureData(stopName);
        Log.d(LOG_TAG, "Create new adapter");
        DepartureArrayAdapter adapter = new DepartureArrayAdapter(this, infos);
        adapter.notifyDataSetChanged();
        ListView listView = (ListView) rootView.findViewById(R.id.list_all_departures);
        listView.setAdapter(adapter);
    }


}
