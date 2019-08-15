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
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import kotlinx.android.synthetic.main.item_activity.view.*
import com.timekeeper.R
import kotlinx.android.synthetic.main.popupwindow.view.*
import android.os.Bundle
import com.timekeeper.Data.Activity


class MainViewAdapter(
    val context: Context,
    var activities: ArrayList<Activity>
) : RecyclerView.Adapter<MainViewAdapter.MyViewHolder>() {
    private val viewBinderHelper = ViewBinderHelper()

    init {
        viewBinderHelper.setOpenOnlyOne(true)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_activity, viewGroup, false)
        return MyViewHolder(v, context)
    }

    override fun getItemCount() = activities.size

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        if (pos >= 0 && pos < activities.size) {
            viewBinderHelper.bind(holder.swipeRevealLayout, activities[pos].id.toString())

            holder.titleAct.text = activities[pos].name
            holder.deleteLayout.setOnClickListener {
                activities.remove(activities[pos])
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, itemCount - pos)
                Toast.makeText(context, "$pos+${activities.size}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveStates(outState: Bundle?) {
        viewBinderHelper.saveStates(outState)
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in [android.app.Activity.onRestoreInstanceState]
     */
    fun restoreStates(inState: Bundle?) {
        viewBinderHelper.restoreStates(inState)
    }


    class MyViewHolder(v: View, private val context: Context) :
        RecyclerView.ViewHolder(v) {
        val titleAct: TextView = v.titleact
        val fullTime: Chronometer = v.fulltime
        val swipeRevealLayout: SwipeRevealLayout = v.swipe_layout
        val deleteLayout = v.delete_layout

        init {

            v.setOnLongClickListener {
                showPopupWindow()
                true
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
            view.btndelete.setOnClickListener {
                Toast.makeText(context, "DELETE", Toast.LENGTH_SHORT).show()
            }
            builder.setView(view)
            builder.show()
        }


    }
}