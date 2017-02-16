package com.example.cutransit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.cutransit.R;
import com.example.cutransit.util.DataUtils;

public class MainActivity extends AppCompatActivity implements AllStopsFragment.Callback{

    static final String INTENT_EXTRA_STOP_ID = "id";
    static final String INTENT_EXTRA_STOP_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void fetchStopsData(View view) {
        DataUtils.fetchStopsData(this);
    }


    @Override
    public void onItemSelected(String id, String name) {
        Intent intent = new Intent(this, StopDepartureActivity.class)
                .putExtra(INTENT_EXTRA_STOP_ID, id)
                .putExtra(INTENT_EXTRA_STOP_NAME, name);
        startActivity(intent);
    }
}
