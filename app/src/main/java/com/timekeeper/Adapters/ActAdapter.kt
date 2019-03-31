package com.timekeeper.Adapters

import android.content.Context
import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.toxaxab.timekeeper.R
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.Model.Condition
import com.timekeeper.Model.MyActivity
import com.timekeeper.Model.SetNotification
import kotlinx.android.synthetic.main.list_item.view.*

class ActAdapter internal constructor(
        val context: Context
) : RecyclerView.Adapter<ActAdapter.ActivityViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var activities = emptyList<Activity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActAdapter.ActivityViewHolder {
        val rootView = inflater.inflate(R.layout.list_item, parent, false)
        return ActivityViewHolder(rootView)
    }

    override fun getItemCount() = activities.size

    override fun onBindViewHolder(holder: ActAdapter.ActivityViewHolder, position: Int) {
        holder.setActivity(activities[position].fromDB(), position)
    }

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val manageNotification: SetNotification = SetNotification(itemView.context)
        private var currentActivity: MyActivity? = null
        private var currentPosition: Int = 0

        init {
            itemView.ivCondition.setOnClickListener {
                when (currentActivity!!.condition) {
                    Condition.INACTIVE -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_stop)
                        currentActivity!!.condition = Condition.ACTIVE
                        //val base = SystemClock.elapsedRealtime() - currentActivity!!.currentTime
                        //currentActivity!!.timerBase = base
                        startTimer(currentActivity)
                        manageNotification.sendNotification(currentActivity)
                    }

                    Condition.ACTIVE -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_play)
                        currentActivity!!.condition = Condition.INACTIVE
                        stopTimer(currentActivity)
                        manageNotification.cancelNotification(currentActivity)
                    }
                }
            }
        }

        fun setActivity(myActivity: MyActivity?, pos: Int) {
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
            //main.connecting(activity)
            itemView.timer.start()
        }

        private fun stopTimer(activity: MyActivity?) {
            itemView.timer.stop()
            //job!!.cancel()
            val time = (SystemClock.elapsedRealtime() - activity!!.timerBase) - activity.currentTime
            activity.currentTime += time
            //val sec = time / 1000
            //Toast.makeText(context, "$sec seconds", Toast.LENGTH_SHORT).show()
        }
    }

    internal fun setData(activities: List<MyActivity>) {
        var tmp = mutableListOf<Activity>()
        for(elem in activities){
            tmp.add(elem.toDB())
        }
        this.activities = tmp
        notifyDataSetChanged()
    }
}