package com.example.cutransit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cutransit.R;
import com.example.cutransit.model.NearbyStopInfo;
import com.example.cutransit.util.NumberUtil;

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

        TextView tv2 = (TextView) convertView.findViewById(R.id.stop_distance);
        tv2.setText(NumberUtil.feetToMileDisplay(info.distance));
        return convertView;
    }
}

