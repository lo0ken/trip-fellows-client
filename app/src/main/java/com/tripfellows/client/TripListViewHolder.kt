package com.tripfellows.client

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TripListViewHolder(
    private val itemView: View,
    val departureAddress: TextView = itemView.findViewById(R.id.trip_departure_address),
    val destinationAddress: TextView = itemView.findViewById(R.id.trip_destination_address),
    val startTime: TextView  = itemView.findViewById(R.id.trip_start_time),
    val places: TextView = itemView.findViewById(R.id.trip_places),
    val price: TextView = itemView.findViewById(R.id.trip_price)
) : RecyclerView.ViewHolder(itemView)