package com.timekeeper.UI.Navigation.StatisticsTab

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.toxaxab.timekeeper.R
import com.timekeeper.Adapters.StatisticsActAdapter
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.Model.ActivityViewModel

class StatisticsAct : Fragment() {
    //private lateinit var activityViewModel: ActivityViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_statistics, container, false)
        val recycleView = v.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = StatisticsActAdapter(v.context)
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(v.context)
        //activityViewModel = ViewModelProviders.of(this).get(ActivityViewModel::class.java)
        //activityViewModel.allActivity.observe(this, Observer { acts ->
        //    acts?.let { adapter.setData(it) }
        //})
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun insertInto(activity: Activity){
        //activityViewModel.insert(activity)
    }

    companion object {
        const val newActivityRequestCode = 1
    }
}