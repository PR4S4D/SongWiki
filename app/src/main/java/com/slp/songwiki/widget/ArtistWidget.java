package com.slp.songwiki.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.slp.songwiki.R;
import com.slp.songwiki.ui.activity.ArtistActivity;
import com.slp.songwiki.ui.fragment.TopArtistsFragment;

/**
 * Implementation of App Widget functionality.
 */
public class ArtistWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.artist_widget);
            setWidgetOnClick(context, views);
            Intent intent = new Intent(context, ArtistWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            Log.i("updating the widget ", appWidgetIds.toString());
            intent.putExtra("appWidgetIds", appWidgetIds);
            views.setRemoteAdapter(R.id.artists, intent);
            views.setEmptyView(R.id.artists,R.id.widget_empty);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void setWidgetOnClick(Context context, RemoteViews views) {

        Intent intent = new Intent(context, ArtistActivity.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.artists, clickPendingIntentTemplate);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("Inside onreceive", String.valueOf(intent.getAction()));
        if(TopArtistsFragment.ACTION_DATA_UPDATED.equals(intent.getAction()) ){

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.artist_item);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


