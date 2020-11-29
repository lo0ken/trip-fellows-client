package com.tripfellows.authorization.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tripfellows.authorization.R
import com.tripfellows.authorization.model.Address
import com.tripfellows.authorization.network.request.CreateTripRequest
import com.tripfellows.authorization.states.ActionState
import com.tripfellows.authorization.viewmodel.CreateTripViewModel
import com.tripfellows.authorization.viewmodel.LocationViewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateTripFragment : Fragment() {
    private lateinit var createTripViewModel: CreateTripViewModel
    private lateinit var locationViewModel: LocationViewModel

    private lateinit var departureAddress: Address
    private lateinit var destinationAddress: Address

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.create_trip_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectTime = view.findViewById<TextView>(R.id.start_time)
        selectTime.setOnClickListener {
            var startTimeHour:Int = 0
            var startTimeMinute:Int = 0

            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                startTimeHour = hourOfDay
                startTimeMinute = minute
                val calendar = Calendar.getInstance()
                calendar.set(0,0,0,startTimeHour,startTimeMinute)
                selectTime.text = android.text.format.DateFormat.format("hh:mm", calendar)
            }

            val timePickerDialog = TimePickerDialog(
                view.context,
                timeSetListener,
                12, 0, true
            )

            timePickerDialog.updateTime(startTimeHour, startTimeMinute)
            timePickerDialog.show()
        }

        locationViewModel = ViewModelProvider(activity!!,
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
            LocationViewModel::class.java)

        val departureAddressTextField: EditText = view.findViewById(R.id.departure_address)
        locationViewModel.getAddress()
            .observe(viewLifecycleOwner, AddressObserver(departureAddressTextField))
        locationViewModel.getCurrentAddress()

        createTripViewModel = ViewModelProvider(activity!!,
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
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
        val places = view.findViewById<EditText>(R.id.places).text.toString()
        var startTimeString = view.findViewById<EditText>(R.id.start_time).text.toString()
        val price = view.findViewById<EditText>(R.id.price).text.toString()
        val comment = view.findViewById<EditText>(R.id.comment).text.toString()

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'")

        val tripDateTime = simpleDateFormat.format(Date()) + startTimeString

        var newTrip = CreateTripRequest(
                this.departureAddress,
                this.destinationAddress,
                Integer.parseInt(places),
                tripDateTime,
                Integer.parseInt(price),
                comment
        )

        createTripViewModel.createTrip(newTrip)
    }

    inner class CreateButtonObserver(private val createBtn: Button) : Observer<ActionState> {

        override fun onChanged(createTripState: ActionState) {
            when (createTripState) {
                ActionState.NONE -> setButtonEnable(true)
                ActionState.ERROR -> {
                    Toast.makeText(context, "Error during creating trip", Toast.LENGTH_LONG).show()
                    setButtonEnable(true)
                }
                ActionState.IN_PROGRESS -> setButtonEnable(false)
                ActionState.SUCCESS -> {
                    Toast.makeText(context, "Successfully created trip!", Toast.LENGTH_LONG).show()
                }
                else -> setButtonEnable(true)
            }
        }

        private fun setButtonEnable(enabled: Boolean) {
            createBtn.isEnabled = true
        }
    }

    inner class AddressObserver(private val editText: EditText) : Observer<Address> {

        override fun onChanged(address: Address?) {
            if (address != null) {
                editText.setText(address.address)
                departureAddress = address
                destinationAddress = address
            }
        }
    }
}
