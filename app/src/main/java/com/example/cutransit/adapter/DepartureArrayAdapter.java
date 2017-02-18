package com.example.cutransit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cutransit.R;
import com.example.cutransit.model.DepartureInfo;
import com.example.cutransit.util.ColorUtils;

/**
 * Created by JingJin on 2/15/17.
 */

public class DepartureArrayAdapter extends ArrayAdapter<DepartureInfo> {
    private Context mContext;

    public DepartureArrayAdapter(Context context) {
        super(context, 0);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        DepartureInfo info = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_departure, parent, false);
        }

        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.departure_info);
        int backgroundcolor = Color.parseColor("#" + info.routeColor);
        int textcolor = ColorUtils.getContrastColor(backgroundcolor);
        ll.setBackgroundColor(backgroundcolor);
        ((TextView) ll.findViewById(R.id.departure_info_route)).setTextColor(textcolor);
        ((TextView) ll.findViewById(R.id.departure_info_time)).setTextColor(textcolor);

        TextView tv1 = (TextView) convertView.findViewById(R.id.departure_info_route);
        tv1.setText(info.headSign);

        TextView tv2 = (TextView) convertView.findViewById(R.id.departure_info_time);
        String expMin = info.expectedMins;
        switch (expMin) {
            case "0":
                tv2.setText(mContext.getResources().getString(R.string.min0));
                break;
            case "1":
                tv2.setText(String.format(mContext.getResources().getString(R.string.min1), expMin));
                break;
            default:
                tv2.setText(String.format(mContext.getResources().getString(R.string.min2), expMin));
                break;
        }

        return convertView;
    }
}
