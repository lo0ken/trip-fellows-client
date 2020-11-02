package com.tripfellows.client

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(context: Context?, data: List<String>) :
    RecyclerView.Adapter<MyViewHolder>() {
    private val layoutInflater: LayoutInflater
    private val data: List<String>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = layoutInflater.inflate(R.layout.element_search_history, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        val TitleTextW2 = data[i]
        holder.textWayS.setText(TitleTextW2)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    init {
        layoutInflater = LayoutInflater.from(context)
        this.data = data
    }
}