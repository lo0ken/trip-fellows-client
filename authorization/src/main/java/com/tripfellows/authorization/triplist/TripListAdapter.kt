package com.tripfellows.authorization.triplist

import com.tripfellows.authorization.model.Trip
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.MainRouter

class TripListAdapter(
    private val data: List<Trip>,
    private val mainRouter: MainRouter
) : RecyclerView.Adapter<TripListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.trip_list_item,
            parent,
            false
        )
        return TripListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripListViewHolder, position: Int) {
        val currentTrip = data[position]
        holder.destinationAddress.text = currentTrip.destinationAddress
        holder.departureAddress.text = currentTrip.departureAddress
        holder.startTime.text = currentTrip.startTime.toString()
        holder.places.text = currentTrip.places.toString()
        holder.price.text = currentTrip.price
        holder.tripItemView.setOnClickListener {
            mainRouter.showTrip(currentTrip.id)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}