package com.example.cutransit.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.cutransit.R;
import com.example.cutransit.data.DataContract;
import com.example.cutransit.model.StopInfo;

import java.util.ArrayList;
import java.util.List;

import static com.example.cutransit.ui.MainActivity.INTENT_EXTRA_STOP_FAVORITE;
import static com.example.cutransit.ui.MainActivity.INTENT_EXTRA_STOP_ID;
import static com.example.cutransit.ui.MainActivity.INTENT_EXTRA_STOP_NAME;

/**
 * Created by JingJin on 2/18/17.
 */

public class FavoriteStopsWidgetRemoteService extends RemoteViewsService {

    public final static String LOG_TAG = FavoriteStopsWidgetRemoteService.class.getSimpleName();

    public FavoriteStopsWidgetRemoteService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavoriteStopsRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class FavoriteStopsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private List<StopInfo> stops;
        private Context mContext;
        private int mAppWidgetId;

        private Cursor cursor = null;


        public FavoriteStopsRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            stops = new ArrayList<>();
            Log.d(LOG_TAG, "RemoteViewsFactory onCreate");
        }

        @Override
        public void onDataSetChanged() {
            long identity = Binder.clearCallingIdentity();

            String[] projection = new String[]{
                    DataContract.StopEntry.COLUMN_ID,
                    DataContract.StopEntry.COLUMN_NAME,
                    DataContract.StopEntry.COLUMN_FAVORITE,
            };

            cursor = getContentResolver().query(DataContract.StopEntry.CONTENT_URI,
                    projection,
                    DataContract.StopEntry.TABLE_NAME + "." + DataContract.StopEntry.COLUMN_FAVORITE + " = ? ",
                    new String[]{"1"},
                    DataContract.StopEntry.COLUMN_NAME + " ASC");

            cursor.moveToPosition(-1);

            try {
                while (cursor.moveToNext()) {
                    stops.add(new StopInfo(cursor.getString(1), cursor.getString(0), cursor.getInt(2)));
                    Log.d(LOG_TAG, cursor.getString(0) + " " + cursor.getString(1) +  "\n");
                }
            } finally {
                cursor.close();
            }

            Log.d(LOG_TAG, "cursor length: " + cursor.getCount() + "\n" + "stops length: " + stops.size());
            Binder.restoreCallingIdentity(identity);
        }


        @Override
        public void onDestroy() {
            stops.clear();
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(getPackageName(), R.layout.widget_list_item_favorite_stops);

            Log.d("Widgets", "Get view at " + position);

            rv.setTextViewText(R.id.widget_stop_name, stops.get(position).stop_name);

            // Next, set a fill-intent, which will be used to fill in the pending intent template
            // that is set on the collection view in StackWidgetProvider.
            Bundle extras = new Bundle();
            extras.putString(INTENT_EXTRA_STOP_NAME, stops.get(position).stop_name);
            extras.putString(INTENT_EXTRA_STOP_ID, stops.get(position).id);
            extras.putInt(INTENT_EXTRA_STOP_FAVORITE, stops.get(position).favorite);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            // Make it possible to distinguish the individual on-click
            // action of a given item
            rv.setOnClickFillInIntent(R.id.widget_stop_name, fillInIntent);

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(getPackageName(), R.layout.widget_list_item_favorite_stops);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
