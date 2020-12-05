package com.tripfellows.authorization.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import com.tripfellows.authorization.listeners.AuthRouter
import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.states.ActionStatus
import com.tripfellows.authorization.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var authRouter: AuthRouter
    private lateinit var loginViewModel: LoginViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        authRouter = context as AuthRouter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(activity!!, ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(LoginViewModel::class.java)
        initializeSignUpText(view)
        loginButton(view)
    }

    private fun initializeSignUpText(fragmentView: View) {
        val spannableString = SpannableString(getString(R.string.sign_up_link))

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                authRouter.goToSignUp()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.textSize = 55F
            }
        }

        spannableString.setSpan(clickableSpan, 18, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val signUpLink = fragmentView.findViewById<TextView>(R.id.sing_up_link)
        signUpLink.text = spannableString
        signUpLink.movementMethod = LinkMovementMethod.getInstance()
        signUpLink.highlightColor = Color.TRANSPARENT
    }

    private fun loginButton(fragmentView: View) {
        val loginButton = fragmentView.findViewById<Button>(R.id.login_btn)

        loginViewModel.getProgress()
            .observe(viewLifecycleOwner, LoginButtonObserver(loginButton))

        loginButton.setOnClickListener {
            val email = fragmentView.findViewById<TextView>(R.id.auth_email).text.toString()
            val password = fragmentView.findViewById<TextView>(R.id.auth_password).text.toString()

            val loginRequest = LoginRequest(email, password)
            loginViewModel.login(loginRequest)
        }
    }

    inner class LoginButtonObserver(private val loginBtn: Button) : Observer<ActionStatus> {

        override fun onChanged(loginStatus: ActionStatus) {
            when(loginStatus) {
                ActionStatus.NONE -> setButtonEnable(true)
                ActionStatus.ERROR -> {
                    Toast.makeText(context, "Error during login", Toast.LENGTH_LONG).show()
                    setButtonEnable(true)
                }
                ActionStatus.IN_PROGRESS -> setButtonEnable(false)
                ActionStatus.SUCCESS -> {
                    Toast.makeText(context, "Success login", Toast.LENGTH_LONG).show()
                    authRouter.mainMenu()
                }
                else -> setButtonEnable(true)
            }
        }

        private fun setButtonEnable(enabled: Boolean) {
            loginBtn.isEnabled = enabled
        }
    }
}