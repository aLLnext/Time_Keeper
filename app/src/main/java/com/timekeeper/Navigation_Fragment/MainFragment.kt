package com.timekeeper.Navigation_Fragment

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timekeeper.Adapters.MainViewAdapter
import com.timekeeper.NavigationActivity
import com.timekeeper.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainFragment : Fragment() {

    //private val activity = this.getActivity() as NavigationActivity

    var activities: ArrayList<String> = arrayListOf("sleep", "code", "repeat", "another thing", "else", "again")
    lateinit var v: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.activity_main, null)

        val MLight = Typeface.createFromAsset(activity!!.assets, "fonts/Montserrat-Light.ttf")
        val MMedium = Typeface.createFromAsset(activity!!.assets, "fonts/Montserrat-Medium.ttf")
        v.titlepage!!.typeface = MMedium
        v.subtitle.typeface = MLight
        v.endpage.typeface = MLight
        val adapter = MainViewAdapter(this.context!!, activities)
        v.recycle_activity.layoutManager = LinearLayoutManager(activity)
        v.recycle_activity.adapter = adapter
        adapter.notifyDataSetChanged()
        return v
    }
}
