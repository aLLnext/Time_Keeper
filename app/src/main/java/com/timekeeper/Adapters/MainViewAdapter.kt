package com.timekeeper.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import kotlinx.android.synthetic.main.item_activity.view.*
import com.timekeeper.R

class MainViewAdapter(
    val context: Context,
    val activities: ArrayList<String>
) : RecyclerView.Adapter<MainViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_activity, viewGroup, false))
    }

    override fun getItemCount() = activities.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.titleAct.text = activities[pos]
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val titleAct: TextView = v.titleact
        val fullTime: Chronometer = v.fulltime
        val currentTime: Chronometer = v.currenttime
    }
}