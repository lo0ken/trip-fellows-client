package com.tripfellows.authorization.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tripfellows.authorization.R

class TripInfoFragmentConductor : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        if (true) {
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_container,
                    DriverFragment()
                )
                ?.commit()
            return inflater.inflate(R.layout.driver_fragment, container, false)
        } else {
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_container,
                    PassengerFragment()
                )
                ?.commit()
            return inflater.inflate(R.layout.passenger_fragment, container, false)
        }
    }
}
