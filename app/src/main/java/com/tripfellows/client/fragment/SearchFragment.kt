package com.tripfellows.client.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tripfellows.client.MyAdapter
import com.tripfellows.client.R
import java.util.*

class SearchFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var adapter: MyAdapter? = null
    var items: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.search_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items = ArrayList()
        items!!.add("Moscow")
        items!!.add("Himki")
        items!!.add("Odintsovo")
        items!!.add("Krasnogorsk")
        items!!.add("Malahovka")
        items!!.add("Frazino")
        items!!.add("Korolev")
        items!!.add("Zelenograd")
        items!!.add("Dolgoprudny")

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = (LinearLayoutManager(activity!!.applicationContext))
        adapter = MyAdapter(activity!!.applicationContext, items!!)
        recyclerView?.adapter = adapter
    }
}
