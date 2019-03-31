package com.timekeeper.Adapters

import android.app.*
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.timekeeper.Model.Condition
import com.timekeeper.Model.MyActivity
import com.example.toxaxab.timekeeper.R
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.coroutines.*
import android.widget.RemoteViews
import com.timekeeper.MainActivity

import android.support.v4.app.NotificationCompat


class ActivityActAdapter(val main: MainActivity, val context: Context, private val myActivities: List<MyActivity>, val data: SQLiteDatabase?) : RecyclerView.Adapter<ActivityActAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myActivities.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myActivity = myActivities[position]
        holder.setData(myActivity, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentActivity: MyActivity? = null
        private var currentPosition: Int = 0
        private var job: Job? = null
        private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        private var mNotifyManager: NotificationManager? = null
        //private val NOTIFICATION_ID = 0 //????

        init {
            createNotificationChannel()
            itemView.setOnClickListener {
                Toast.makeText(context, currentActivity!!.name + " clicked", Toast.LENGTH_SHORT).show()
            }

            itemView.ivCondition.setOnClickListener {
                when (currentActivity!!.condition) {
                    Condition.INACTIVE -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                        currentActivity!!.condition = Condition.ACTIVE
                        //val base = SystemClock.elapsedRealtime() - currentActivity!!.currentTime
                        //currentActivity!!.timerBase = base
                        startTimer(currentActivity)
                        sendNotification()
                    }

                    Condition.ACTIVE -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_play)
                        currentActivity!!.condition = Condition.INACTIVE
                        stopTimer(currentActivity)
                        cancelNotification(currentActivity)
                    }
                }
            }
        }

        fun setData(myActivity: MyActivity?, pos: Int) {
            with(myActivity!!) {
                itemView.txvTitle.text = name
                itemView.txvComment.text = comment
                itemView.timer.base = SystemClock.elapsedRealtime() - currentTime
                when (condition) {
                    Condition.ACTIVE -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                        //val base = SystemClock.elapsedRealtime() - myActivity.currentTime
                        //myActivity.timerBase = base
                        //saveData()
                        startTimer(this)
                    }
                    Condition.INACTIVE ->
                        itemView.ivCondition.setImageResource(R.drawable.ic_play)
                }
            }

            this.currentActivity = myActivity
            this.currentPosition = pos
        }

        private fun startTimer(activity: MyActivity?){
            itemView.timer.base = SystemClock.elapsedRealtime() - activity!!.currentTime
            activity.timerBase = itemView.timer.base
            main.connecting(activity)
            itemView.timer.start()
        }

//        private fun startTimer(activity: MyActivity?) = runBlocking {
//            job = launch(Dispatchers.IO) {
//                itemView.timer.base = SystemClock.elapsedRealtime() - activity!!.currentTime
//                activity.timerBase = itemView.timer.base
//                itemView.timer.start()
//            }
//        }

        private fun stopTimer(activity: MyActivity?) {
            itemView.timer.stop()
            //job!!.cancel()
            val time = (SystemClock.elapsedRealtime() - activity!!.timerBase) - activity.currentTime
            activity.currentTime += time
            val sec = time / 1000
            Toast.makeText(context, "$sec seconds", Toast.LENGTH_SHORT).show()
        }

        private fun sendNotification() {
            val notifyBuilder = getNotificationBuilder()
            mNotifyManager!!.notify(currentActivity!!.id, notifyBuilder.build())
        }

        private fun createNotificationChannel() {
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

        private fun getNotificationIntent(): Intent {
            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.putExtra("activity", currentActivity!!.id)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            return notificationIntent
        }

        private fun getNotificationBuilder(): NotificationCompat.Builder {
            val notificationIntent = getNotificationIntent()
            notificationIntent.putExtra("start", SystemClock.elapsedRealtime() - currentActivity!!.currentTime)

            val notificationPendingIntent = PendingIntent.getActivity(context,
                    0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val notificationStopIntent = getNotificationIntent()
            notificationStopIntent.action = "STOP"
            val notificationPendingStop = PendingIntent.getActivity(context,
                    0, notificationStopIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val remoteViews = RemoteViews(context.packageName, R.layout.notification)
            remoteViews.setTextViewText(R.id.textView, currentActivity!!.name)
            remoteViews.setChronometer(R.id.timer, SystemClock.elapsedRealtime() - currentActivity!!.currentTime, "%s", true)
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

        private fun cancelNotification(activity: MyActivity?) {
            mNotifyManager?.cancel(activity!!.id)
        }
    }
}