package com.example.cutransit.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cutransit.R;
import com.example.cutransit.model.NearbyStopInfo;

/**
 * Created by JingJin on 2/16/17.
 */

public class NearbyStopArrayAdapter extends ArrayAdapter<NearbyStopInfo> {
    Context mContext;

    private static final String LOG_TAG = NearbyStopArrayAdapter.class.getSimpleName();

    public NearbyStopArrayAdapter(Context context) {
        super(context, 0);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NearbyStopInfo info = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_nearby_stop, parent, false);
        }

        TextView tv1 = (TextView) convertView.findViewById(R.id.stop_name);
        tv1.setText(info.stop_name);
        Log.d(LOG_TAG, info.stop_name);

//        TextView tv2 = (TextView) convertView.findViewById(R.id.departure_info_time);
//        String expMin = info.expectedMins;
//        if (expMin.equals("0")) {
//            tv2.setText("DUE");
//        } else if (expMin.equals("1")) {
//            tv2.setText(String.format(mContext.getResources().getString(R.string.min1), expMin));
//        } else {
//            tv2.setText(String.format(mContext.getResources().getString(R.string.min2), expMin));
//        }

        return convertView;
    }
}

