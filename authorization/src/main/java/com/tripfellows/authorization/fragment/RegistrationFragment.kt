package com.tripfellows.authorization.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tripfellows.authorization.R
import com.tripfellows.authorization.request.SignUpRequest

class RegistrationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.registration_fragment, container, false)

        signUpButton(view)

        return view;
    }

    private fun signUpButton(fragmentView: View) {
        val signUpButton = fragmentView.findViewById<Button>(R.id.sign_up_btn)

        signUpButton.setOnClickListener {
            val username = fragmentView.findViewById<TextView>(R.id.sign_up_username).text.toString()
            val password = fragmentView.findViewById<TextView>(R.id.sign_up_password).text.toString()
            val name = fragmentView.findViewById<TextView>(R.id.sign_up_name).text.toString()
            val phoneNumber = fragmentView.findViewById<TextView>(R.id.sign_up_phone).text.toString()

            val signUpRequest = SignUpRequest(username, password, name, phoneNumber)
        }
    }
}