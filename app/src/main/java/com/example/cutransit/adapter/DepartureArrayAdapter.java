package com.example.cutransit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cutransit.R;
import com.example.cutransit.model.DepartureInfo;

import java.util.ArrayList;

/**
 * Created by JingJin on 2/15/17.
 */

public class DepartureArrayAdapter extends ArrayAdapter<DepartureInfo> {
    public DepartureArrayAdapter(Context context, ArrayList<DepartureInfo> infos) {
        super(context, 0, infos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DepartureInfo info = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_departure, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.departure_info);
        tv.setText(info.headSign);

        return convertView;
    }
}
