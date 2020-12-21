package com.tripfellows.authorization.fragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.MainRouter
import com.tripfellows.authorization.model.Account
import com.tripfellows.authorization.repo.FcmTokenRepo
import com.tripfellows.authorization.states.RequestProgress
import com.tripfellows.authorization.viewmodel.AccountViewModel

class AccountFragment : Fragment() {

    private lateinit var viewModel: AccountViewModel
    private lateinit var router: MainRouter
    private val nightModeKey = "NightMode"
    private val appSettingPref = "AppSettingPrefs"
    private val pushEnable = "PushEnable"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        router = context as MainRouter
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshAccount()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.account_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appSettingPrefs: SharedPreferences = activity!!.getSharedPreferences(appSettingPref, MODE_PRIVATE)
        val btnToggleDark = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean(nightModeKey, false)
        val createTripPushesEnabled: Boolean = appSettingPrefs.getBoolean(pushEnable, false)

        turnNightMode(
            if (isNightModeOn) MODE_NIGHT_YES else MODE_NIGHT_NO
        )

        btnToggleDark.setOnClickListener {
            val newNightModeState = if (isNightModeOn) MODE_NIGHT_NO else MODE_NIGHT_YES
            turnNightMode(newNightModeState)
            sharedPrefsEdit.putBoolean(nightModeKey, !isNightModeOn)
            sharedPrefsEdit.apply()
        }

        viewModel = ViewModelProvider(activity!!, ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
            AccountViewModel::class.java)

        viewModel.getAccount().observe(viewLifecycleOwner, AccountObserver())
        val switch = view.findViewById<SwitchCompat>(R.id.Pushes)
        switch.isChecked = createTripPushesEnabled

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) FirebaseMessaging.getInstance().subscribeToTopic("TRIP")
            else FirebaseMessaging.getInstance().unsubscribeFromTopic("TRIP")
            sharedPrefsEdit.putBoolean(pushEnable, !createTripPushesEnabled)
            sharedPrefsEdit.apply()
        }

        view.findViewById<Button>(R.id.sign_out_btn).setOnClickListener {
            FcmTokenRepo.getInstance(context!!).deleteFcmToken().observe(viewLifecycleOwner, object: Observer<RequestProgress> {
                override fun onChanged(progress: RequestProgress) {
                    if (RequestProgress.SUCCESS == progress) {
                        router.signOut()
                    }
                }
            })
        }
    }

    private fun turnNightMode(nightMode: Int) {
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    inner class AccountObserver : Observer<Account> {
        override fun onChanged(account: Account) {
            view?.findViewById<TextView>(R.id.accountNameVal)?.text = account.name
            view?.findViewById<TextView>(R.id.accountMailVal)?.text = FirebaseAuth.getInstance().currentUser?.email
            view?.findViewById<TextView>(R.id.accountPhoneVal)?.text = account.phoneNumber
        }
    }
}