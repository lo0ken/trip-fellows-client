package com.tripfellows.client.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tripfellows.client.R
import com.tripfellows.client.TripListAdapter
import java.util.*

class TripListFragment : Fragment() {

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

        val items: ArrayList<String> = ArrayList()
        items.add("Moscow")
        items.add("Himki")
        items.add("Odintsovo")
        items.add("Krasnogorsk")
        items.add("Malahovka")
        items.add("Frazino")
        items.add("Korolev")
        items.add("Zelenograd")
        items.add("Dolgoprudny")

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        val adapter =  TripListAdapter(items)
        recyclerView.adapter = adapter
    }
}
