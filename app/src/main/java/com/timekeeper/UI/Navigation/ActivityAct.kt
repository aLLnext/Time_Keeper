package com.timekeeper.UI.Navigation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timekeeper.Adapters.MainActivityAdapter
import com.timekeeper.Model.Supplier
import com.example.toxaxab.timekeeper.R
import kotlinx.android.synthetic.main.fragment_activity.view.*


class ActivityAct : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_activity, container, false)
        val layoutManager = LinearLayoutManager(v.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //val rv = v!!.find<RecyclerView>(R.id.recyclerView)

        v.recyclerView.layoutManager = layoutManager

        val adapter = MainActivityAdapter(v.context, Supplier.activities)
        v.recyclerView.adapter = adapter

        return v
    }

    /*TO DEBUG
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
           Toast.makeText(activity, "FirstFragment.onStart()",
                   Toast.LENGTH_LONG).show()
           Log.d("Fragment 1", "onStart")
       }

       override fun onResume() {
           super.onResume()
           Toast.makeText(activity, "FirstFragment.onResume()",
                   Toast.LENGTH_LONG).show()
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
       }*/
}