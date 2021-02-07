package com.example.commoncalendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class calendarwidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context,CalendarActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            RemoteViews view = new RemoteViews(context.getPackageName(),R.layout.calendarwidget);
            view.setOnClickPendingIntent(R.id.calendarView,pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId,view);
        }
    }

}