package com.tripfellows.client.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.tripfellows.client.R
import com.tripfellows.client.dto.CreateTripRequest
import java.util.*

class CreateTripFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.create_trip_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createButton: Button = view.findViewById(R.id.create_button)
        createButton.setOnClickListener { createButtonPressed(view) }
    }

    private fun createButtonPressed(view: View) {
        val departureAddress = view.findViewById<EditText>(R.id.create_trip_departure_address).text.toString()
        val destinationAddress = view.findViewById<EditText>(R.id.create_trip_destination_address).text.toString()
        val places = view.findViewById<EditText>(R.id.create_trip_places).text.toString().toInt()
        val timeString = view.findViewById<EditText>(R.id.create_trip_start_time).text.toString()
        val price = view.findViewById<EditText>(R.id.create_trip_price).text.toString().toInt()
        val comment = view.findViewById<EditText>(R.id.create_trip_comment).text.toString()

        val date = Date()
        date.hours = timeString.split(":")[0].toInt()
        date.minutes = timeString.split(":")[1].toInt()

        var newTrip = CreateTripRequest(departureAddress, destinationAddress, places, date, price, comment)
    }
}
