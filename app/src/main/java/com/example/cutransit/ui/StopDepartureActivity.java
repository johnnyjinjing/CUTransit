package com.example.cutransit.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cutransit.BuildConfig;
import com.example.cutransit.R;
import com.example.cutransit.adapter.DepartureArrayAdapter;
import com.example.cutransit.data.DataContract;
import com.example.cutransit.model.DepartureInfo;
import com.example.cutransit.util.DataUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;


public class StopDepartureActivity extends AppCompatActivity {
    private static final String LOG_TAG = StopDepartureActivity.class.getSimpleName();

    View rootView;
    ListView listView;
    final Handler handler = new Handler();
    private Runnable runnable;
    private Timer timer;

    int stopFavorite;
    String stopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stop_departure);

        String stopName;

        rootView = findViewById(R.id.activity_stop_departure);
        Intent intent = getIntent();
        stopName = intent.getStringExtra(MainActivity.INTENT_EXTRA_STOP_NAME);
        stopId = intent.getStringExtra(MainActivity.INTENT_EXTRA_STOP_ID);
        stopFavorite = intent.getIntExtra(MainActivity.INTENT_EXTRA_STOP_FAVORITE, 0);


        setTitle(stopName);

        final DepartureArrayAdapter adapter = new DepartureArrayAdapter(this);
        listView = (ListView) rootView.findViewById(R.id.list_all_departures);
        listView.setAdapter(adapter);
//        listView.setEmptyView(rootView.findViewById(R.id.empty_list_all_departures));

        timer = new Timer();

        runnable = new Runnable() {
            public void run() {
                FetchStopDepartureData(stopId, adapter);
            }
        };

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        };
        timer.schedule(task, 0, 1000 * 60);

//        FetchStopDepartureData(stopId, adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite, menu);
        MenuItem item = menu.findItem(R.id.action_favorite);
        item.setChecked(stopFavorite == 1);
        item.setIcon(item.isChecked() ? R.drawable.heart_selected : R.drawable.heart_unselected);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            item.setChecked(!item.isChecked());
            Log.d(LOG_TAG, "" + item.isChecked());
            item.setIcon(item.isChecked() ? R.drawable.heart_selected : R.drawable.heart_unselected);

            ContentValues cv = new ContentValues();
            cv.put(DataContract.StopEntry.COLUMN_FAVORITE, item.isChecked() ? "1" : "0");
            getContentResolver().update(DataContract.StopEntry.CONTENT_URI,
                    cv,
                    DataContract.StopEntry.TABLE_NAME + "." +
                            DataContract.StopEntry.COLUMN_ID + " = ? ",
                    new String[]{stopId});

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy called");
        handler.removeCallbacksAndMessages(runnable);
        timer.cancel();
    }

    public void FetchStopDepartureData(String id, final ArrayAdapter<DepartureInfo> adapter) {
        final ArrayList<DepartureInfo> infos = new ArrayList<>();

        Uri uri = Uri.parse(DataUtils.QUEST_URL + "/" + DataUtils.PATH_GET_STOP_DEPARTURE).buildUpon()
                .appendQueryParameter(DataUtils.CUMTD_API_KEY_PARAM, BuildConfig.CUMTD_API_KEY)
                .appendQueryParameter(DataUtils.CUMTD_API_KEY_STOP_ID, id)
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
                Log.d(LOG_TAG, "Get departure data success");
                Log.d(LOG_TAG, new String(response));
                DataUtils.parseStopDepartureDataFromJson(infos, new String(response));
                adapter.clear();
                for (DepartureInfo info : infos) {
                    adapter.add(info);
                }

                // Delay the empty view binding to avoid flashing
                listView.setEmptyView(rootView.findViewById(R.id.empty_list_all_departures));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(LOG_TAG, "Get departure data failure");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

}
