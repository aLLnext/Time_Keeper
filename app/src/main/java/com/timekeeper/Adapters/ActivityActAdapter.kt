package com.timekeeper.Adapters

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
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
import com.timekeeper.Adapters.Notifications.SetNotification
import com.timekeeper.UI.Navigation.ActivityTab.ActivityAct
import kotlinx.android.synthetic.main.list_item.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class ActivityActAdapter internal constructor(
        val context: Context,
        val ActActivity: ActivityAct
) : RecyclerView.Adapter<ActivityActAdapter.ActivityViewHolder>() {
    private var activities = emptyList<Activity>()
    var statuses = emptyList<Status>()
    private lateinit var data: ActivityRoomDatabase
    lateinit var holder: ActivityViewHolder
    private lateinit var par: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityActAdapter.ActivityViewHolder {
        val rootView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        par = parent
        holder = ActivityViewHolder(rootView)
        return holder
    }

    override fun getItemCount() = activities.size

    override fun onBindViewHolder(holder: ActivityActAdapter.ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.itemView.id = activity.id
        holder.setData(activity)
    }

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mNotifyManager: NotificationManager by lazy {
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        private var currentActivity: Activity? = null
        private var currentStatus: Status? = null
        private var setNotify: SetNotification? = SetNotification(context, mNotifyManager)

        init {
            setNotify!!.createNotificationChannel()
            itemView.ivCondition.setOnClickListener {
                when (currentStatus!!.condition) {
                    0 -> startTimer(currentActivity!!, currentStatus)
                    1 -> stopTimer(currentStatus)
                }
            }
        }

        private fun getData(activity: Activity) = data.statusDao().getStatus(activity.statusId)

        internal fun setData(activity: Activity) {
            doAsync {
                statuses = data.statusDao().getAllStatuses()
                uiThread {
                    with(activity) {
                        val status = statuses[activity.id]
                        itemView.txvTitle.text = name
                        itemView.txvComment.text = comment
                        when (status.condition) {
                            0 -> {
                                itemView.timer.base = SystemClock.elapsedRealtime() - status.current_time
                                itemView.ivCondition.setImageResource(R.drawable.ic_play)
                            }
                            1 -> {
                                val time = Calendar.getInstance().timeInMillis - status.timer_base
                                status.current_time += time
                                startTimer(activity, status)
                            }
                        }
                        this@ActivityViewHolder.currentActivity = activity
                        this@ActivityViewHolder.currentStatus = status
                    }
                }
            }
        }

        private fun startTimer(activity: Activity, status: Status?) {
            itemView.ivCondition.setImageResource(R.drawable.ic_stop)
            status!!.condition = 1

            itemView.timer.base = SystemClock.elapsedRealtime() - status.current_time
            status.timer_base = Calendar.getInstance().timeInMillis
            itemView.timer.start()
            setNotify!!.sendNotification(activity, status, itemView.id)
            ActActivity.updateStatus(status)

        }

        private fun stopTimer(status: Status?) {
            itemView.ivCondition.setImageResource(R.drawable.ic_play)
            currentStatus!!.condition = 0

            itemView.timer.stop()
            setNotify!!.cancelNotification(currentActivity!!.id)
            val time = Calendar.getInstance().timeInMillis - status!!.timer_base
            status.current_time += time
            ActActivity.updateStatus(status)
        }

        fun stopTimer(id: Int, status: Status?) {
            val itemView = par.findViewById<View>(id)
            itemView.ivCondition.setImageResource(R.drawable.ic_play)
            currentStatus!!.condition = 0
            itemView.timer.stop()
            setNotify!!.cancelNotification(id)
            val time = Calendar.getInstance().timeInMillis - status!!.timer_base
            status.current_time += time
            ActActivity.updateStatus(status)
        }
    }

    internal fun setActivities(activities: List<Activity>, DB: ActivityRoomDatabase) {
        this.activities = activities
        this.data = DB
        notifyDataSetChanged()
    }
}