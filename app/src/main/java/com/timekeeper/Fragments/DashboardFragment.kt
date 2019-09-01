package com.timekeeper.Fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timekeeper.R
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val MLight = Typeface.createFromAsset(activity!!.assets, "fonts/Montserrat-Light.ttf")
        val MMedium = Typeface.createFromAsset(activity!!.assets, "fonts/Montserrat-Medium.ttf")
        v.titlepageDashboard!!.typeface = MMedium
        v.subtitlepageDashboard.typeface = MLight
        return v
    }
}