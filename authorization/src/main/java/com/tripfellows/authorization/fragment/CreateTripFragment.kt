package com.tripfellows.authorization.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.CreateTripListener
import com.tripfellows.authorization.network.request.CreateTripRequest
import com.tripfellows.authorization.states.CreateTripState
import com.tripfellows.authorization.viewmodel.CreateTripViewModel
import java.util.*

class CreateTripFragment : Fragment() {
    private lateinit var createButtonListener : CreateTripListener
    private lateinit var createTripViewModel: CreateTripViewModel

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

        createTripViewModel = ViewModelProvider(activity!!, ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
            CreateTripViewModel::class.java)

        createButton(view)
    }

    private fun createButton(view: View) {
        val createButton: Button = view.findViewById(R.id.create_button)

        createTripViewModel.getProgress()
            .observe(viewLifecycleOwner, CreateButtonObserver(createButton))

        createButton.setOnClickListener { createButtonPressed(view) }

    }

    private fun createButtonPressed(view: View) {
        val departureAddress = view.findViewById<EditText>(R.id.departure_address).text.toString()
        val destinationAddress = view.findViewById<EditText>(R.id.destination_address).text.toString()
        val places = view.findViewById<EditText>(R.id.places).text.toString()
        var startTimeString = view.findViewById<EditText>(R.id.start_time).text.toString()
        val price = view.findViewById<EditText>(R.id.price).text.toString()
        val comment = view.findViewById<EditText>(R.id.comment).text.toString()

        val startDate = Date()
        startTimeString = "2020-11-11T12:20"


        var newTrip = CreateTripRequest(
                departureAddress,
                destinationAddress,
                Integer.parseInt(places),
            startTimeString,
                Integer.parseInt(price),
                comment
        )

        createTripViewModel.createTrip(newTrip)
    }

    private fun stringToIntOrZeroWhenNull (string : String): Int {
        var number = string
        if (number == "") number = "0"
        return number.toInt()
    }

    inner class CreateButtonObserver(private val createBtn: Button) : Observer<CreateTripState> {

        override fun onChanged(createTripState: CreateTripState) {
            when(createTripState) {
                CreateTripState.NONE -> setButtonEnable(true)
                CreateTripState.ERROR -> {
                    Toast.makeText(context, "Error during login", Toast.LENGTH_LONG).show()
                    setButtonEnable(true)
                }
                CreateTripState.IN_PROGRESS -> setButtonEnable(false)
                CreateTripState.SUCCESS -> {
                    Toast.makeText(context, "Success login", Toast.LENGTH_LONG).show()
                    createButtonListener.createTripButtonPressed()
                }
                else -> setButtonEnable(true)
            }
        }

        private fun setButtonEnable(enabled: Boolean) {
            createBtn.isEnabled = true
        }
    }
}
