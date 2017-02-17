package com.example.cutransit.ui;

import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cutransit.BuildConfig;
import com.example.cutransit.R;
import com.example.cutransit.adapter.NearbyStopArrayAdapter;
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

    View rootView;
    ListView listView;

    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100;
    private int count = 0;

    NearbyStopArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(120 * 1000)
                .setFastestInterval(60 * 1000);
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
                    ((NearbyStopsFragment.Callback) getActivity()).onItemSelected(info.id,
                            info.stop_name, 0);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.
                    // PERMISSION_REQUEST_ACCESS_FINE_LOCATION can be any unique int
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
                }
            }
//            Log.d(LOG_TAG, "Permission denied");
        }
    }

    private void handleNewLocation(Location location) {

        FetchNearbyStop(adapter, location.getLatitude(), location.getLongitude());
        Log.d(LOG_TAG, location.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "Location service failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    } catch (SecurityException e) {

                    }
                } else {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                }
                return;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
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

        Log.d(LOG_TAG, "URL is: " + url);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url.toString(), new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d(LOG_TAG, "Get nearby stop success");
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
                Log.d(LOG_TAG, "Get nearby stop failure");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public interface Callback {
        public void onItemSelected(String id, String name, int favorite);
    }

}

