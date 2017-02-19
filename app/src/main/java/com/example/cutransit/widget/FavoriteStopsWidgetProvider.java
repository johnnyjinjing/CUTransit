package com.example.cutransit.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.cutransit.R;
import com.example.cutransit.ui.StopDepartureActivity;

import static com.example.cutransit.ui.MainActivity.INTENT_EXTRA_STOP_FAVORITE;
import static com.example.cutransit.ui.MainActivity.INTENT_EXTRA_STOP_ID;
import static com.example.cutransit.ui.MainActivity.INTENT_EXTRA_STOP_NAME;


/**
 * Created by JingJin on 2/18/17.
 */

public class FavoriteStopsWidgetProvider extends AppWidgetProvider {

    public static final String INTENT_ACTION = "com.example.android.cutransit.widget.INTENT_ACTION";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // update each of the app widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {

            // Set up the intent that starts the StockPriceWidgetIntentService, which will provide the views for this collection.
            Intent intent = new Intent(context, FavoriteStopsWidgetRemoteService.class);

            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_favorite);

            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects to a RemoteViewsService  through the specified intent.
            // This is how you populate the data.
            rv.setRemoteAdapter(appWidgetIds[i], R.id.widget_list_view, intent);
            rv.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews object above.
//            rv.setEmptyView(R.id.stack_view, R.id.empty_view);

            //
            // Do additional processing specific to this app widget...
            //
//            // Create an Intent to launch MainActivity
//            Intent launchIntent = new Intent(context, MainActivity.class);

            // This section makes it possible for items to have individualized behavior.
            // It does this by setting up a pending intent template. Individuals items of a collection
            // cannot set up their own pending intents. Instead, the collection as a whole sets
            // up a pending intent template, and the individual items set a fillInIntent
            // to create unique behavior on an item-by-item basis.
            Intent newIntent = new Intent(context, FavoriteStopsWidgetProvider.class);
            // Set the action for the intent.
            newIntent.setAction(FavoriteStopsWidgetProvider.INTENT_ACTION);
            newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.widget_list_view);
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(INTENT_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            String stopName = intent.getStringExtra(INTENT_EXTRA_STOP_NAME);
            String stopId = intent.getStringExtra(INTENT_EXTRA_STOP_ID);
            int favorite = intent.getIntExtra(INTENT_EXTRA_STOP_FAVORITE, 1);

            Toast.makeText(context, stopName + " " +
                    stopId + " " +
                    favorite, Toast.LENGTH_SHORT).show();

            Intent newIntent = new Intent(context, StopDepartureActivity.class)
                    .putExtra(INTENT_EXTRA_STOP_ID, stopId)
                    .putExtra(INTENT_EXTRA_STOP_NAME, stopName)
                    .putExtra(INTENT_EXTRA_STOP_FAVORITE, favorite);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(newIntent);
        }
        super.onReceive(context, intent);
    }
}
