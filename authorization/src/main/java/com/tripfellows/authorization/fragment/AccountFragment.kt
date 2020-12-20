package com.tripfellows.authorization.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.MainRouter
import com.tripfellows.authorization.model.Account
import com.tripfellows.authorization.viewmodel.AccountViewModel

class AccountFragment : Fragment() {

    private lateinit var viewModel: AccountViewModel
    private lateinit var router: MainRouter

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
        val btnToggleDark = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val appSettingPrefs: SharedPreferences = activity!!.getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)

        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        btnToggleDark.setOnClickListener {
            if (isNightModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode", false)
                sharedPrefsEdit.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
            }
        }
        viewModel = ViewModelProvider(activity!!, ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
            AccountViewModel::class.java)

        viewModel.getAccount().observe(viewLifecycleOwner, AccountObserver())

        view.findViewById<Button>(R.id.sign_out_btn).setOnClickListener {
            router.signOut()
        }
    }

    inner class AccountObserver : Observer<Account> {
        override fun onChanged(account: Account) {
            view?.findViewById<TextView>(R.id.accountNameVal)?.text = account.name
            view?.findViewById<TextView>(R.id.accountMailVal)?.text = FirebaseAuth.getInstance().currentUser?.email
            view?.findViewById<TextView>(R.id.accountPhoneVal)?.text = account.phoneNumber
        }
    }
}
