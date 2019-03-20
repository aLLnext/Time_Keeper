package com.example.toxaxab.timekeeper.Adapters

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.toxaxab.timekeeper.Model.Condition
import com.example.toxaxab.timekeeper.Model.MyActivity
import com.example.toxaxab.timekeeper.R
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.coroutines.*
import java.time.Duration
import android.support.v4.content.ContextCompat.getSystemService
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.graphics.Color
import android.os.Build
import android.provider.Settings.Global.getString
import android.widget.RemoteViews
import com.example.toxaxab.timekeeper.MainActivity
import android.support.v4.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.fragment_activity.view.*


class MainActivityAdapter(val context: Context, val myActivities: List<MyActivity>) : RecyclerView.Adapter<MainActivityAdapter.MyViewHolder>() {
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
        private lateinit var job: Job
        private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        private var mNotifyManager: NotificationManager? = null
        private val NOTIFICATION_ID = 0

        init {
            itemView.setOnClickListener {
                Toast.makeText(context, currentActivity!!.name + " clicked", Toast.LENGTH_SHORT).show()
            }

            itemView.ivCondition.setOnClickListener {
                when (currentActivity!!.condition) {
                    Condition.INACTIVE -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                        currentActivity!!.condition = Condition.ACTIVE
                        startTimer(currentActivity)
                        createNotificationChannel()
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
            itemView.txvTitle.text = myActivity!!.name
            itemView.txvComment.text = myActivity.comment
            itemView.timer.setEms(100)
            itemView.ivCondition.setImageResource(R.drawable.ic_play)
            //if (myActivity.condition == Condition.INACTIVE)
            this.currentActivity = myActivity
            this.currentPosition = pos
        }

        private fun startTimer(activity: MyActivity?) = runBlocking {
            job = launch(Dispatchers.IO) {
                itemView.timer.base = SystemClock.elapsedRealtime() - activity!!.currentTime
                itemView.timer.start()
                //sendNotification()
                //startTimer(currentActivity)
            }
        }

        private fun stopTimer(activity: MyActivity?) {
            itemView.timer.stop()
            job.cancel()
            val time = (SystemClock.elapsedRealtime() - itemView.timer.base) - activity!!.currentTime
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

        private fun getNotificationBuilder(): NotificationCompat.Builder {
            val notificationIntent = Intent(context, MainActivity::class.java)
            val notificationPendingIntent = PendingIntent.getActivity(context,
                    NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            return NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                    .setContentTitle("You've been notified!")
                    .setContentText(currentActivity!!.name + "Start")
                    .setSmallIcon(R.drawable.ic_play)
                    .setContentIntent(notificationPendingIntent)
                    .setOngoing(true)
        }

        private fun cancelNotification(activity: MyActivity?){
            mNotifyManager?.cancel(activity!!.id)
        }

        /*private fun sendNotification(){
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val notification = Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_play)
                    .setContentTitle("Test")
                    .setContentText("text")
            notification.setContentIntent(pendingIntent)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification.build())
        }*/
    }
}