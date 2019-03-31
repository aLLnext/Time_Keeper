package com.timekeeper.Model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.example.toxaxab.timekeeper.R
import com.timekeeper.MainActivity

class SetNotification(val context: Context) {
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    private var mNotifyManager: NotificationManager? = null

    init {
        mNotifyManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Timer Notification", NotificationManager
                    .IMPORTANCE_HIGH)
            notificationChannel.enableVibration(false)
            notificationChannel.enableLights(false)
            notificationChannel.description = "Notification from TimeKeeper"
            mNotifyManager!!.createNotificationChannel(notificationChannel)
        }
    }

    fun sendNotification(currentActivity: MyActivity?) {
        val notifyBuilder = getNotificationBuilder(currentActivity)
        mNotifyManager!!.notify(currentActivity!!.id, notifyBuilder.build())
    }


    private fun getNotificationIntent(currentActivity: MyActivity?): Intent {
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra("activity", currentActivity!!.id)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return notificationIntent
    }

    private fun getNotificationBuilder(currentActivity: MyActivity?): NotificationCompat.Builder {
        val notificationIntent = getNotificationIntent(currentActivity)
        notificationIntent.putExtra("start", SystemClock.elapsedRealtime() - currentActivity!!.currentTime)

        val notificationPendingIntent = PendingIntent.getActivity(context,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationStopIntent = getNotificationIntent(currentActivity)
        notificationStopIntent.action = "STOP"
        val notificationPendingStop = PendingIntent.getActivity(context,
                0, notificationStopIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val remoteViews = RemoteViews(context.packageName, R.layout.notification)
        remoteViews.setTextViewText(R.id.textView, currentActivity.name)
        remoteViews.setChronometer(R.id.timer, SystemClock.elapsedRealtime() - currentActivity.currentTime, "%s", true)
        remoteViews.setOnClickPendingIntent(R.id.root, notificationPendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.n_btn_stop, notificationPendingStop)
        return NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_play)
                .setCustomContentView(remoteViews)
                .setContentIntent(notificationPendingIntent)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setShowWhen(false)
                .setOngoing(true)
    }

    fun cancelNotification(activity: MyActivity?) {
        mNotifyManager?.cancel(activity!!.id)
    }
}