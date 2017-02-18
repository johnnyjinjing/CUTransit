package com.example.cutransit.ui;

/**
 * Created by JingJin on 2/16/17.
 */

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cutransit.R;
import com.example.cutransit.adapter.StopCursorAdapter;
import com.example.cutransit.data.DataContract;

public class FavoriteStopsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STOP_LOADER = 0;

    private static final String LOG_TAG = AllStopsFragment.class.getSimpleName();

    private StopCursorAdapter stopCursorAdapter;

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        stopCursorAdapter = new StopCursorAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_all_stops, container, false);

        listView = (ListView) rootView.findViewById(R.id.list_all_stops);
        listView.setAdapter(stopCursorAdapter);
        listView.setEmptyView(rootView.findViewById(R.id.empty_list_favorite_stops));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Cursor adapter returns a cursor at position for getItem(), or null
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                if (cursor != null) {
                    int colId = cursor.getColumnIndex(DataContract.StopEntry.COLUMN_ID);
                    int colName = cursor.getColumnIndex(DataContract.StopEntry.COLUMN_NAME);
                    int colFavorite = cursor.getColumnIndex(DataContract.StopEntry.COLUMN_FAVORITE);
//                    Toast.makeText(getContext(), cursor.getString(col), Toast.LENGTH_SHORT).show();
                    ((Callback) getActivity()).onItemSelected(cursor.getString(colId),
                            cursor.getString(colName), cursor.getInt(colFavorite));
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(STOP_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = DataContract.StopEntry.CONTENT_URI;
        return new CursorLoader(getActivity(), uri, null,
                DataContract.StopEntry.TABLE_NAME + "." + DataContract.StopEntry.COLUMN_FAVORITE + " = ? ",
                new String[]{"1"},
                DataContract.StopEntry.COLUMN_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        stopCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        stopCursorAdapter.swapCursor(null);
    }

    public interface Callback {
        public void onItemSelected(String id, String name, int favorite);
    }
}

