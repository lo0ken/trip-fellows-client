package com.tripfellows.authorization.triplist

import TripData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.MainRouter

class TripListAdapter(
    private val data: List<TripData>,
    private val mainRouter: MainRouter
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            mainRouter.showTrip(currentTrip.id)
        })

    }

    override fun getItemCount(): Int {
        return data.size
    }
}