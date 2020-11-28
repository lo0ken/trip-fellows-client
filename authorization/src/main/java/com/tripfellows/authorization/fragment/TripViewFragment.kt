package com.tripfellows.authorization.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tripfellows.authorization.R
import com.tripfellows.authorization.model.Trip
import com.tripfellows.authorization.util.DateTimeUtil
import com.tripfellows.authorization.viewmodel.TripViewViewModel

class TripViewFragment : Fragment() {

    private lateinit var viewModel: TripViewViewModel

    companion object {
        private const val TRIP_ID_KEY = "tripId"
        fun newInstance(tripId: Int): TripViewFragment {
            val fragment = TripViewFragment()
            val bundle = Bundle()
            bundle.putInt(TRIP_ID_KEY, tripId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.trip_view_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(activity!!, ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
            TripViewViewModel::class.java)

        val button: Button = view.findViewById(R.id.join_btn)

        viewModel.getTrip().observe(viewLifecycleOwner, TripObserver())

        if (arguments != null) {
            viewModel.refresh(arguments!!.getInt(TRIP_ID_KEY))
        }

        button.setOnClickListener {
            val text = "you joined the trip!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, text, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    inner class TripObserver: Observer<Trip> {
        override fun onChanged(trip: Trip) {
            view?.findViewById<TextView>(R.id.tripDeparture)?.text = trip.departureAddress.address
            view?.findViewById<TextView>(R.id.tripDestination)?.text = trip.destinationAddress.address
            view?.findViewById<TextView>(R.id.tripStartTime)?.text = DateTimeUtil.formatWithDateAndTime(trip.startDate)
            view?.findViewById<TextView>(R.id.tripPlaces)?.text = trip.placesCount.toString()
            view?.findViewById<TextView>(R.id.tripPrice)?.text = trip.price
        }
    }
}