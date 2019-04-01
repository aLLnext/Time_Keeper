package com.timekeeper.Adapters

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.Toast
import com.example.toxaxab.timekeeper.R
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.MainActivity
import com.timekeeper.Model.Condition
import com.timekeeper.Model.SetNotification
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ActAdapter internal constructor(
        val context: Context,
        val main: MainActivity
) : RecyclerView.Adapter<ActAdapter.ActivityViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var activities = emptyList<Activity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActAdapter.ActivityViewHolder {
        val rootView = inflater.inflate(R.layout.list_item, parent, false)
        return ActivityViewHolder(rootView)
    }

    override fun getItemCount() = activities.size

    override fun onBindViewHolder(holder: ActAdapter.ActivityViewHolder, position: Int) {
        holder.setActivity(activities[position], position)
    }

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentActivity: Activity? = null
        private var currentPosition: Int = 0
        private var job: Job? = null
        private var mNotifyManager: NotificationManager? = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        private var notifyManager: SetNotification? = null
        //private val NOTIFICATION_ID = 0 //????

        init {
            notifyManager = SetNotification(context, mNotifyManager)
            //mNotifyManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            notifyManager!!.createNotificationChannel()
            itemView.setOnClickListener {
                Toast.makeText(context, currentActivity!!.name + " clicked", Toast.LENGTH_SHORT).show()
            }

            itemView.ivCondition.setOnClickListener {
                when (currentActivity!!.condition) {
                    1 -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                        currentActivity!!.condition = 0
                        //val base = SystemClock.elapsedRealtime() - currentActivity!!.currentTime
                        //currentActivity!!.timerBase = base
                        startTimer(currentActivity)
                        notifyManager!!.sendNotification(currentActivity)
                    }

                    0 -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_play)
                        currentActivity!!.condition = 1
                        stopTimer(currentActivity)
                        notifyManager!!.cancelNotification(currentActivity)
                    }
                }
            }
        }

        fun setActivity(myActivity: Activity?, pos: Int) {
            with(myActivity!!) {
                itemView.txvTitle.text = name
                itemView.txvComment.text = comment
                itemView.timer.base = SystemClock.elapsedRealtime() - current_time
                when (condition) {
                    0 -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                        //val base = SystemClock.elapsedRealtime() - myActivity.currentTime
                        //myActivity.timerBase = base
                        //saveData()
                        startTimer(this)
                    }
                    1 ->
                        itemView.ivCondition.setImageResource(R.drawable.ic_play)
                }
            }

            this.currentActivity = myActivity
            this.currentPosition = pos
        }

        private fun startTimer(activity: Activity?) {
            itemView.timer.base = SystemClock.elapsedRealtime() - activity!!.current_time
            activity.timer_base = itemView.timer.base
            //main.connecting(activity)
            itemView.timer.start()
        }

        /*private fun startTimer(activity: Activity?) = runBlocking {
            job = launch(Dispatchers.IO) {
                itemView.timer.base = SystemClock.elapsedRealtime() - activity!!.current_time
                activity.timer_base = itemView.timer.base
                itemView.timer.start()
            }
        }*/

        private fun stopTimer(activity: Activity?) {
            itemView.timer.stop()
            //job!!.cancel()
            val time = (SystemClock.elapsedRealtime() - activity!!.timer_base) - activity.current_time
            activity.current_time += time
            val sec = time / 1000
            Toast.makeText(context, "$sec seconds", Toast.LENGTH_SHORT).show()
        }
    }

    /*inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //private var mNotifyManager: NotificationManager? = null
        //private val manageNotification: SetNotification = SetNotification(itemView.context)
        private var currentActivity: Activity? = null
        private var currentPosition: Int = 0
        private var manageNotification: SetNotification? = null

        init {
            manageNotification = SetNotification(itemView.context)
            manageNotification!!.createNotification()
            itemView.ivCondition.setOnClickListener {
                when (currentActivity!!.condition) {
                    1 -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                        currentActivity!!.condition = 0
                        //val base = SystemClock.elapsedRealtime() - currentActivity!!.currentTime
                        //currentActivity!!.timerBase = base
                        startTimer(currentActivity)
                        manageNotification!!.sendNotification(currentActivity)
                    }

                    0 -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_play)
                        currentActivity!!.condition = 1
                        stopTimer(currentActivity)
                        manageNotification!!.cancelNotification(currentActivity)
                    }
                }
            }
        }

        fun setActivity(myActivity: Activity?, pos: Int) {
            with(myActivity!!) {
                itemView.txvTitle.text = name
                itemView.txvComment.text = comment
                itemView.timer.base = SystemClock.elapsedRealtime() - current_time
                when (condition) {
                    0 -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                        //val base = SystemClock.elapsedRealtime() - myActivity.currentTime
                        //myActivity.timerBase = base
                        //saveData()
                        //startTimer(this)
                    }
                    1 ->
                        itemView.ivCondition.setImageResource(R.drawable.ic_play)
                }
            }

            this.currentActivity = myActivity
            this.currentPosition = pos
        }

        private fun startTimer(activity: Activity?){
            itemView.timer.base = SystemClock.elapsedRealtime() - activity!!.current_time
            activity.timer_base = itemView.timer.base
            main.save(activity)
            itemView.timer.start()
        }

        private fun stopTimer(activity: Activity?) {
            itemView.timer.stop()
            //job!!.cancel()
            val time = (SystemClock.elapsedRealtime() - activity!!.timer_base) - activity.current_time
            activity.current_time += time
            main.save(activity)
            //val sec = time / 1000
            //Toast.makeText(context, "$sec seconds", Toast.LENGTH_SHORT).show()
        }
    }*/

    internal fun setData(activities: List<Activity>) {
        this.activities = activities
        notifyDataSetChanged()
    }
}