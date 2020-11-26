package com.tripfellows.authorization

import TripListFragment
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
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

        askForPermission(ACCESS_FINE_LOCATION, 1)
        askForPermission(ACCESS_COARSE_LOCATION, 2)

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

    private fun askForPermission(permission: String, requestCode: Int) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permission)) {
                Toast.makeText(this,
                    "Please grant the requested permission to get your task done!",
                    Toast.LENGTH_LONG).show()
                requestPermissions(this@MainActivity,
                    arrayOf(permission),
                    requestCode)
            } else {
                requestPermissions(this@MainActivity,
                    arrayOf(permission),
                    requestCode)
            }
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