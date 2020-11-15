package com.tripfellows.authorization.fragment

import android.content.Context
import android.os.Bundle
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
import com.tripfellows.authorization.listeners.Router
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.authorization.states.SignUpState
import com.tripfellows.authorization.viewmodel.RegistrationViewModel

class RegistrationFragment : Fragment() {

    private lateinit var router: Router
    private lateinit var signUpViewModel: RegistrationViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        router = context as Router
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.registration_fragment, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpViewModel = ViewModelProvider(activity!!, ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
            RegistrationViewModel::class.java)
        
        signUpButton(view)
    }

    private fun signUpButton(fragmentView: View) {
        val signUpButton = fragmentView.findViewById<Button>(R.id.sign_up_btn)

        signUpViewModel.getProgress()
            .observe(viewLifecycleOwner, SignUpButtonObserver(signUpButton))

        signUpButton.setOnClickListener {
            val email = fragmentView.findViewById<TextView>(R.id.sign_up_email).text.toString()
            val password = fragmentView.findViewById<TextView>(R.id.sign_up_password).text.toString()
            val name = fragmentView.findViewById<TextView>(R.id.sign_up_name).text.toString()
            val phoneNumber = fragmentView.findViewById<TextView>(R.id.sign_up_phone).text.toString()

            val signUpRequest = SignUpRequest(email, password, name, phoneNumber)
            
            signUpViewModel.signUp(signUpRequest)
        }
    }

    inner class SignUpButtonObserver(private val signUpBtn: Button) : Observer<SignUpState> {
        override fun onChanged(signUpState: SignUpState) {
            when(signUpState) {
                SignUpState.NONE -> setButtonEnable(true)
                SignUpState.ERROR -> {
                    Toast.makeText(context, "Error during signUp", Toast.LENGTH_LONG).show()
                    setButtonEnable(true)
                }
                SignUpState.IN_PROGRESS -> setButtonEnable(false)
                SignUpState.SUCCESS -> {
                    Toast.makeText(context, "Success signUp", Toast.LENGTH_LONG).show()
                    router.mainMenu()
                }
                else -> setButtonEnable(true)
            }
        }

        private fun setButtonEnable(enabled: Boolean) {
            signUpBtn.isEnabled = enabled
        }
    }
}