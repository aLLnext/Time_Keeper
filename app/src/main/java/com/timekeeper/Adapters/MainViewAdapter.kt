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
import android.util.Log
import com.timekeeper.Data.Activity
import com.google.android.material.snackbar.Snackbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.timekeeper.Timer.TimerActivity
import com.timekeeper.Utils.CONSTANTS
import kotlinx.android.synthetic.main.activity_timer.view.*


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

            val id = activities[pos].id
            holder.titleAct.text = activities[pos].name

            if (timerActivity.timerState == TimerActivity.TimerState.stopped) {
                timerActivity.activityId = id
                holder.btnPlay.setImageResource(R.drawable.ic_play_arrow_black_48dp)
            } else if (id == timerActivity.activityId) {
                if (timerActivity.timerState == TimerActivity.TimerState.paused) {
                    Log.i("name", activities[pos].name)
                    holder.btnPlay.setImageResource(R.drawable.ic_pause_black_48dp)
                } else if (timerActivity.timerState == TimerActivity.TimerState.running)
                    holder.btnPlay.setImageResource(R.drawable.ic_stop_black_48dp)
            } else {
                holder.btnPlay.setImageResource(R.drawable.ic_play_arrow_black_48dp)
            }

            holder.deleteLayout.setOnClickListener {
                deletedPosition = pos
                deletedItem = activities[pos]
                activities.removeAt(pos)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, itemCount - pos)
                Toast.makeText(context, "$pos+${activities.size}", Toast.LENGTH_SHORT).show()
                showUndoSnackbar(holder.itemView)
            }

            holder.editLayout.setOnClickListener {
                showPopupWindow(holder.itemView)
            }


            holder.btnPlay.setOnClickListener {
                if (timerActivity.timerState == TimerActivity.TimerState.stopped)
                    timerActivity.activityId = id
                Log.i("id", id.toString())
                Log.i("btnPLay", timerActivity.activityId.toString())
                if (timerActivity.activityId == id || timerActivity.timerState == TimerActivity.TimerState.stopped) {
                    bottomSheet.collapsed_text.text = holder.itemView.titleact.text
                    if (timerActivity.timerState == TimerActivity.TimerState.running) {
                        holder.btnPlay.setImageResource(R.drawable.ic_play_arrow_black_48dp)
                        timerActivity.setFabStop()
                        holder.sheetBehavior.isHideable = true
                        holder.sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        //holder.fullTime.start()
                    } else {
                        holder.btnPlay.setImageResource(R.drawable.ic_stop_black_48dp)
                        holder.sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        timerActivity.setFabPlay()
                        holder.sheetBehavior.isHideable = false
                    }
                }
            }

            holder.itemList.setOnClickListener {
                Log.i("ID", timerActivity.timerState.toString())
                if (timerActivity.timerState == TimerActivity.TimerState.stopped)
                    timerActivity.activityId = id
                Toast.makeText(context, id.toString(), Toast.LENGTH_SHORT).show()
                if (timerActivity.activityId == id || timerActivity.timerState == TimerActivity.TimerState.stopped) {
                    bottomSheet.collapsed_text.text = holder.itemView.titleact.text
                    //timerActivity.current_view_id = holder.itemView.id

                    if (timerActivity.timerState != TimerActivity.TimerState.running) {
                        if (holder.sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                            holder.sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        } else
                            holder.sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        //holder.fullTime.start()
                    }
                }
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
        outState!!.putString("activityName", bottomSheet.collapsed_text.text.toString())
        viewBinderHelper.saveStates(outState)
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in [android.app.Activity.onRestoreInstanceState]
     */
    fun restoreStates(inState: Bundle?) {
        bottomSheet.collapsed_text.text = inState!!.getString("activityName")
        Log.i("TEST_DATA", bottomSheet.collapsed_text.text.toString())
        viewBinderHelper.restoreStates(inState)
    }

    class MyViewHolder(v: View, timerActivity: TimerActivity, bottomSheet: CoordinatorLayout) :
        RecyclerView.ViewHolder(v) {
        val titleAct: TextView = v.titleact
        val fullTime: Chronometer = v.fulltime
        val swipeRevealLayout: SwipeRevealLayout = v.swipe_layout
        val deleteLayout = v.delete_layout
        val editLayout = v.edit_layout
        val btnPlay = v.btnplay
        val sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        val itemList = v.item_list

//        init {
//            v.item_list.setOnClickListener {
//                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
////                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
////                val dialogView = inflater.inflate(R.layout.activity_bottom_timer, null)
////                val dialog = BottomSheetDialog(context)
////                dialog.setContentView(dialogView)
////                dialog.show()
//                //val intent = Intent(context, TimerActivity::class.java)
//                //ContextCompat.startActivity(context, intent, null)
//            }
//        }


    }
}