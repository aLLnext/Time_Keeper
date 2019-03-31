package com.timekeeper.Adapters

import android.content.Context
import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import com.example.toxaxab.timekeeper.R
import com.timekeeper.Database.Entity.Activity

class StatisticsActAdapter internal constructor(
        context: Context
) : RecyclerView.Adapter<StatisticsActAdapter.ActivityViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var activities = emptyList<Activity>()

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cur_name = itemView.findViewById<TextView>(R.id.name)
        val cur_timerbase = itemView.findViewById<Chronometer>(R.id.timer_start)
        val cur_time = itemView.findViewById<Chronometer>(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val itemView = inflater.inflate(R.layout.stat_act_list_item, parent, false)
        return ActivityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val current = activities[position]
        holder.cur_name.text = current.name
        holder.cur_timerbase.base = (SystemClock.elapsedRealtime() - current.current_time)
        holder.cur_time.base = ((SystemClock.elapsedRealtime() - current.timer_base) - current.current_time)
    }

    internal fun setData(activities: List<Activity>) {
        this.activities = activities
        notifyDataSetChanged()
    }

    override fun getItemCount() = activities.size
}