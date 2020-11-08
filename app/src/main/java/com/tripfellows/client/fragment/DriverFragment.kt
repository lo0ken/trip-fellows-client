package com.tripfellows.client.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tripfellows.client.R

class DriverFragment : Fragment() {
    private lateinit var changeStatusButton: Button

    private lateinit var cancelButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.driver_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            setStatus(view, getString(R.string.status_waiting))
        }

        val editButton: Button = view.findViewById(R.id.edit_button)
        editButton.setOnClickListener { editButtonPressed() }

        cancelButton = view.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener { cancelButtonPressed(view) }

        changeStatusButton = view.findViewById(R.id.change_status_button)
        changeStatusButton.setOnClickListener { changeStatusButtonPressed(view) }
        toggleButtons(view)
    }

    private fun setStatus(view: View, status: String) {
        val statusTextView = view.findViewById<TextView>(R.id.status)
        statusTextView.text = status
    }

    private fun toggleButtons(view: View) {
        when (view.findViewById<TextView>(R.id.status).text.toString()) {
            "Waiting" -> {
                changeStatusButton.text = getString(R.string.status_start)
                cancelButton.visibility = View.VISIBLE
            }
            "Canceled" -> {
                changeStatusButton.text = getString(R.string.status_reopen)
                cancelButton.visibility = View.GONE
            }
            "Started" -> {
                changeStatusButton.text = getString(R.string.status_finish)
                cancelButton.visibility = View.VISIBLE
            }
            "Finished" -> {
                changeStatusButton.text = getString(R.string.status_reopen)
                cancelButton.visibility = View.GONE
            }
        }
    }

    private fun cancelButtonPressed(view: View) {
        setStatus(view, "Canceled")
        toggleButtons(view)
    }

    private fun changeStatusButtonPressed(view: View) {
        when (view.findViewById<TextView>(R.id.status).text.toString()) {
            "Waiting" -> setStatus(view, getString(R.string.status_started))
            "Canceled" -> setStatus(view, getString(R.string.status_waiting))
            "Started" -> setStatus(view, getString(R.string.status_finished))
            "Finished" -> setStatus(view, getString(R.string.status_started))
        }
        toggleButtons(view)
    }

    private fun editButtonPressed() {
        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.main_fragment_container, EditTripFragment())
            ?.addToBackStack("EditTripFragment")
            ?.commit()
    }
}
