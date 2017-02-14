package com.example.cutransit;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cutransit.data.DataContract;

/**
 * Created by JingJin on 2/14/17.
 */

public class StopCursorAdapter extends CursorAdapter {

    private View view;
    private TextView stopNameTextView;

    public StopCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item_stop, parent, false);
        stopNameTextView = (TextView) view.findViewById(R.id.stop_name);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int idxStopName = cursor.getColumnIndex(DataContract.StopEntry.COLUMN_NAME);
        stopNameTextView.setText(cursor.getString(idxStopName));
    }
}
