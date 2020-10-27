package com.tripfellows.client

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import com.tripfellows.client.fragment.AccountFragment
import com.tripfellows.client.fragment.CreateTripFragment
import com.tripfellows.client.fragment.HistoryFragment
import com.tripfellows.client.fragment.SearchFragment


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView : BottomNavigationView

    private val onNavigationItemSelectedListener : OnNavigationItemSelectedListener = getOnNavigationItemSelectedListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            loadFragment(SearchFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .commit()
    }

    private fun getOnNavigationItemSelectedListener(): OnNavigationItemSelectedListener {
        return object : OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.search_page -> {
                        loadFragment(SearchFragment())
                        return true
                    }
                    R.id.create_trip_page -> {
                        loadFragment(CreateTripFragment())
                        return true
                    }
                    R.id.history_page -> {
                        loadFragment(HistoryFragment())
                        return true
                    }
                    R.id.account_page -> {
                        loadFragment(AccountFragment())
                        return true
                    }
                    else -> return false
                }
            }
        }
    }
}