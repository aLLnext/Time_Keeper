package com.timekeeper.Timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.timekeeper.R
import com.timekeeper.Utils.NotificationsUtils
import com.timekeeper.Utils.PrefUtilsTimer

import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*
import java.util.*

class TimerActivity : AppCompatActivity() {

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

    private lateinit var timer: CountDownTimer
    private var timerLength: Long = 0
    private var timerState = TimerState.stopped

    private var secondsRemaining = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        fab_play.setOnClickListener {
            startTimer()
            timerState = TimerState.running
            updateButtons()
        }

        fab_pause.setOnClickListener {
            timer.cancel()
            timerState = TimerState.paused
            updateButtons()
        }
        //setSupportActionBar(toolbar)
        fab_stop.setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

        initTimer()

        removeAlarm(this)
        NotificationsUtils.hideTimerNotification(this)
    }


    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.running) {
            timer.cancel()
            val wakeUpTime = setAlarm(this, nowSeconds, secondsRemaining)
            NotificationsUtils.showTimerRunning(this, wakeUpTime)
        } else if (timerState == TimerState.paused) {
            NotificationsUtils.showTimerPaused(this)
        }

        PrefUtilsTimer.setPreviousTimerLengthSeconds(timerLength, this)
        PrefUtilsTimer.setSecondsRemaining(secondsRemaining, this)
        PrefUtilsTimer.setTimerState(timerState, this)
    }

    private fun initTimer() {
        timerState = PrefUtilsTimer.getTimerState(this)

        setNewTimerLength()

        if (timerState == TimerState.stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.running || timerState == TimerState.paused)
            PrefUtilsTimer.getSecondsRemaining(this)
        else
            timerLength

        val alarmSetTime = PrefUtilsTimer.getAlarmSetTime(this)
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if (secondsRemaining <= 0)
            onTimerFinished()
        else if (timerState == TimerState.running)
            startTimer()

        updateButtons()
        updateCountDownUI()
    }


    private fun onTimerFinished() {
        timerState = TimerState.stopped

        setNewTimerLength()

        countdown_progressbar.progress = 0

        PrefUtilsTimer.setSecondsRemaining(timerLength, this)
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
        val lengthInMinutes = PrefUtilsTimer.getTimerLength(this)
        timerLength = (lengthInMinutes * 60L)
        countdown_progressbar.max = timerLength.toInt()
    }

    private fun setPreviousTimerLength() {
        timerLength = PrefUtilsTimer.getPreviousTimerLengthSeconds(this)
        countdown_progressbar.max = timerLength.toInt()
    }

    private fun updateCountDownUI() {

        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        text_countdown.text = "$minutesUntilFinished:${
        if (secondsStr.length == 2) secondsStr
        else "0$secondsStr"}"

        countdown_progressbar.progress = (timerLength - secondsRemaining).toInt()
    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.running -> {
                fab_play.isEnabled = false
                fab_pause.isEnabled = true
                fab_stop.isEnabled = true
            }
            TimerState.stopped -> {
                fab_play.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = false
            }
            TimerState.paused -> {
                fab_play.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
        }
    }
}
