package com.tripfellows.client

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TripListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textWayF: TextView
    var textWayS: TextView
    var textTime: TextView
    var textPlace: TextView
    var textMoney: TextView

    init {
        textWayF = itemView.findViewById(R.id.wayLocation1)
        textWayS = itemView.findViewById(R.id.wayLocation2)
        textTime = itemView.findViewById(R.id.dataTime)
        textPlace = itemView.findViewById(R.id.places_pass)
        textMoney = itemView.findViewById(R.id.cost_in_money)
    }
}