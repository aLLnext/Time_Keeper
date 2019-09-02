package com.timekeeper.Adapters

import android.app.AlertDialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
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
import android.R.string
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import com.google.android.material.snackbar.Snackbar
import android.R.id
import android.content.Intent
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.timekeeper.Timer.TimerActivity
import kotlinx.android.synthetic.main.bottom_sheet.view.*


class MainViewAdapter(
    val timerActivity: TimerActivity,
    val bottomSheet: CoordinatorLayout,
    val context: Context,
    var activities: ArrayList<Activity>
) : RecyclerView.Adapter<MainViewAdapter.MyViewHolder>() {
    private val viewBinderHelper = ViewBinderHelper()
    private var deletedItem: Activity? = null
    private var deletedPosition: Int? = null

    init {
        viewBinderHelper.setOpenOnlyOne(true)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_activity, viewGroup, false)
        return MyViewHolder(v, timerActivity, bottomSheet)
    }

    override fun getItemCount() = activities.size

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        if (pos >= 0 && pos < activities.size) {
            viewBinderHelper.bind(holder.swipeRevealLayout, activities[pos].id.toString())

            holder.titleAct.text = activities[pos].name

            holder.deleteLayout.setOnClickListener {
                deletedPosition = pos
                deletedItem = activities[pos]
                activities.removeAt(pos)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, itemCount - pos)
                Toast.makeText(context, "$pos+${activities.size}", Toast.LENGTH_SHORT).show()
                showUndoSnackbar(holder.itemView)
            }

            holder.edit_layout.setOnClickListener {
                showPopupWindow(holder.itemView)
            }
        }
    }


    private fun showUndoSnackbar(v: View) {
        val snackbar = Snackbar.make(
            v, R.string.snackbar_title,
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(R.string.snackbar_undo) {
            undoDelete()
        }
        snackbar.show()
    }

    fun showPopupWindow(v: View) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.popup_title)

        val view = LayoutInflater.from(context).inflate(R.layout.popupwindow, null)
        (view.popup_title as TextView).text = v.titleact.text
        builder.setView(view)
        builder.show()
    }

    private fun undoDelete() {
        activities.add(deletedPosition!!, deletedItem!!)
        notifyItemInserted(deletedPosition!!)
        notifyItemRangeChanged(deletedPosition!!, itemCount)
        viewBinderHelper.closeLayout(deletedItem!!.id.toString())
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

    class MyViewHolder(v: View, timerActivity: TimerActivity, bottomSheet: CoordinatorLayout) :
        RecyclerView.ViewHolder(v) {
        val titleAct: TextView = v.titleact
        val fullTime: Chronometer = v.fulltime
        val swipeRevealLayout: SwipeRevealLayout = v.swipe_layout
        val deleteLayout = v.delete_layout
        val edit_layout = v.edit_layout
        private val sheetBehavior = BottomSheetBehavior.from(bottomSheet)

        init {
            v.item_list.setOnClickListener {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheet.collapsed_text.text = v.titleact.text
//                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                val dialogView = inflater.inflate(R.layout.activity_bottom_timer, null)
//                val dialog = BottomSheetDialog(context)
//                dialog.setContentView(dialogView)
//                dialog.show()
                //val intent = Intent(context, TimerActivity::class.java)
                //ContextCompat.startActivity(context, intent, null)
            }

            v.btnplay.setOnClickListener {
                if (timerActivity.timerState == TimerActivity.TimerState.running) {
                    v.btnplay.setImageResource(R.drawable.ic_play_arrow_black_48dp)
                    timerActivity.setFabStop()
                    sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else {
                    v.btnplay.setImageResource(R.drawable.ic_stop_black_48dp)
                    sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    timerActivity.setFabPlay()
                }
            }
        }


    }
}