package com.timekeeper.Timer


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.View
import com.timekeeper.Timer.TimerReceiver
import com.timekeeper.Utils.CONSTANTS
import com.timekeeper.Utils.PrefUtilsTimer
import kotlinx.android.synthetic.main.activity_timer.view.*
import kotlinx.android.synthetic.main.content_timer.view.*
import java.util.*

class TimerActivity() {
    private var view: View? = null

    constructor(v: View) : this() {
        view = v
        initTimer()
    }


    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long {
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtilsTimer.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context) {
            val intent = Intent(context, TimerReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtilsTimer.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }

    enum class TimerState {
        stopped, paused, running
    }

    lateinit var timer: CountDownTimer
    var timerLength: Long = 0
    var timerState = TimerState.stopped
    var secondsRemaining = 0L
    var activityId = 0

    //    override fun onResume() {
//        super.onResume()
//
//        initTimer()
//
//        removeAlarm(this)
//        NotificationsUtils.hideTimerNotification(this)
//    }
//
//
//    override fun onPause() {
//        super.onPause()
//
//        if (timerState == TimerState.running) {
//            timer.cancel()
//            val wakeUpTime = setAlarm(this, nowSeconds, secondsRemaining)
//            NotificationsUtils.showTimerRunning(this, wakeUpTime)
//        } else if (timerState == TimerState.paused) {
//            NotificationsUtils.showTimerPaused(this)
//        }
//
//        PrefUtilsTimer.setPreviousTimerLengthSeconds(timerLength, this)
//        PrefUtilsTimer.setSecondsRemaining(secondsRemaining, this)
//        PrefUtilsTimer.setTimerState(timerState, this)
//    }
//
    private fun initTimer() {
        timerState = PrefUtilsTimer.getTimerState(view!!.context)
        activityId = PrefUtilsTimer.getActivityId(view!!.context)

        if (timerState == TimerState.stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.running || timerState == TimerState.paused)
            PrefUtilsTimer.getSecondsRemaining(view!!.context)
        else
            timerLength

        val alarmSetTime = PrefUtilsTimer.getAlarmSetTime(view!!.context)
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if (secondsRemaining <= 0)
            onTimerFinished()
        else if (timerState == TimerState.running)
            startTimer()

        updateButtons()
        updateCountDownUI()
    }

    fun onTimerFinished() {
        timerState = TimerState.stopped

        setNewTimerLength()

        view!!.countdown_progressbar.progress = timerLength.toInt()

        PrefUtilsTimer.setSecondsRemaining(timerLength, view!!.context)
        PrefUtilsTimer.setActivityId(activityId, view!!.context)

        secondsRemaining = timerLength

        updateButtons()
        updateCountDownUI()
    }

    private fun startTimer() {
        timerState = TimerState.running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millis: Long) {
                secondsRemaining = millis / 1000
                updateCountDownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtilsTimer.getTimerLength(view!!.context)
        timerLength = (lengthInMinutes * 60L)
        view!!.countdown_progressbar.max = timerLength.toInt()
    }

    private fun setPreviousTimerLength() {
        timerLength = PrefUtilsTimer.getPreviousTimerLengthSeconds(view!!.context)
        view!!.countdown_progressbar.max = timerLength.toInt()
    }

    fun updateCountDownUI() {

        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        view!!.text_countdown.text = "$minutesUntilFinished:${
        if (secondsStr.length == 2) secondsStr
        else "0$secondsStr"}"

        view!!.countdown_progressbar.progress = (secondsRemaining).toInt()
    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.running -> {
                view!!.fab_play.isEnabled = false
                view!!.fab_pause.isEnabled = true
                view!!.fab_stop.isEnabled = true
            }
            TimerState.stopped -> {
                view!!.fab_play.isEnabled = true
                view!!.fab_pause.isEnabled = false
                view!!.fab_stop.isEnabled = false
            }
            TimerState.paused -> {
                view!!.fab_play.isEnabled = true
                view!!.fab_pause.isEnabled = false
                view!!.fab_stop.isEnabled = true
            }
        }
    }

    fun setFabPlay() {
        startTimer()
        timerState = TimerActivity.TimerState.running
        updateButtons()
    }

    fun setFabStop() {
        timer.cancel()
        onTimerFinished()
    }

    fun setFabPause() {
        timer.cancel()
        timerState = TimerActivity.TimerState.paused
        updateButtons()
    }

    fun initializeTimer() {
        initTimer()
    }
}
