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
import androidx.fragment.app.Fragment
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.AuthorizationListener
import com.tripfellows.authorization.request.LoginRequest

class LoginFragment : Fragment() {

    private lateinit var authorizationListener: AuthorizationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        authorizationListener = context as AuthorizationListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        val view = inflater.inflate(R.layout.login_fragment, container, false)

        initializeSignUpText(view)
        loginButton(view)

        return view
    }

    private fun initializeSignUpText(fragmentView: View) {
        val spannableString = SpannableString(getString(R.string.sign_up_link))

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                authorizationListener.goToSignUp()
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

        loginButton.setOnClickListener {
            val email = fragmentView.findViewById<TextView>(R.id.auth_email).text.toString()
            val password = fragmentView.findViewById<TextView>(R.id.auth_password).text.toString()

            val loginRequest = LoginRequest(email, password)
            authorizationListener.signIn(loginRequest)
        }
    }
}