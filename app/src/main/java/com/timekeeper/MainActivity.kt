package com.timekeeper

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.facebook.stetho.Stetho
import com.timekeeper.Fragments.DashboardFragment
import com.timekeeper.Fragments.MainFragment
import com.timekeeper.Fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_navigation.*


class MainActivity : AppCompatActivity() {
    private lateinit var main: MainFragment
    private lateinit var dashboard: DashboardFragment
    private lateinit var settings: SettingsFragment

    companion object {
        const val KEY_MAIN = "MainFragment"
        const val KEY_DASHBOARD = "DashboardFragment"
        const val KEY_SETTINGS = "SettingsFragment"
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectedFragment by lazy {
            when (item.itemId) {
                R.id.navigationHome -> {
                    main
                }
                R.id.navigationInfo -> {
                    dashboard
                }
                R.id.navigationSettings -> {
                    settings
                }
                else -> null
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var Fmain: Fragment? = null
        var Fdashboard: Fragment? = null
        var Fsettings: Fragment? = null
        if (savedInstanceState != null) {
            Toast.makeText(applicationContext, "HAS", Toast.LENGTH_SHORT).show()
            Fmain = supportFragmentManager.getFragment(savedInstanceState, KEY_MAIN)
            Fdashboard = supportFragmentManager.getFragment(savedInstanceState, KEY_DASHBOARD)
            Fsettings = supportFragmentManager.getFragment(savedInstanceState, KEY_SETTINGS)
        }
        main = if (Fmain == null) MainFragment() else Fmain as MainFragment
        dashboard = if (Fdashboard == null) DashboardFragment() else Fdashboard as DashboardFragment
        settings = if (Fsettings == null) SettingsFragment() else Fsettings as SettingsFragment

        Stetho.initializeWithDefaults(this)
        setContentView(R.layout.activity_navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, main).commit()
        //addFragmentsToManager()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (main.isAdded)
            supportFragmentManager.putFragment(outState!!, KEY_MAIN, main)
        if (dashboard.isAdded)
            supportFragmentManager.putFragment(outState!!, KEY_DASHBOARD, dashboard)
        if (settings.isAdded)
            supportFragmentManager.putFragment(outState!!, KEY_SETTINGS, settings)
        val adapter = main.adapter
        adapter.saveStates(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val adapter = main.adapter
        adapter.restoreStates(savedInstanceState)
    }
}
