package com.example.cutransit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cutransit.R;

public class StopDepartureActivity extends AppCompatActivity {
    private String stopName;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_departure);

        rootView = findViewById(R.id.activity_stop_departure);
        Intent intent = getIntent();
        stopName = intent.getStringExtra(MainActivity.INTENT_EXTRA_STOP_NAME);

        TextView stopNameTextView = (TextView) rootView.findViewById(R.id.stop_name);
        stopNameTextView.setText(stopName);
    }


}
