package com.tripfellows.client

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class  TripListAdapter (context: Context?, private var data: List<String>) :
    RecyclerView.Adapter<TripListViewHolder>() {
    private val layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.element_search_history, parent, false)
        return TripListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripListViewHolder, i: Int) {
        val wayLocationS = data[i]
        holder.textWayS.setText(wayLocationS)
    }
    override fun getItemCount(): Int {
        return data.size
    }
    init {
        layoutInflater = LayoutInflater.from(context)

    }
}