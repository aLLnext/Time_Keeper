package com.timekeeper.Timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.timekeeper.Utils.CONSTANTS
import com.timekeeper.Utils.NotificationsUtils
import com.timekeeper.Utils.PrefUtilsTimer
import java.sql.Time

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            CONSTANTS.ACTION_STOP -> {
                TimerActivity.removeAlarm(context)
                PrefUtilsTimer.setTimerState(TimerActivity.TimerState.stopped, context)
                NotificationsUtils.hideTimerNotification(context)
            }

            CONSTANTS.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtilsTimer.getSecondsRemaining(context)
                val alarmSetTime = PrefUtilsTimer.getAlarmSetTime(context)
                val nowSeconds = TimerActivity.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtilsTimer.setSecondsRemaining(secondsRemaining, context)

                TimerActivity.removeAlarm(context)
                PrefUtilsTimer.setTimerState(TimerActivity.TimerState.paused, context)
                NotificationsUtils.showTimerPaused(context)
            }

            CONSTANTS.ACTION_RESUME -> {
                var secondsRemaining = PrefUtilsTimer.getSecondsRemaining(context)
                val wakeUpTime = TimerActivity.setAlarm(context, TimerActivity.nowSeconds, secondsRemaining)
                PrefUtilsTimer.setTimerState(TimerActivity.TimerState.running, context)
                NotificationsUtils.showTimerRunning(context, wakeUpTime)
            }

            CONSTANTS.ACTION_START -> {
                val minutesRemaining = PrefUtilsTimer.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val wakeUpTime = TimerActivity.setAlarm(context, TimerActivity.nowSeconds, secondsRemaining)
                PrefUtilsTimer.setTimerState(TimerActivity.TimerState.running, context)
                PrefUtilsTimer.setSecondsRemaining(secondsRemaining, context)
                NotificationsUtils.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}
