package com.tripfellows.authorization

import TripListFragment
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import com.tripfellows.authorization.fragment.*
import com.tripfellows.authorization.listeners.MainRouter
import com.tripfellows.authorization.util.MenuItemEnum


class MainActivity : AppCompatActivity(), MainRouter {
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var toolbar: Toolbar

    private var existsCurrentTrip: Boolean = false

    private val onNavigationItemSelectedListener: OnNavigationItemSelectedListener =
        getOnNavigationItemSelectedListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        toggleBottomMenuVisibility()

        if (savedInstanceState == null) {
            toolbar.title = getString(R.string.toolbar_search)
            loadFragment(TripListFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .commit()
    }

   override fun showTrip(tripId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, TripViewFragment.newInstance(tripId))
            .addToBackStack("Fragment close")
            .commit()
    }

    private fun getOnNavigationItemSelectedListener(): OnNavigationItemSelectedListener {
        return object : OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.search_page -> {
                        toolbar.title = getString(R.string.toolbar_search)
                        loadFragment(TripListFragment())
                        return true
                    }
                    R.id.create_trip_page -> {
                        toolbar.title = getString(R.string.toolbar_create_trip)
                        loadFragment(CreateTripFragment())
                        return true
                    }
                    R.id.my_trip_page -> {
                        toolbar.title = getString(R.string.toolbar_my_trip)
                        loadFragment(TripInfoFragmentConductor())
                        return true
                    }
                    R.id.history_page -> {
                        toolbar.title = getString(R.string.toolbar_history)
                        loadFragment(HistoryFragment())
                        return true
                    }
                    R.id.account_page -> {
                        toolbar.title = getString(R.string.toolbar_account)
                        loadFragment(AccountFragment())
                        return true
                    }
                    else -> return false
                }
            }
        }
    }

    override fun createTripButtonPressed() {
        existsCurrentTrip = true
        toggleBottomMenuVisibility()
        onNavigationItemSelectedListener.onNavigationItemSelected(
            bottomNavigationView.menu.getItem(MenuItemEnum.MY_TRIP.id) as MenuItem
        )
        bottomNavigationView.menu.getItem(MenuItemEnum.MY_TRIP.id).isChecked = true
    }

    private fun toggleBottomMenuVisibility() = if (existsCurrentTrip) {
        bottomNavigationView.menu.getItem(MenuItemEnum.MY_TRIP.id).isVisible = true
        bottomNavigationView.menu.getItem(MenuItemEnum.CREATE.id).isVisible = false
    } else {
        bottomNavigationView.menu.getItem(MenuItemEnum.MY_TRIP.id).isVisible = false
        bottomNavigationView.menu.getItem(MenuItemEnum.CREATE.id).isVisible = true
    }
}