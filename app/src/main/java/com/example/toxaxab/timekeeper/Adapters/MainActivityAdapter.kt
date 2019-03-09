package com.example.toxaxab.timekeeper.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.toxaxab.timekeeper.Model.MyActivity
import com.example.toxaxab.timekeeper.R
import kotlinx.android.synthetic.main.list_item.view.*

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
        fun setData(myActivity: MyActivity?, pos: Int) {
            itemView.txvTitle.text = myActivity!!.name
        }
    }
}