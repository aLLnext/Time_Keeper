package com.timekeeper.UI.Navigation.ActivityTab

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
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

class ActivityAct : Fragment() {
    private lateinit var activityViewModel: ActivityViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_activity, container, false)
        val layoutManager = LinearLayoutManager(rootView.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rootView.recyclerView.layoutManager = layoutManager
        val adapter = ActivityActAdapter(rootView.context, this)
        rootView.recyclerView.adapter = adapter

        activityViewModel = ViewModelProviders.of(this).get(ActivityViewModel::class.java)

        activityViewModel.allActivity.observe(this, Observer { acts ->
            acts?.let {
                adapter.setActivities(it, activityViewModel.DB)
            }
        })
        return rootView
    }

    override fun onPause() {
        //saveAll()
        super.onPause()
    }

    fun updateStatus(status: Status){
        activityViewModel.updateStatus(status)
    }

    /*fun update(activity: Activity) {
        activityViewModel.update(activity)
    }*/

    fun insert(activity: Activity) {
        activityViewModel.insert(activity)
    }

    fun saveAll(){
        for (act in activityViewModel.allActivity.value!!){
            activityViewModel.insert(act)
        }
    }
}