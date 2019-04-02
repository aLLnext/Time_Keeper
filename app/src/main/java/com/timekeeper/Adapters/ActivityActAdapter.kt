package com.timekeeper.Adapters

import android.content.Context
import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.toxaxab.timekeeper.R
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.MainActivity
import com.timekeeper.Model.ActivityViewModel
import com.timekeeper.UI.Navigation.ActivityTab.ActivityAct
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ActivityActAdapter internal constructor(
        val context: Context,
        val ActActivity: ActivityAct
) : RecyclerView.Adapter<ActivityActAdapter.ActivityViewHolder>() {
    private var activities = emptyList<Activity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityActAdapter.ActivityViewHolder {
        val rootView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ActivityViewHolder(rootView)
    }

    override fun getItemCount() = activities.size

    override fun onBindViewHolder(holder: ActivityActAdapter.ActivityViewHolder, position: Int) {
        val activity = activities[position]
        Log.i("data", activity.name)
        holder.setData(activity)
    }

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentActivity: Activity? = null
        private var job: Job? = null

        init {
            itemView.ivCondition.setOnClickListener {
                when (currentActivity!!.condition) {
                    0 -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                        currentActivity!!.condition = 1
                        //val base = SystemClock.elapsedRealtime() - currentActivity!!.currentTime
                        //currentActivity!!.timerBase = base
                        startTimer(currentActivity)
                        //sendNotification()
                    }

                    1 -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_play)
                        currentActivity!!.condition = 0
                        stopTimer(currentActivity)
                        //cancelNotification(currentActivity)
                    }
                }
            }
        }

        internal fun setData(activity: Activity) {
            with(activity) {
                itemView.txvTitle.text = name
                itemView.txvComment.text = comment
                when (condition) {
                    0 -> {
                        itemView.timer.base = SystemClock.elapsedRealtime() - current_time
                        itemView.ivCondition.setImageResource(R.drawable.ic_play)
                    }
                    1 -> {
                        val sec = (SystemClock.elapsedRealtime() - activity.timer_base) - activity.current_time
                        //Toast.makeText(context, "${sec/1000} seconds", Toast.LENGTH_SHORT).show()
                        activity.current_time += sec
                        itemView.timer.base = SystemClock.elapsedRealtime() - activity.current_time

                        itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                        startTimer(this)
                    }
                }
            }
            this.currentActivity = activity
        }

//        private fun startTimer(activity: Activity?) = runBlocking {
//            job = launch(Dispatchers.IO) {
//                itemView.timer.base = SystemClock.elapsedRealtime() - activity!!.current_time
//                activity.timer_base = itemView.timer.base
//                itemView.timer.start()
//                if (activity.saved == 0) {
//                    ActActivity.update(activity)
//                }
//            }
//        }

        private fun startTimer(activity: Activity?) {
            itemView.timer.base = SystemClock.elapsedRealtime() - activity!!.current_time
            activity.timer_base = itemView.timer.base
            itemView.timer.start()
            if (activity.saved == 0) {
                ActActivity.update(activity)
                //Toast.makeText(context, "Saved start", Toast.LENGTH_SHORT).show()
            }
        }

        private fun stopTimer(activity: Activity?) {
            itemView.timer.stop()
            //job!!.cancel()
            val time = (SystemClock.elapsedRealtime() - activity!!.timer_base) - activity.current_time
            activity.current_time += time
            if (activity.saved == 1) {
                ActActivity.update(activity)
                //Toast.makeText(context, "Saved finish", Toast.LENGTH_SHORT).show()
            }
        }
    }

    internal fun setActivities(activities: List<Activity>) {
        this.activities = activities
        notifyDataSetChanged()
    }
}