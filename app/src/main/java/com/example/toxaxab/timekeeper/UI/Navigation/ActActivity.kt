package com.example.toxaxab.timekeeper.UI.Navigation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.toxaxab.timekeeper.Adapters.MainActivityAdapter
import com.example.toxaxab.timekeeper.Model.Supplier
import com.example.toxaxab.timekeeper.R
import kotlinx.android.synthetic.main.fragment_activity.*
import org.jetbrains.anko.find

class ActActivity: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_activity, container, false)
        val layoutManager = LinearLayoutManager(v.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val rv = v!!.find<RecyclerView>(R.id.recyclerView)

        rv.layoutManager = layoutManager

        val adapter = MainActivityAdapter(v.context, Supplier.activities)
        rv.adapter = adapter
        return v
    }
}