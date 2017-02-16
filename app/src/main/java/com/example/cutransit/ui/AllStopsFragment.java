package com.example.cutransit.ui;

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

public class AllStopsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = AllStopsFragment.class.getSimpleName();

    private StopCursorAdapter stopCursorAdapter;
    ListView listView;
    private static final int STOP_LOADER = 0;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Cursor adapter returns a cursor at position for getItem(), or null
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    int colId = cursor.getColumnIndex(DataContract.StopEntry.COLUMN_ID);
                    int colName = cursor.getColumnIndex(DataContract.StopEntry.COLUMN_NAME);
//                    Toast.makeText(getContext(), cursor.getString(col), Toast.LENGTH_SHORT).show();
//                    ((InputQueue.Callback) getActivity()).onItemSelected(cursor.getInt(col));
                    ((Callback) getActivity()).onItemSelected(cursor.getString(colId), cursor.getString(colName));
                }
            }
        });

//        if (savedInstanceState != null && savedInstanceState.containsKey(SCROLL_LOCATION_KEY)) {
//            mPosition = savedInstanceState.getInt(SCROLL_LOCATION_KEY);
//        }

        return rootView;
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(STOP_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = DataContract.StopEntry.CONTENT_URI;
//        Log.d(LOG_TAG, "CursorLoader created, from " + uri);
        return new CursorLoader(getActivity(), uri, null, null, null,
                DataContract.StopEntry.COLUMN_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        stopCursorAdapter.swapCursor(data);
//        if (data == null) {
//            Log.d(LOG_TAG, "Cursor is null");
//        }
//        Log.d(LOG_TAG, "Load finished");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        stopCursorAdapter.swapCursor(null);
//        Log.d(LOG_TAG, "Loader reset");
    }

    public interface Callback {
        public void onItemSelected(String id, String name);
    }

//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
