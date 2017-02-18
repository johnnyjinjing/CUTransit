package com.example.cutransit.ui;

import android.content.AsyncQueryHandler;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cutransit.BuildConfig;
import com.example.cutransit.R;
import com.example.cutransit.adapter.NearbyStopArrayAdapter;
import com.example.cutransit.data.DataContract;
import com.example.cutransit.model.NearbyStopInfo;
import com.example.cutransit.util.DataUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by JingJin on 2/16/17.
 */

public class NearbyStopsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String LOG_TAG = NearbyStopsFragment.class.getSimpleName();

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100;
    private static final int locationRequestInterval = 120;
    private static final int locationRequestFastestInterval = 60;

    View rootView;
    ListView listView;
    TextView tv_permission;

    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    NearbyStopArrayAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(locationRequestInterval * 1000)
                .setFastestInterval(locationRequestFastestInterval * 1000);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_nearby_stops, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_nearby_stops);

        adapter = new NearbyStopArrayAdapter(getActivity());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearbyStopInfo info = (NearbyStopInfo) parent.getItemAtPosition(position);
                if (info != null) {
                    new AsyncQueryHandler(getActivity().getContentResolver()) {
                        @Override
                        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                            if (cursor == null) {
                                // Some providers return null if an error occurs whereas others throw an exception
                            } else if (cursor.getCount() < 1) {
                                // No matches found
                            } else {
                                cursor.moveToFirst();
                                ((NearbyStopsFragment.Callback) getActivity()).onItemSelected(
                                        cursor.getString(cursor.getColumnIndex(DataContract.StopEntry.COLUMN_ID)),
                                        cursor.getString(cursor.getColumnIndex(DataContract.StopEntry.COLUMN_NAME)),
                                        cursor.getInt(cursor.getColumnIndex(DataContract.StopEntry.COLUMN_FAVORITE)));
                            }
                        }
                    }.startQuery(
                            1, null,
                            DataContract.StopEntry.CONTENT_URI,
                            null,
                            DataContract.StopEntry.TABLE_NAME + "." + DataContract.StopEntry.COLUMN_ID + " = ? ",
                            new String[]{info.id},
                            null
                    );
                }
            }
        });

        tv_permission = (TextView) rootView.findViewById(R.id.permission_status);

        return rootView;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d(LOG_TAG, "Google api connected");

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

//            Log.d(LOG_TAG, "Try getting permission");

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Log.d(LOG_TAG, "Not really try getting permission!");

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_ACCESS_FINE_LOCATION);

            } else {
                // No explanation needed, we can request the permission.

//                Log.d(LOG_TAG, "Really try getting permission!");
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            tv_permission.setVisibility(View.GONE);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private void handleNewLocation(Location location) {
        FetchNearbyStop(adapter, location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.d(LOG_TAG, "Location service failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d(LOG_TAG, "Waiting for permission");
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "Permission grant");
                    mGoogleApiClient.reconnect();
                }
                else {
                    Log.d(LOG_TAG, "Permission denied");
                    listView.setVisibility(View.GONE);
                    tv_permission.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    public void FetchNearbyStop(final ArrayAdapter<NearbyStopInfo> adapter, double lat, double lon) {
        final ArrayList<NearbyStopInfo> infos = new ArrayList<>();

        Uri uri = Uri.parse(DataUtils.QUEST_URL + "/" + DataUtils.PATH_GET_NEARBY_STOPS).buildUpon()
                .appendQueryParameter(DataUtils.CUMTD_API_KEY_PARAM, BuildConfig.CUMTD_API_KEY)
                .appendQueryParameter(DataUtils.CUMTD_API_KEY_LAT, String.valueOf(lat))
                .appendQueryParameter(DataUtils.CUMTD_API_KEY_LON, String.valueOf(lon))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

//        Log.d(LOG_TAG, "URL is: " + url);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url.toString(), new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
//                Log.d(LOG_TAG, "Get nearby stop success");
                DataUtils.parseNearbyStopsDataFromJson(infos, new String(response));
                adapter.clear();
                for (NearbyStopInfo info : infos) {
                    adapter.add(info);
                }

                // Delay the empty view binding to avoid flashing
                listView.setEmptyView(rootView.findViewById(R.id.empty_list_nearby_stops));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
//                Log.d(LOG_TAG, "Get nearby stop failure");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public interface Callback {
        void onItemSelected(String id, String name, int favorite);
    }

}