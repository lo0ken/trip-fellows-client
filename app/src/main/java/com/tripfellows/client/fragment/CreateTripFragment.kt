package com.tripfellows.client.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.client.MainActivity
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
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)
            ?.addOnCompleteListener(activity as Activity) { task ->
                if (task.isSuccessful) {
                    println("x")
                } else {
                    println("Error: ${task.exception?.message}")
                }
            }
    }

    private fun createButtonPressed(view: View) {
        val departureAddress = view.findViewById<EditText>(R.id.create_trip_departure_address).text.toString()
        val destinationAddress = view.findViewById<EditText>(R.id.create_trip_destination_address).text.toString()
        val places = view.findViewById<EditText>(R.id.create_trip_places).text.toString().toInt()
        val startTimeString = view.findViewById<EditText>(R.id.create_trip_start_time).text.toString()
        val price = view.findViewById<EditText>(R.id.create_trip_price).text.toString().toInt()
        val comment = view.findViewById<EditText>(R.id.create_trip_comment).text.toString()

        val startDate = Date()
        startDate.hours = startTimeString.split(":")[0].toInt()
        startDate.minutes = startTimeString.split(":")[1].toInt()

        var newTrip = CreateTripRequest(departureAddress, destinationAddress, places, startDate, price, comment)
    }
}
