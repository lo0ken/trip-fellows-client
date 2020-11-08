package com.tripfellows.client.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.tripfellows.client.R
import com.tripfellows.client.listeners.CreateTripListener
import com.tripfellows.client.request.CreateTripRequest
import java.util.*

class CreateTripFragment : Fragment() {
    private lateinit var createButtonListener : CreateTripListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createButtonListener = context as CreateTripListener
    }

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
        val departureAddress = view.findViewById<EditText>(R.id.departure_address).text.toString()
        val destinationAddress = view.findViewById<EditText>(R.id.destination_address).text.toString()
        val places = stringToIntOrZeroWhenNull(view.findViewById<EditText>(R.id.places).text.toString())
        var startTimeString = view.findViewById<EditText>(R.id.start_time).text.toString()
        val price = stringToIntOrZeroWhenNull(view.findViewById<EditText>(R.id.price).text.toString())
        val comment = view.findViewById<EditText>(R.id.comment).text.toString()

        val startDate = Date()
        startTimeString = "12:20"
        startDate.hours = stringToIntOrZeroWhenNull(startTimeString.split(":")[0])
        startDate.minutes = stringToIntOrZeroWhenNull(startTimeString.split(":")[1])

        var newTrip = CreateTripRequest(departureAddress, destinationAddress, places, startDate, price, comment)

        createButtonListener.createTripButtonPressed()
    }

    private fun stringToIntOrZeroWhenNull (string : String): Int {
        var number = string
        if (number == "") number = "0"
        return number.toInt()
    }
}
