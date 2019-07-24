package com.timekeeper

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.timekeeper.Adapters.MainViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var activities: ArrayList<String> = arrayListOf("sleep", "code", "repeat")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val MLight = Typeface.createFromAsset(assets, "fonts/Montserrat-Light.ttf")
        val MMedium = Typeface.createFromAsset(assets, "fonts/Montserrat-Medium.ttf")

        titlepage.typeface = MMedium
        subtitle.typeface = MLight
        endpage.typeface = MLight
        val adapter = MainViewAdapter(this, activities)
        recycle_activity.layoutManager = LinearLayoutManager(this)
        recycle_activity.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}
