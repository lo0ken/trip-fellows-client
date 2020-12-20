package com.tripfellows.authorization.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.MainRouter
import com.tripfellows.authorization.model.Account
import com.tripfellows.authorization.repo.FcmTokenRepo
import com.tripfellows.authorization.states.RequestProgress
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

        viewModel = ViewModelProvider(activity!!, ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
            AccountViewModel::class.java)

        viewModel.getAccount().observe(viewLifecycleOwner, AccountObserver())

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

    inner class AccountObserver : Observer<Account> {
        override fun onChanged(account: Account) {
            view?.findViewById<TextView>(R.id.accountNameVal)?.text = account.name
            view?.findViewById<TextView>(R.id.accountMailVal)?.text = FirebaseAuth.getInstance().currentUser?.email
            view?.findViewById<TextView>(R.id.accountPhoneVal)?.text = account.phoneNumber
        }
    }
}
