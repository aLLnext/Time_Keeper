package com.timekeeper.Fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timekeeper.Adapters.MainViewAdapter
import com.timekeeper.Data.Activity
import com.timekeeper.R
import kotlinx.android.synthetic.main.activity_main.view.*

class MainFragment : Fragment() {

    //private val activity = this.getActivity() as NavigationActivity

    var activities: ArrayList<Activity> = arrayListOf(
        Activity(0, "sleep"), Activity(1, "code"),
        Activity(2, "repeat"), Activity(3, "another thing"), Activity(4, "else"), Activity(5, "again")
    )
    private lateinit var v: View

    var adapter: MainViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.activity_main, null)
        val MLight = Typeface.createFromAsset(activity!!.assets, "fonts/Montserrat-Light.ttf")
        val MMedium = Typeface.createFromAsset(activity!!.assets, "fonts/Montserrat-Medium.ttf")
        v.titlepage!!.typeface = MMedium
        v.subtitle.typeface = MLight
        v.endpage.typeface = MLight
        adapter = MainViewAdapter(this.context!!, activities)
        v.recycle_activity.layoutManager = LinearLayoutManager(activity)
        v.recycle_activity.adapter = adapter
        adapter!!.notifyDataSetChanged()

        return v
    }


}
