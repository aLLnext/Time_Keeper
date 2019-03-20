package com.example.toxaxab.timekeeper

import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import com.example.toxaxab.timekeeper.UI.Navigation.ActivityAct
import com.example.toxaxab.timekeeper.UI.Navigation.SettingsAct
import com.example.toxaxab.timekeeper.UI.Navigation.StatisticsAct

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
        container.offscreenPageLimit = 2
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/

        /*val mBuilder:NotificationCompat.Builder = NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.ic_play)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
// Creates an explicit intent for an Activity in your app
        val resultIntent = Intent(this, MainActivity::class.java)

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        val stackBuilder = TaskStackBuilder.create(this)
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity::class.java)
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)
        val mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
// mId allows you to update the notification later on.
        val mId = 1
        mNotificationManager!!.notify(mId, mBuilder.build())*/
    }

    //TO DEBUG
    /*override fun onStart() {
        super.onStart()

        Toast.makeText(applicationContext, "onStart()", Toast.LENGTH_SHORT).show()
        Log.i("MAIN", "onStart()")
    }

    override fun onResume() {
        super.onResume()

        Toast.makeText(applicationContext, "onResume()", Toast.LENGTH_SHORT).show()
        Log.i("MAIN", "onResume()")
    }

    override fun onPause() {
        super.onPause()

        Toast.makeText(applicationContext, "onPause()", Toast.LENGTH_SHORT).show()
        Log.i("MAIN", "onPause()")
    }

    override fun onStop() {
        super.onStop()

        Toast.makeText(applicationContext, "onStop()", Toast.LENGTH_SHORT).show()
        Log.i("MAIN", "onStop()")
    }

    override fun onRestart() {
        super.onRestart()

        Toast.makeText(applicationContext, "onRestart()", Toast.LENGTH_SHORT).show()
        Log.i("MAIN", "onRestart()")
    }

    override fun onDestroy() {
        super.onDestroy()

        Toast.makeText(applicationContext, "onDestroy()", Toast.LENGTH_SHORT).show()
        Log.i("MAIN", "onDestroy()")
    }
    //!TO DEBUG*/

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            when (position) {
                0 -> {
                    return ActivityAct()
                }
                1 -> {
                    return StatisticsAct()
                }
                2 -> {
                    return SettingsAct()
                }
                else -> return null
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> {
                    return resources.getString(R.string.tab_1)
                }
                1 -> {
                    return resources.getString(R.string.tab_2)
                }
                2 -> {
                    return resources.getString(R.string.tab_3)
                }
                else -> return null
            }
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */

}
