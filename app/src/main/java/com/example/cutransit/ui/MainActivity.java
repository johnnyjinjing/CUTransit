package com.example.cutransit.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.cutransit.R;
import com.example.cutransit.util.DataUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void fetchStopsData(View view) {
        DataUtils.fetchStopsData(this);
    }
}
