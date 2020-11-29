package com.tripfellows.authorization.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.R
import com.tripfellows.authorization.model.Trip
import com.tripfellows.authorization.model.TripMember
import com.tripfellows.authorization.states.ActionState
import com.tripfellows.authorization.util.DateTimeUtil
import com.tripfellows.authorization.viewmodel.TripViewViewModel

class TripViewFragment : Fragment() {

    private lateinit var viewModel: TripViewViewModel
    private lateinit var currentTrip: Trip

    companion object {
        private const val TRIP_ID_KEY = "tripId"
        private const val DRIVER_MODE_KEY = "driverMode"
        fun newInstance(tripId: Int, driverMode: Boolean): TripViewFragment {
            val fragment = TripViewFragment()
            val bundle = Bundle()
            bundle.putInt(TRIP_ID_KEY, tripId)
            bundle.putBoolean(DRIVER_MODE_KEY, driverMode)
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

        val joinTripButton: Button = view.findViewById(R.id.join_btn)
        val getOutBtn: Button = view.findViewById(R.id.get_out_btn)

        viewModel.getTrip().observe(viewLifecycleOwner, TripObserver())

        if (arguments != null) {
            viewModel.refresh(arguments!!.getInt(TRIP_ID_KEY))
        }

        viewModel.getJoiningState().observe(viewLifecycleOwner, JoinButtonObserver(joinTripButton))
        viewModel.getRemovingState().observe(viewLifecycleOwner, GetOutButtonObserver(getOutBtn))

        joinTripButton.setOnClickListener {
            viewModel.joinMember(arguments!!.getInt(TRIP_ID_KEY))
        }

        getOutBtn.setOnClickListener {
            viewModel.removeMember(getCurrentTripMemberId())
        }
    }

    private fun getCurrentTripMemberId(): Int {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid

        for (member in currentTrip.members) {
            if (member.account.uid == currentUid) {
                return member.id
            }
        }

        return 0
    }

    inner class TripObserver: Observer<Trip> {
        override fun onChanged(trip: Trip) {
            view?.findViewById<TextView>(R.id.tripDepartureVal)?.text = trip.departureAddress.address
            view?.findViewById<TextView>(R.id.tripDestinationVal)?.text = trip.destinationAddress.address
            view?.findViewById<TextView>(R.id.tripStartTimeVal)?.text = DateTimeUtil.formatWithDateAndTime(trip.startDate)
            view?.findViewById<TextView>(R.id.tripPlacesVal)?.text = trip.placesCount.toString()
            view?.findViewById<TextView>(R.id.tripPriceVal)?.text = trip.price
            view?.findViewById<TextView>(R.id.tripDriverVal)?.text = trip.creator.name

            val passengerListView = view?.findViewById<ListView>(R.id.tripPassengersList)

            val passengerList = mutableListOf<String>()
            for (member in trip.members) {
                passengerList.add(member.account.name)
            }

            val adapter = ArrayAdapter<String>(context!!, R.layout.passenger_list_item, passengerList)

            if (passengerListView != null) {
                passengerListView.adapter = adapter
            }

            resolveButtonsVisibility(trip.members)
            currentTrip = trip
        }

        private fun resolveButtonsVisibility(tripMembers: List<TripMember>) {
            val joinBtn = view?.findViewById<Button>(R.id.join_btn)!!
            val getOutBtn = view?.findViewById<Button>(R.id.get_out_btn)!!

            val driverMode = arguments!!.getBoolean(DRIVER_MODE_KEY)

            if (driverMode) {
                joinBtn.visibility = View.GONE
                getOutBtn.visibility = View.GONE
                return
            }

            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

            for (tripMember in tripMembers) {
                if (tripMember.account.uid == currentUserUid) {
                    joinBtn.isEnabled = false
                    getOutBtn.isEnabled = true
                    return
                }
            }

            joinBtn.isEnabled = true
            getOutBtn.isEnabled = false
        }
    }

    inner class JoinButtonObserver(private val joinBtn: Button) : Observer<ActionState> {
        override fun onChanged(joinState: ActionState) {
            when(joinState) {
                ActionState.ERROR -> {
                    Toast.makeText(context, "Error during joining", Toast.LENGTH_SHORT).show()
                    joinBtn.isVisible = true
                    joinBtn.isEnabled = true
                }

                ActionState.IN_PROGRESS -> {
                    joinBtn.isEnabled = false
                }

                ActionState.SUCCESS -> {
                    Toast.makeText(context, "Success joining", Toast.LENGTH_SHORT).show()
                    joinBtn.isEnabled = true
                }
            }
        }

    }

    inner class GetOutButtonObserver(private val getOutBtn: Button) : Observer<ActionState> {
        override fun onChanged(removingState: ActionState) {
            when(removingState) {
                ActionState.ERROR -> {
                    Toast.makeText(context, "Error during get out", Toast.LENGTH_SHORT).show()
                    getOutBtn.isVisible = true
                    getOutBtn.isEnabled = true
                }

                ActionState.IN_PROGRESS -> {
                    getOutBtn.isEnabled = false
                }

                ActionState.SUCCESS -> {
                    Toast.makeText(context, "Success get out", Toast.LENGTH_SHORT).show()
                    getOutBtn.isEnabled = true
                }
            }
        }
    }
}