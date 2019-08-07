package com.timekeeper.Adapters

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.item_activity.view.*
import com.timekeeper.R
import kotlinx.android.synthetic.main.popupwindow.view.*

class MainViewAdapter(
    val context: Context,
    val activities: ArrayList<String>
) : RecyclerView.Adapter<MainViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_activity, viewGroup, false), context)
    }

    override fun getItemCount() = activities.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.titleAct.text = activities[pos]
    }


    class ViewHolder(v: View, private val context: Context) : RecyclerView.ViewHolder(v) {
        val titleAct: TextView = v.titleact
        val fullTime: Chronometer = v.fulltime

        init {
            v.setOnClickListener {
                showPopupWindow()
            }

            v.btnplay.setOnClickListener {
                v.btnplay.setImageResource(R.drawable.ic_stop_black_48dp)
            }
        }

        private fun showPopupWindow() {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.popup_title)

            val view = LayoutInflater.from(context).inflate(R.layout.popupwindow, null)
            (view.popup_title as TextView).text = titleAct.text
            view.btndelete.setOnClickListener{
                Toast.makeText(context, "DELETE", Toast.LENGTH_SHORT).show()
            }
            builder.setView(view)
            builder.show()
        }

    }
}