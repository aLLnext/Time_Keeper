package com.example.toxaxab.timekeeper.Adapters

import android.content.Context
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
        var currentActivity: MyActivity? = null
        var currentPosition: Int = 0
        private lateinit var job: Job

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
                    }

                    Condition.ACTIVE -> {
                        itemView.ivCondition.setImageResource(R.drawable.ic_play)
                        currentActivity!!.condition = Condition.INACTIVE
                        stopTimer(currentActivity)
                    }
                }

            }
        }

        fun setData(myActivity: MyActivity?, pos: Int) {
            itemView.txvTitle.text = myActivity!!.name
            itemView.txvComment.text = myActivity.comment
            itemView.timer.setEms(100)
            itemView.ivCondition.setImageResource(R.drawable.ic_play)
            if (myActivity.condition == Condition.INACTIVE)
                this.currentActivity = myActivity
            this.currentPosition = pos
        }

        fun startTimer(activity: MyActivity?) = runBlocking {
            job = launch(Dispatchers.IO) {
                itemView.timer.base = SystemClock.elapsedRealtime() - activity!!.currentTime
                itemView.timer.start()
                //startTimer(currentActivity)

            }

        }

        fun stopTimer(activity: MyActivity?) {
            if (activity!!.condition == Condition.INACTIVE) {
                itemView.timer.stop()

                job.cancel()
                val time = (SystemClock.elapsedRealtime() - itemView.timer.base) - activity.currentTime
                activity.currentTime += time
                val sec = time / 1000
                Toast.makeText(context, "$sec seconds", Toast.LENGTH_SHORT).show()
            }
        }
    }
}