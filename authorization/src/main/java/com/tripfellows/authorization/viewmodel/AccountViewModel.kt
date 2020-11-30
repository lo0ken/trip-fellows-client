package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tripfellows.authorization.model.Account
import com.tripfellows.authorization.repo.AuthRepo

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private var accountRepo: AuthRepo  = AuthRepo.getInstance(getApplication())
    private var account: MutableLiveData<Account> = accountRepo.getAccount()

    fun getAccount(): MutableLiveData<Account> {
        return account
    }

    fun refreshAccount() {
        accountRepo.getCurrentAccount()
    }
}