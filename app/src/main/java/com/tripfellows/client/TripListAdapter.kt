package com.tripfellows.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TripListAdapter (private val data: List<String>) : RecyclerView.Adapter<TripListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.element_search_history, parent, false)
        return TripListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripListViewHolder, i: Int) {
        val destinationLocation = data[i]
        holder.destinationAddress.text = destinationLocation
    }

    override fun getItemCount(): Int {
        return data.size
    }
}