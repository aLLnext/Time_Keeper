package com.timekeeper.UI.Navigation.ActivityTab

import android.content.ContentValues
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.timekeeper.Adapters.ActivityActAdapter
import com.timekeeper.Model.Supplier
import com.example.toxaxab.timekeeper.R
import com.timekeeper.Adapters.ActAdapter
import com.timekeeper.MainActivity
import com.timekeeper.Model.Condition
import kotlinx.android.synthetic.main.fragment_activity.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ActivityAct : Fragment() {
    //private var sPref: SharedPreferences? = null
    //private var editor: SharedPreferences.Editor? = null
    private var dataBase: SQLiteDatabase? = null
    private lateinit var adapter: ActAdapter
    //@SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_activity, container, false)
        val layoutManager = LinearLayoutManager(v.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //val rv = v!!.find<RecyclerView>(R.id.recyclerView)
        //val id = activity?.intent?.getIntExtra("activity", 0)
        v.recyclerView.layoutManager = layoutManager
        val act = activity as MainActivity
        adapter = ActAdapter(v.context)
        adapter.setData(Supplier.activities)
        v.recyclerView.adapter = adapter
        dataBase = context!!.openOrCreateDatabase("data.db", MODE_PRIVATE, null)
        //dataBase!!.execSQL("DROP TABLE activities")
        dataBase!!.execSQL("CREATE TABLE IF NOT EXISTS activities(id INT, name VARCHAR(50), time LONG, timerBase LONG, condition BOOL)")
        if (dataBase!!.isOpen) {
            Toast.makeText(activity, "DATEBASE",
                    Toast.LENGTH_SHORT).show()
        }
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(activity, "FirstFragment.onCreate()",
                Toast.LENGTH_SHORT).show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadData()
        Toast.makeText(activity, "FirstFragment.onActivityCreated()",
                Toast.LENGTH_SHORT).show()
        //Log.d("Fragment 1", "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        //Toast.makeText(activity, "FirstFragment.onStart()",
        //        Toast.LENGTH_SHORT).show()
        Log.d("Fragment 1", "onStart")
    }

    override fun onResume() {
        super.onResume()
        loadData()
        if (activity?.intent?.action != null) {
            if (activity?.intent?.action == "STOP") {
                val id = activity?.intent?.getIntExtra("activity", -1)
                val timeStart = activity?.intent?.getLongExtra("start", 0)
                Supplier.activities[id!!].currentTime = (SystemClock.elapsedRealtime() - timeStart!!)
                Supplier.activities[id].condition = Condition.ACTIVE
                //adapter.
            }
        }
        /*val id1 = activity?.intent?.getIntExtra("activity", -1)
        val timeStart = activity?.intent?.getLongExtra("start", 0)
        Toast.makeText(activity, "FirstFragment.onResume()  = $id1",
                Toast.LENGTH_LONG).show()
        if (id1 != -1) {
            Supplier.activities[id1!!].currentTime = (SystemClock.elapsedRealtime() - timeStart!!)
            Supplier.activities[id1].condition = Condition.ACTIVE
        }*/
        Log.d("Fragment 1", "onResume")
    }

    override fun onPause() {
        super.onPause()
        //Toast.makeText(activity, "FirstFragment.onPause()",
        //        Toast.LENGTH_SHORT).show()
        Log.d("Fragment 1", "onPause")
    }

    override fun onStop() {
        super.onStop()
        saveData()
        Toast.makeText(activity, "FirstFragment.onStop()",
                Toast.LENGTH_SHORT).show()
        Log.d("Fragment 1", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Toast.makeText(activity, "FirstFragment.onDestroyView()",
        //        Toast.LENGTH_SHORT).show()
        Log.d("Fragment 1", "onDestroyView")
    }

    override fun onDestroy() {
        saveData()
        dataBase?.close()
        super.onDestroy()
        Toast.makeText(activity, "FirstFragment.onDestroy()",
                Toast.LENGTH_SHORT).show()
        Log.d("Fragment 1", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        //Toast.makeText(activity, "FirstFragment.onDetach()",
        //        Toast.LENGTH_SHORT).show()
        Log.d("Fragment 1", "onDetach")
    }

    private fun initializeValues(data: Cursor) {
        val id = data.getInt(0)
        val currentTime = data.getLong(1)
        val timerBase = data.getLong(2)
        val cond: Condition = if (data.getInt(3) == 0) Condition.ACTIVE else Condition.INACTIVE
        with(Supplier.activities[id]) {
            if (timerBase == 0L)
                return
            this.condition = cond
            this.timerBase = timerBase
            if (cond == Condition.ACTIVE)
                this.currentTime += (SystemClock.elapsedRealtime() - this.timerBase - this.currentTime)
            else
                this.currentTime = currentTime
        }
    }

    private fun loadData() {
        if (dataBase!!.isOpen) {
            Toast.makeText(activity, "LOAD DATA",
                    Toast.LENGTH_SHORT).show()
        }else{
            dataBase = context!!.openOrCreateDatabase("data.db", MODE_PRIVATE, null)
        }
        //dataBase!!.execSQL("DROP TABLE activities")
        //dataBase!!.execSQL("CREATE TABLE IF NOT EXISTS activities(id INT, name VARCHAR(50), time LONG, timerBase LONG, condition BOOL)")
        val data = dataBase?.rawQuery("SELECT id, time, timerBase, condition FROM activities ORDER BY id", null)
        if (data!!.moveToFirst()) {
            initializeValues(data)
            while (data.moveToNext()) {
                initializeValues(data)
            }
            data.close()
        }
        /*editor!!.clear()
        editor!!.commit()
        for (act in Supplier.activities) {
            val tmp = sPref!!.getStringSet(act.id.toString(), null)
            System.out.println(tmp?.size)
            if (tmp != null) {
                val list = ArrayList<String>(tmp)
                //System.out.println(list)
                act.condition = Condition.valueOf(list[0])
                act.currentTime = (SystemClock.elapsedRealtime() - list[1].toLong())
                val time = (SystemClock.elapsedRealtime() - list[2].toLong()) - act.currentTime
                act.currentTime += time

            }
        }
        //editor!!.clear()
        //editor!!.commit()*/
    }

    fun saveData() = runBlocking {
        launch(Dispatchers.IO) {
            //dataBase!!.execSQL("DROP TABLE activities")
            //dataBase!!.execSQL("CREATE TABLE IF NOT EXISTS activities(id INT, name VARCHAR(50), time LONG, timerBase LONG, condition BOOL)")
            //TODO надо сохранять также все активности,
            //и скорее всего надо будет переелать метод сохранения
            //скорее всего в БД, потому что SET хранит даные произвольно

            for (act in Supplier.activities) {
                val row = ContentValues()
                row.put("id", act.id)
                row.put("name", act.name)
                row.put("time", act.currentTime)
                row.put("timerBase", act.timerBase)
                row.put("condition", act.condition.ordinal)
                //if (act.currentTime != 0L) {
                //    dataBase!!.execSQL("DROP TABLE activities")
                //    dataBase!!.execSQL("CREATE TABLE IF NOT EXISTS activities(id INT, name VARCHAR(50), time LONG, timerBase LONG, condition BOOL)")
                //}
                dataBase!!.insert("activities", null, row)
                /*else {
                    System.out.print("RES $row")
                    dataBase!!.update("activities", row, "id = ?", Array(1) { id.toString() })
                }*/
                //update
            }
        }.join()
    }
}