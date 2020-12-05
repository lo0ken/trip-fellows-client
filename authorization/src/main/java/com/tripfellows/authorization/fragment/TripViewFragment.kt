package com.tripfellows.authorization.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.R
import com.tripfellows.authorization.model.Trip
import com.tripfellows.authorization.model.TripMember
import com.tripfellows.authorization.model.TripStatusCodeEnum
import com.tripfellows.authorization.states.ActionState
import com.tripfellows.authorization.states.StateWithError
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
        val changeStatusBtn: Button = view.findViewById(R.id.change_status_button)

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

        changeStatusBtn.setOnClickListener {
            when(currentTrip.status.code) {
                TripStatusCodeEnum.WAITING -> viewModel.changeStatus(currentTrip.id, TripStatusCodeEnum.STARTED)
                TripStatusCodeEnum.STARTED -> viewModel.changeStatus(currentTrip.id, TripStatusCodeEnum.FINISHED)
            }
        }

        showBottomMenu()
    }

    private fun showMap(location : LatLng) {
        val mapFragment = DisabledMapFragment.newInstance(location)
        mapFragment.setTargetFragment(this, 100)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.main_fragment_container, mapFragment)
            ?.addToBackStack("Map opened")
            ?.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        showBottomMenu()
    }

    private fun showBottomMenu() {
        val bottomNavigationView: BottomNavigationView? =
            activity?.findViewById(R.id.bottom_navigation)
        bottomNavigationView?.visibility = View.VISIBLE
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
            val departureVal = view?.findViewById<TextView>(R.id.tripDepartureVal)!!
            departureVal.text = trip.departureAddress.address
            departureVal.setOnClickListener {
                showMap(LatLng(trip.departureAddress.latitude!!, trip.departureAddress.longitude!!))
            }

            val destinationVal = view?.findViewById<TextView>(R.id.tripDestinationVal)!!
            destinationVal.text = trip.destinationAddress.address
            destinationVal.setOnClickListener {
                showMap(LatLng(trip.destinationAddress.latitude!!, trip.destinationAddress.longitude!!))
            }

            view?.findViewById<TextView>(R.id.tripStartTimeVal)?.text = DateTimeUtil.formatWithDateAndTime(trip.startDate)
            view?.findViewById<TextView>(R.id.tripPlacesVal)?.text = trip.placesCount.toString()
            view?.findViewById<TextView>(R.id.tripPriceVal)?.text = trip.price
            view?.findViewById<TextView>(R.id.tripDriverVal)?.text = trip.creator.name
            view?.findViewById<TextView>(R.id.tripStatusVal)?.text = trip.status.name
            view?.findViewById<TextView>(R.id.tripCommentVal)?.text = trip.comment

            val passengerListView = view?.findViewById<ListView>(R.id.tripPassengersList)

            val passengerList = mutableListOf<String>()
            for (member in trip.members) {
                passengerList.add(member.account.name)
            }

            val adapter = ArrayAdapter<String>(context!!, R.layout.passenger_list_item, passengerList)

            if (passengerListView != null) {
                passengerListView.adapter = adapter
            }

            resolveButtonsVisibility(trip)
            currentTrip = trip
        }

        private fun resolveButtonsVisibility(trip: Trip) {
            val tripMembers: List<TripMember> = trip.members
            val joinBtn = view?.findViewById<Button>(R.id.join_btn)!!
            val getOutBtn = view?.findViewById<Button>(R.id.get_out_btn)!!
            val changeStatusButton = view?.findViewById<Button>(R.id.change_status_button)!!

            val driverMode = arguments!!.getBoolean(DRIVER_MODE_KEY)

            if (driverMode) {
                joinBtn.visibility = View.GONE
                getOutBtn.visibility = View.GONE
                changeStatusButton.visibility = View.VISIBLE

                when(trip.status.code) {
                    TripStatusCodeEnum.WAITING -> changeStatusButton.text = getString(R.string.status_start)
                    TripStatusCodeEnum.STARTED -> changeStatusButton.text = getString(R.string.status_finish)
                    TripStatusCodeEnum.FINISHED -> changeStatusButton.visibility = View.GONE
                }
                return
            } else {
                changeStatusButton.visibility = View.GONE
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

    inner class JoinButtonObserver(private val joinBtn: Button) : Observer<StateWithError> {
        override fun onChanged(joinStateWithError: StateWithError) {
            when(joinStateWithError.actionState) {
                ActionState.ERROR -> {
                    Toast.makeText(context, "Error during joining", Toast.LENGTH_SHORT).show()
                    joinBtn.isVisible = true
                    joinBtn.isEnabled = true
                }

                ActionState.FAILED -> {
                    Toast.makeText(context, joinStateWithError.errorMessage, Toast.LENGTH_SHORT).show()
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