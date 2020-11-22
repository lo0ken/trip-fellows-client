package com.tripfellows.authorization.triplist

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tripfellows.authorization.R

class TripListViewHolder(
    private val itemView: View,
    val departureAddress: TextView = itemView.findViewById(R.id.trip_departure_address),
    val destinationAddress: TextView = itemView.findViewById(R.id.trip_destination_address),
    val startTime: TextView = itemView.findViewById(R.id.trip_start_time),
    val places: TextView = itemView.findViewById(R.id.trip_places),
    val price: TextView = itemView.findViewById(R.id.trip_price),
    //val itemlist: CardView = itemView.findViewById(R.id.trip_item),
    val itemlist: CardView? = itemView.findViewById(R.id.trip_item) as CardView?
) : RecyclerView.ViewHolder(itemView)