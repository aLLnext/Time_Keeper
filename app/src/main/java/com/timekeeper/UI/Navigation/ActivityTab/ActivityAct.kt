package com.timekeeper.UI.Navigation.ActivityTab

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.toxaxab.timekeeper.R
import com.timekeeper.Adapters.ActivityActAdapter
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.Database.Entity.Status
import com.timekeeper.MainActivity
import com.timekeeper.Model.ActivityViewModel
import kotlinx.android.synthetic.main.fragment_activity.view.*
import android.arch.persistence.room.ForeignKey.NO_ACTION
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*
import android.content.IntentFilter


class ActivityAct : Fragment() {
    internal lateinit var activityViewModel: ActivityViewModel
    internal lateinit var adapter: ActivityActAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_activity, container, false)
        val layoutManager = LinearLayoutManager(rootView.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rootView.recyclerView.layoutManager = layoutManager

        adapter = ActivityActAdapter(rootView.context, this)
        rootView.recyclerView.adapter = adapter

        activityViewModel = ViewModelProviders.of(this).get(ActivityViewModel::class.java)

        activityViewModel.allActivity.observe(this, Observer { acts ->
            acts?.let {
                adapter.setActivities(it, activityViewModel.DB)
            }
        })
        return rootView
    }


    fun updateStatus(status: Status) {
        activityViewModel.updateStatus(status)
    }

    fun updateActivity(activity: Activity) {
        activityViewModel.updateActivity(activity)
    }

    fun insert(status: Status, activity: Activity) {
        activityViewModel.insert(status, activity)
    }

}