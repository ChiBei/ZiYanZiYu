package com.chiibeii.ZiYanZiYu.ui.activity

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.chiibeii.ZiYanZiYu.R

class RandomBlogWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        // Perform this loop procedure for each App Widget that belongs to this provider
//        appWidgetIds.forEach { appWidgetId ->
//            // Create an Intent to launch ExampleActivity
//            val pendingIntent: PendingIntent = Intent(context, ExampleActivity::class.java)
//                .let { intent ->
//                    PendingIntent.getActivity(context, 0, intent, 0)
//                }
//
//            // Get the layout for the App Widget and attach an on-click listener
//            // to the button
//            val views: RemoteViews = RemoteViews(
//                context.packageName,
//                R.layout.widget_random_blog
//            ).apply {
//                setOnClickPendingIntent(R.id.button, pendingIntent)
//            }
//
//            // Tell the AppWidgetManager to perform an update on the current app widget
//            appWidgetManager.updateAppWidget(appWidgetId, views)
//        }
    }
}
