package com.timekeeper.Timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.timekeeper.Utils.NotificationsUtils
import com.timekeeper.Utils.PrefUtilsTimer

class TimerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationsUtils.showTimerExpired(context)

        PrefUtilsTimer.setTimerState(TimerActivity.TimerState.stopped, context)
        PrefUtilsTimer.setAlarmSetTime(0, context)
    }
}
