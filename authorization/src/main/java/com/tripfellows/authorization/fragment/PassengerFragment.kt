package com.tripfellows.authorization.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tripfellows.authorization.R

class PassengerFragment : Fragment() {
    private lateinit var joinButton: Button

    private lateinit var cancelButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.passenger_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            setStatus(view, getString(R.string.status_waiting))
        }

        joinButton = view.findViewById(R.id.join_button)
        joinButton.setOnClickListener { joinButtonPressed() }

        cancelButton = view.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener { cancelButtonPressed() }
        cancelButton.visibility = View.GONE
    }

    private fun setStatus(view: View, status: String) {
        val statusTextView = view.findViewById<TextView>(R.id.status)
        statusTextView.text = status
    }

    private fun cancelButtonPressed() {
        joinButton.visibility = View.VISIBLE
        cancelButton.visibility = View.GONE
    }

    private fun joinButtonPressed() {
        joinButton.visibility = View.GONE
        cancelButton.visibility = View.VISIBLE
    }
}
