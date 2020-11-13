package com.tripfellows.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TripListAdapter(private val data: List<TripData>) : RecyclerView.Adapter<TripListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.trip_list, parent, false)
        return TripListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripListViewHolder, i: Int) {
        val currentTrip = data[i]
        holder.destinationAddress.text = currentTrip.destinationAddress
        holder.departureAddress.text = currentTrip.departureAddress
        holder.startTime.text = currentTrip.startTime.toString()
        holder.places.text = currentTrip.places.toString()
        holder.price.text = currentTrip.price.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }
}