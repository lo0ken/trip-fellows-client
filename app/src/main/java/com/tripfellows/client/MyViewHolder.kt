package com.tripfellows.client

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textWayF: TextView
    var textWayS: TextView
    var textTime: TextView
    var textPlace: TextView
    var textMoney: TextView

    init {
        textWayF = itemView.findViewById(R.id.TextWay1)
        textWayS = itemView.findViewById(R.id.TextWay2)
        textTime = itemView.findViewById(R.id.TextTime)
        textPlace = itemView.findViewById(R.id.Places)
        textMoney = itemView.findViewById(R.id.Money)
    }
}