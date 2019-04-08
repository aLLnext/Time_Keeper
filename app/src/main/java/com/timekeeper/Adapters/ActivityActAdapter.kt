package com.timekeeper.Adapters

import android.app.NotificationManager
import android.content.Context
import android.os.SystemClock
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.toxaxab.timekeeper.R
import com.timekeeper.Database.ActivityRoomDatabase
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.Database.Entity.Status
import com.timekeeper.Database.Repository.ActivityRepository
import com.timekeeper.MainActivity
import com.timekeeper.Model.ActivityViewModel
import com.timekeeper.Model.SetNotification
import com.timekeeper.UI.Navigation.ActivityTab.ActivityAct
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync

class ActivityActAdapter internal constructor(
        val context: Context,
        val ActActivity: ActivityAct
) : RecyclerView.Adapter<ActivityActAdapter.ActivityViewHolder>() {
    private var activities = emptyList<Activity>()
    private lateinit var repository: ActivityRepository
    private lateinit var data: ActivityRoomDatabase

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
        private var currentStatus: Status? = null
        private var mNotifyManager: NotificationManager? = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        private var setNotify: SetNotification? = SetNotification(context, mNotifyManager)

        init {
            //setNotify!!.createNotificationChannel()
            itemView.ivCondition.setOnClickListener {
                when (currentStatus!!.condition) {
                    0 -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                        currentStatus!!.condition = 1
                        //val base = SystemClock.elapsedRealtime() - currentActivity!!.currentTime
                        //currentActivity!!.timerBase = base
                        startTimer(currentActivity, currentStatus)
                        //setNotify!!.sendNotification()
                    }

                    1 -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_play)
                        currentStatus!!.condition = 0
                        stopTimer(currentActivity, currentStatus)
                        //setNotify!!.cancelNotification(currentActivity)
                    }
                }
            }
        }

        internal fun setData(activity: Activity) {
            doAsync {
                val status = data.statusDao().getStatus(activity.statusId)
                with(activity) {
                    itemView.txvTitle.text = name
                    itemView.txvComment.text = comment
                    when (status.condition) {
                        0 -> {
                            itemView.timer.base = SystemClock.elapsedRealtime() - status.current_time
                            itemView.ivCondition.setImageResource(R.drawable.ic_play)
                        }
                        1 -> {
                            val sec = (SystemClock.elapsedRealtime() - status.timer_base) - status.current_time
                            //Toast.makeText(context, "${sec/1000} seconds", Toast.LENGTH_SHORT).show()
                            status.current_time += sec
                            itemView.timer.base = SystemClock.elapsedRealtime() - status.current_time

                            itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                            startTimer(this, status)
                        }
                    }
                }
                this@ActivityViewHolder.currentActivity = activity
                this@ActivityViewHolder.currentStatus = status
            }
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

        private fun startTimer(activity: Activity?, status: Status?) {
            itemView.timer.base = SystemClock.elapsedRealtime() - status!!.current_time
            status.timer_base = itemView.timer.base
            itemView.timer.start()
            ActActivity.updateStatus(status)
            //Toast.makeText(context, "Saved start", Toast.LENGTH_SHORT).show()
        }

        private fun stopTimer(activity: Activity?, status: Status?) {
            itemView.timer.stop()
            //job!!.cancel()
            val time = (SystemClock.elapsedRealtime() - status!!.timer_base) - status.current_time
            status.current_time += time
            ActActivity.updateStatus(status)
            //Toast.makeText(context, "Saved finish", Toast.LENGTH_SHORT).show()
        }
    }

    internal fun setActivities(activities: List<Activity>, DB: ActivityRoomDatabase) {
        this.activities = activities
        this.data = DB
        notifyDataSetChanged()
    }
}