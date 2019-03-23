package com.timekeeper.UI.Navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.timekeeper.Adapters.MainActivityAdapter
import com.timekeeper.Model.Supplier
import com.example.toxaxab.timekeeper.R
import com.timekeeper.Model.Condition
import com.timekeeper.Model.MyActivity
import kotlinx.android.synthetic.main.fragment_activity.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ActivityAct : Fragment() {
    private var sPref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_activity, container, false)
        val layoutManager = LinearLayoutManager(v.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //val rv = v!!.find<RecyclerView>(R.id.recyclerView)
        //val id = activity?.intent?.getIntExtra("activity", 0)
        v.recyclerView.layoutManager = layoutManager
        val adapter = MainActivityAdapter(v.context, Supplier.activities)
        v.recyclerView.adapter = adapter
        sPref = PreferenceManager.getDefaultSharedPreferences(context)
        editor = sPref!!.edit()
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(activity, "FirstFragment.onCreate()",
                Toast.LENGTH_LONG).show()
    }


    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        Toast.makeText(getActivity(), "FirstFragment.onAttach()",
                Toast.LENGTH_LONG).show()
        Log.d("Fragment 1", "onAttach")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Toast.makeText(activity, "FirstFragment.onActivityCreated()",
                Toast.LENGTH_LONG).show()
        Log.d("Fragment 1", "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        loadData()
        Toast.makeText(activity, "FirstFragment.onStart()",
                Toast.LENGTH_LONG).show()
        Log.d("Fragment 1", "onStart")
    }

    override fun onResume() {
        super.onResume()
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
        Toast.makeText(activity, "FirstFragment.onPause()",
                Toast.LENGTH_LONG).show()
        Log.d("Fragment 1", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(activity, "FirstFragment.onStop()",
                Toast.LENGTH_LONG).show()
        Log.d("Fragment 1", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Toast.makeText(activity, "FirstFragment.onDestroyView()",
                Toast.LENGTH_LONG).show()
        Log.d("Fragment 1", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
        Toast.makeText(activity, "FirstFragment.onDestroy()",
                Toast.LENGTH_LONG).show()
        Log.d("Fragment 1", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        //mListener = null;
        Toast.makeText(activity, "FirstFragment.onDetach()",
                Toast.LENGTH_LONG).show()
        Log.d("Fragment 1", "onDetach")
    }

    fun saveData() = runBlocking {
        launch(Dispatchers.IO) {
            //TODO надо сохранять также все активности,
            //и скорее всего надо будет переелать метод сохранения
            //скорее всего в БД, потому что SET хранит даные произвольно
            for (act in Supplier.activities) {
                val set = HashSet<String>()
                set.add(act.condition.toString())
                set.add(act.currentTime.toString())
                set.add(act.timerBase.toString())
                editor!!.putStringSet(act.id.toString(), set)
            }
            editor!!.apply()
        }.join()
    }

    fun loadData() {
        //editor!!.clear()
        //editor!!.commit()
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
        //editor!!.commit()
    }
}