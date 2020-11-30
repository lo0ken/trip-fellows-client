package com.tripfellows.authorization.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tripfellows.authorization.R

class DisabledMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var marker: Marker
    private val ZOOMING = 15.0f

    companion object {
        private const val LATITUDE_KEY = "latitude"
        private const val LONGITUDE_KEY = "longitude"
        fun newInstance(latLng: LatLng): ActiveMapFragment {
            val fragment = ActiveMapFragment()
            val bundle = Bundle()
            bundle.putDouble(LATITUDE_KEY, latLng.latitude)
            bundle.putDouble(LONGITUDE_KEY, latLng.longitude)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.disabled_map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val backButton: Button = view.findViewById(R.id.back_button)
        backButton.setOnClickListener { backButtonPressed() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun backButtonPressed() {
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, Intent())
        fragmentManager?.popBackStack()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        supportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if (arguments == null) {
            return
        }

        val markerPosition =
            LatLng(arguments!!.getDouble(LATITUDE_KEY), arguments!!.getDouble(LONGITUDE_KEY))
        val markerOptions = MarkerOptions().position(markerPosition)
        marker = map.addMarker(markerOptions)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, ZOOMING))
        map.uiSettings.isZoomControlsEnabled = true

        hideBottomMenu()
    }

    private fun hideBottomMenu() {
        val bottomNavigationView: BottomNavigationView? =
            activity?.findViewById(R.id.bottom_navigation)
        bottomNavigationView?.visibility = View.GONE
    }
}