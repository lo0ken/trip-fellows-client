package com.tripfellows.authorization.triplist

import TripData
import TripListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tripfellows.authorization.R
import com.tripfellows.authorization.fragmentAccess

class TripListAdapter(
    private val data: List<TripData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var activity: TripListFragment = TripListFragment()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.trip_list_item,
            parent,
            false
        )
        return TripListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        val currentTrip = data[i]
        val fViewHolder: TripListViewHolder = holder as TripListViewHolder
        fViewHolder.destinationAddress.text = currentTrip.destinationAddress
        fViewHolder.departureAddress.text = currentTrip.departureAddress
        fViewHolder.startTime.text = currentTrip.startTime.toString()
        fViewHolder.places.text = currentTrip.places.toString()
        fViewHolder.price.text = currentTrip.price
        fViewHolder.itemlist?.setOnClickListener(View.OnClickListener { v ->
            val digit = v.findViewById<TextView>(v.id)
            val number: Int = Integer.parseInt(digit.text as String)
            (activity as fragmentAccess).ShowTrip(number)
        })

    }

    override fun getItemCount(): Int {
        return data.size
    }
}