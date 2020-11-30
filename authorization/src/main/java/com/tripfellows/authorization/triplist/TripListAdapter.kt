package com.tripfellows.authorization.triplist

import com.tripfellows.authorization.model.Trip
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.MainRouter
import com.tripfellows.authorization.util.DateTimeUtil

class TripListAdapter(
    private var data: List<Trip>,
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
        holder.destinationAddress.text = currentTrip.destinationAddress.address
        holder.departureAddress.text = currentTrip.departureAddress.address
        holder.startTime.text = DateTimeUtil.formatWithTime(currentTrip.startDate)
        holder.places.text = makeFreePlacesString(currentTrip)
        holder.price.text = currentTrip.price
        holder.tripItemView.setOnClickListener {
            mainRouter.showTrip(currentTrip.id, currentTrip.creator.uid)
        }
    }

    private fun makeFreePlacesString(trip: Trip): String {
        val busy = trip.members.size
        val all = trip.placesCount
        return (all - busy).toString() + "/" + all
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setTrips(trips: List<Trip>) {
        data = trips
        notifyDataSetChanged()
    }
}