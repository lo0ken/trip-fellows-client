package com.tripfellows.authorization

import TripListFragment
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.tripfellows.authorization.fragment.*
import com.tripfellows.authorization.listeners.MainRouter
import com.tripfellows.authorization.network.request.UpdateFcmTokenRequest
import com.tripfellows.authorization.repo.FcmTokenRepo
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

        initializeFcmMessaging()

        if (isAppLink(intent)) {
            openAppLink(intent.data!!)
            return
        }

        if (isPushNotification(intent)) {
            openPushNotification(intent)
            return
        }

        if (savedInstanceState == null){
            toolbar.title = getString(R.string.toolbar_search)
            loadFragment(TripListFragment())
        }
    }

    private fun isAppLink(intent: Intent?): Boolean {
        return intent != null && intent.data != null
    }

    private fun isPushNotification(intent: Intent?): Boolean {
        return intent?.getBooleanExtra("isPush", false)!!
    }

    private fun openAppLink(appLinkData: Uri) {
        checkAuthentication()
        val tripId: String? = appLinkData.lastPathSegment

        if (tripId != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container,
                    TripViewFragment.newInstance(tripId.toInt(), false))
                .addToBackStack("Fragment close")
                .commit()
        }
    }

    private fun openPushNotification(intent: Intent) {
        checkAuthentication()
        val tripId = intent.getStringExtra("tripId")
        val creatorUid: String? = intent.getStringExtra("creatorUid")

        if (tripId != null && creatorUid != null) {
            showTrip(tripId.toInt(), creatorUid)
        }
    }

    private fun checkAuthentication() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            this.finish()
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

    private fun initializeFcmMessaging() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        FirebaseMessaging.getInstance().subscribeToTopic("TRIP")

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                FcmTokenRepo.getInstance(this).updateFcmToken(UpdateFcmTokenRequest(it.result))
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
            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
            .replace(R.id.main_fragment_container,
                TripViewFragment.newInstance(tripId, userUid == creatorUid))
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

    override fun showShareButton(): Button {
        val shareButton: Button = findViewById(R.id.share_button)
        shareButton.visibility = View.VISIBLE
        return shareButton
    }

    override fun hideShareButton() {
        val shareButton: Button = findViewById(R.id.share_button)
        shareButton.visibility = View.GONE
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