package com.tripfellows.authorization.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tripfellows.authorization.R

class TripViewFragment : Fragment() {

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
        return inflater.inflate(R.layout.trip_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button: Button = view.findViewById(R.id.join_btn)
        button.setOnClickListener {
            context?.toast("approve")
        }
    }

    private fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}