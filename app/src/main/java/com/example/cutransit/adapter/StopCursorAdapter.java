package com.example.cutransit.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cutransit.R;
import com.example.cutransit.data.DataContract;

/**
 * Created by JingJin on 2/14/17.
 */

public class StopCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = StopCursorAdapter.class.getSimpleName();

    public StopCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_stop, parent, false);
//        Log.d(LOG_TAG, "New view created");
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView stopNameTextView = (TextView) view.findViewById(R.id.stop_name);
        int idxStopName = cursor.getColumnIndex(DataContract.StopEntry.COLUMN_NAME);
        stopNameTextView.setText(cursor.getString(idxStopName));
//        Log.d(LOG_TAG, "View binded");
    }
}
