package com.tripfellows.authorization.fragment

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
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
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.MainRouter
import com.tripfellows.authorization.model.Address
import com.tripfellows.authorization.network.request.CreateTripRequest
import com.tripfellows.authorization.states.ActionStatus
import com.tripfellows.authorization.util.DateTimeUtil
import com.tripfellows.authorization.util.TargetAddress
import com.tripfellows.authorization.viewmodel.CreateTripViewModel
import com.tripfellows.authorization.viewmodel.LocationViewModel
import java.util.*
import com.google.android.gms.maps.model.LatLng as AndroidGmsLatLng


class CreateTripFragment : Fragment() {
    private lateinit var createTripViewModel: CreateTripViewModel
    private lateinit var locationViewModel: LocationViewModel

    private lateinit var currentAddress: Address
    private lateinit var departureAddress: Address
    private lateinit var destinationAddress: Address

    private lateinit var router: MainRouter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        router = context as MainRouter

    }

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

        val departureLocationButton: Button = view.findViewById(R.id.departure_location_button)
        departureLocationButton.setOnClickListener { showMap(TargetAddress.DEPARTURE) }

        val destinationLocationButton: Button = view.findViewById(R.id.destination_location_button)
        destinationLocationButton.setOnClickListener { showMap(TargetAddress.DESTINATION) }

        locationViewModel.getAddress()
            .observe(viewLifecycleOwner, AddressObserver())
        locationViewModel.getCurrentAddress()

        createTripViewModel = ViewModelProvider(activity!!,
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
            CreateTripViewModel::class.java)

        createButton(view)

        showBottomMenu()
    }

    private fun showMap(targetAddress: TargetAddress) {
        if (!::currentAddress.isInitialized) return

        if (currentAddress.latitude != null && currentAddress.longitude != null) {
            val mapFragment = ActiveMapFragment.newInstance(AndroidGmsLatLng(currentAddress.latitude!!, currentAddress.longitude!!))
            mapFragment.setTargetFragment(this, targetAddress.ordinal)
            fragmentManager?.beginTransaction()
                ?.replace(R.id.main_fragment_container, mapFragment)
                ?.addToBackStack("Map opened")
                ?.commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == TargetAddress.DEPARTURE.ordinal) {
                departureAddress = Address()
                setAddressFromMap(data, departureAddress)
            } else if (requestCode == TargetAddress.DESTINATION.ordinal) {
                destinationAddress = Address()
                setAddressFromMap(data, destinationAddress)
            }
        }
        showBottomMenu()
    }

    private fun setAddressFromMap(data: Intent, address: Address) {
        val latitude = data.getStringExtra("latitude")?.toDouble()
        val longitude = data.getStringExtra("longitude")?.toDouble()

        if (latitude != null && longitude != null) {
            address.latitude = latitude
            address.longitude = longitude
        }
    }

    private fun showBottomMenu() {
        val bottomNavigationView: BottomNavigationView? =
            activity?.findViewById(R.id.bottom_navigation)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    private fun createButton(view: View) {
        val createButton: Button = view.findViewById(R.id.create_button)

        createTripViewModel.getProgress()
            .observe(viewLifecycleOwner, CreateButtonObserver(createButton))

        createButton.setOnClickListener { createButtonPressed(view)
        }
    }

    private fun createButtonPressed(view: View) {
        val awesomeVal = AwesomeValidation(ValidationStyle.BASIC)
        awesomeVal.addValidation(activity, R.id.places, "^[1-5]$", R.string.invalide_field_num)
        awesomeVal.addValidation(activity, R.id.price, RegexTemplate.NOT_EMPTY, R.string.invalide_field)
        awesomeVal.addValidation(activity, R.id.departure_address, RegexTemplate.NOT_EMPTY, R.string.invalide_field)
        awesomeVal.addValidation(activity, R.id.destination_address, RegexTemplate.NOT_EMPTY, R.string.invalide_field)
        awesomeVal.addValidation(activity, R.id.start_time, RegexTemplate.NOT_EMPTY, R.string.invalide_field)
        if (awesomeVal.validate()) {
        val places = view.findViewById<EditText>(R.id.places).text.toString()
        val startTimeString = view.findViewById<TextView>(R.id.start_time).text.toString()
        val price = view.findViewById<EditText>(R.id.price).text.toString()
        val comment = view.findViewById<EditText>(R.id.comment).text.toString()
        val tripDateTime = DateTimeUtil.makeServerCurrentDayWithTime(startTimeString)

        departureAddress.address = view.findViewById<EditText>(R.id.departure_address).text.toString()
        destinationAddress.address = view.findViewById<EditText>(R.id.destination_address).text.toString()

        val newTrip = CreateTripRequest(
                this.departureAddress,
                this.destinationAddress,
                Integer.parseInt(places),
                tripDateTime,
                Integer.parseInt(price),
                comment
        )

        createTripViewModel.createTrip(newTrip)
    }else {
        val toast = Toast.makeText(context, "Validation failed", Toast.LENGTH_SHORT)
        toast.show()}}

    inner class CreateButtonObserver(private val createBtn: Button) : Observer<ActionStatus> {

        override fun onChanged(createTripStatus: ActionStatus) {
            when (createTripStatus) {
                ActionStatus.NONE -> setButtonEnable(true)
                ActionStatus.ERROR -> {
                    Toast.makeText(context, "Error during creating trip", Toast.LENGTH_LONG).show()
                    setButtonEnable(true)
                }
                ActionStatus.IN_PROGRESS -> setButtonEnable(false)
                ActionStatus.SUCCESS -> {
                    router.tripCreated()
                    Toast.makeText(context, "Successfully created trip!", Toast.LENGTH_LONG).show()
                }
                else -> setButtonEnable(true)
            }
        }

        private fun setButtonEnable(enabled: Boolean) {
            createBtn.isEnabled = true
        }
    }

    inner class AddressObserver : Observer<Address> {

        override fun onChanged(address: Address?) {
            if (address != null) {
                currentAddress = address
            }
        }
    }
}
