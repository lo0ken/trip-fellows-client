package com.tripfellows.authorization.fragment

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.AuthRouter
import com.tripfellows.authorization.listeners.ConnectionRouter
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.authorization.states.ActionStatus
import com.tripfellows.authorization.util.ValidationPatterns
import com.tripfellows.authorization.viewmodel.RegistrationViewModel

class RegistrationFragment : Fragment() {

    private lateinit var authRouter: AuthRouter
    private lateinit var signUpViewModel: RegistrationViewModel
    private lateinit var connectionRouter : ConnectionRouter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        authRouter = context as AuthRouter
        connectionRouter = context as ConnectionRouter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.registration_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpViewModel = ViewModelProvider(activity!!, ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
            RegistrationViewModel::class.java)
        
        signUpButton(view)
    }

    private fun signUpButton(fragmentView: View) {
        val signUpButton = fragmentView.findViewById<Button>(R.id.sign_up_btn)
        val awesomeVal = AwesomeValidation(ValidationStyle.BASIC)
        awesomeVal.addValidation(activity, R.id.sign_up_email, Patterns.EMAIL_ADDRESS, R.string.invalide_email)
        awesomeVal.addValidation(activity, R.id.sign_up_password, ValidationPatterns.PASSWORD, R.string.invalide_password)
        awesomeVal.addValidation(activity, R.id.sign_up_name, RegexTemplate.NOT_EMPTY, R.string.invalide_name)
        awesomeVal.addValidation(activity, R.id.sign_up_phone, ValidationPatterns.PHONE_NUMBER, R.string.invalide_number_phone)

        signUpViewModel.getProgress()
            .observe(viewLifecycleOwner, SignUpButtonObserver(signUpButton))

        signUpButton.setOnClickListener {
            if (!awesomeVal.validate()) {
                val toast = Toast.makeText(context, "Validation failed", Toast.LENGTH_SHORT)
                toast.show()
            }
            if (!connectionRouter.hasInternetConnection()) {
                return@setOnClickListener
            }
            if (awesomeVal.validate()){
                val email = fragmentView.findViewById<TextView>(R.id.sign_up_email).text.toString()
                val password = fragmentView.findViewById<TextView>(R.id.sign_up_password).text.toString()
                val name = fragmentView.findViewById<TextView>(R.id.sign_up_name).text.toString()
                val phoneNumber = fragmentView.findViewById<TextView>(R.id.sign_up_phone).text.toString()

                val signUpRequest = SignUpRequest(email, password, name, phoneNumber)

                if (!connectionRouter.hasInternetConnection()) {
                    return@setOnClickListener
                }
                signUpViewModel.signUp(signUpRequest)}
        }
    }

    inner class SignUpButtonObserver(private val signUpBtn: Button) : Observer<ActionStatus> {
        override fun onChanged(signUpStatus: ActionStatus) {
            when(signUpStatus) {
                ActionStatus.NONE -> setButtonEnable(true)
                ActionStatus.ERROR -> {
                    Toast.makeText(context, "Error during signUp", Toast.LENGTH_LONG).show()
                    setButtonEnable(true)
                }
                ActionStatus.IN_PROGRESS -> setButtonEnable(false)
                ActionStatus.SUCCESS -> {
                    Toast.makeText(context, "Success signUp", Toast.LENGTH_LONG).show()
                    authRouter.mainMenu()
                }
                else -> setButtonEnable(true)
            }
        }

        private fun setButtonEnable(enabled: Boolean) {
            signUpBtn.isEnabled = enabled
        }
    }
}