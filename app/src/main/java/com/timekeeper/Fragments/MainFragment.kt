package com.timekeeper.Fragments

import com.timekeeper.Timer.TimerActivity.Companion.nowSeconds
import com.timekeeper.Timer.TimerActivity.Companion.removeAlarm
import com.timekeeper.Timer.TimerActivity.Companion.setAlarm
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.timekeeper.Adapters.MainViewAdapter
import com.timekeeper.Data.Activity
import com.timekeeper.R
import com.timekeeper.Timer.TimerReceiver
import com.timekeeper.Utils.PrefUtilsTimer
import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.activity_timer.view.*

import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.item_activity.view.*
import java.util.*
import com.timekeeper.Timer.TimerActivity
import com.timekeeper.Utils.BottomOffsetDecoration
import com.timekeeper.Utils.NotificationsUtils
import android.widget.AbsListView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_activity.*

class MainFragment : Fragment() {

    var activities: ArrayList<Activity> = arrayListOf(
        Activity(0, "sleep"), Activity(1, "code"),
        Activity(2, "repeat"), Activity(3, "another thing"), Activity(4, "else"), Activity(5, "again")
    )

    companion object {
        const val KEY = "MainFragment"
    }


    private lateinit var v: View

    var adapter: MainViewAdapter? = null

    private lateinit var timerActivity: TimerActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_main, null)
        val MLight = Typeface.createFromAsset(activity!!.assets, "fonts/Montserrat-Light.ttf")
        val MMedium = Typeface.createFromAsset(activity!!.assets, "fonts/Montserrat-Medium.ttf")

        v.titlepage!!.typeface = MMedium
        v.subtitle.typeface = MLight
        v.endpage.typeface = MLight
        timerActivity = TimerActivity(this, v)
        adapter = MainViewAdapter(timerActivity, v.bottom_sheet, this.context!!, activities)
        v.recycle_activity.layoutManager = LinearLayoutManager(activity)
        v.recycle_activity.adapter = adapter
        val offsetPx = 150
        val bottomOffsetDecoration = BottomOffsetDecoration(offsetPx)
        v.recycle_activity.addItemDecoration(bottomOffsetDecoration)

        adapter!!.notifyDataSetChanged()

        val bottomSheetBehavior = BottomSheetBehavior.from(v.bottom_sheet)

        Log.i("Timer_STATE", timerActivity.timerState.toString())
        Log.i("Timer_id", timerActivity.activityId.toString())
        if (timerActivity.timerState == TimerActivity.TimerState.running) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.isHideable = false
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetBehavior.isHideable = true
        }


        v.recycle_activity.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (timerActivity.timerState == TimerActivity.TimerState.stopped &&
                    bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED
                )
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

//                if (dy > 0) {
//                    // Scrolling up
//                } else {
//                    // Scrolling down
//                }
            }
        })

        v.fab_play.setOnClickListener {
            //btnplay.setImageResource(R.drawable.ic_stop_black_48dp)
            timerActivity.setFabPlay()
            bottomSheetBehavior.isHideable = false
        }

        v.fab_pause.setOnClickListener {
            //btnplay.setImageResource(R.drawable.ic_pause_black_48dp)
            timerActivity.setFabPause()
            bottomSheetBehavior.isHideable = false
        }
        v.fab_stop.setOnClickListener {
            //btnplay.setImageResource(R.drawable.ic_play_arrow_black_48dp)
            timerActivity.setFabStop()
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            collapsed_text.text = it.getString(KEY)
            Toast.makeText(context, it.getString(KEY), Toast.LENGTH_SHORT).show()
            Log.i("onViewCreated", it.getString(KEY))
        }
    }

    override fun onResume() {
        super.onResume()
        timerActivity.initializeTimer()

        removeAlarm(context!!)
        NotificationsUtils.hideTimerNotification(context!!)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.i("Attacjh", "Attach")
    }

    override fun onPause() {
        super.onPause()
        Log.i("PAUSE", "PAUSE")
        if (timerActivity.timerState == TimerActivity.TimerState.running) {
            timerActivity.timer!!.cancel()
            val wakeUpTime = setAlarm(context!!, nowSeconds, timerActivity.secondsRemaining)
            NotificationsUtils.showTimerRunning(context!!, wakeUpTime)
        } else if (timerActivity.timerState == TimerActivity.TimerState.paused) {
            NotificationsUtils.showTimerPaused(context!!)
        }

        val argument = Bundle()
        argument.putString(KEY, collapsed_text.text.toString())

        this.arguments = argument


        PrefUtilsTimer.setPreviousTimerLengthSeconds(timerActivity.timerLength, context!!)
        PrefUtilsTimer.setSecondsRemaining(timerActivity.secondsRemaining, context!!)
        PrefUtilsTimer.setTimerState(timerActivity.timerState, context!!)
        PrefUtilsTimer.setActivityId(timerActivity.activityId, context!!)
    }


//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString("dataGotFromServer", collapsed_text.text.toString())
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        if (savedInstanceState != null) {
//            collapsed_text.text = savedInstanceState.getString("dataGotFromServer")
//            Log.i("DATA_LOAD", collapsed_text.text.toString())
//        }
//    }

}
