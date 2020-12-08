package com.tripfellows.authorization

import TripListFragment
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Pulse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.fragment.*
import com.tripfellows.authorization.listeners.MainRouter
import com.tripfellows.authorization.repo.TripRepo


class MainActivity : AppCompatActivity(), MainRouter {
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var toolbar: Toolbar

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

   override fun showTrip(tripId: Int, creatorUid: String) {
       val userUid = FirebaseAuth.getInstance().currentUser?.uid

        supportFragmentManager.beginTransaction()
            //.addSharedElement(sharedElement, transitionName)
            .replace(R.id.main_fragment_container, TripViewFragment.newInstance(tripId, userUid == creatorUid))
            .addToBackStack("Fragment close")
            .commit()
    }

    override fun tripCreated() {
        loadFragment(TripListFragment())
    }

    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, AuthorizationActivity::class.java))
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
                        createTripFragment()
                        return true
                    }
                    R.id.my_trip_page -> {
                        toolbar.title = getString(R.string.toolbar_my_trip)
                        myTripFragment()
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

    private fun createTripFragment() {
        val tripRepo = TripRepo.getInstance(applicationContext)

        tripRepo.getCurrentTrip().observe(this, Observer {
            if (it != null) {
                loadFragment(CreateTripTextFragment())
            } else {
                loadFragment(CreateTripFragment())
            }
        })

        tripRepo.currentTripRequest()
    }

    private fun myTripFragment() {
        val tripRepo = TripRepo.getInstance(applicationContext)

        tripRepo.getCurrentTrip().observe(this, Observer {
            if (it != null) {
                val currentUid = FirebaseAuth.getInstance().uid
                loadFragment(TripViewFragment.newInstance(it.id, currentUid == it.creator.uid))
            } else {
                loadFragment(MyTripExistsFragment())
            }
        })

        tripRepo.currentTripRequest()
    }
}