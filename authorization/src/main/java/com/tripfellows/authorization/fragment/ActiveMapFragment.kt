package com.tripfellows.authorization.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
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


class ActiveMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var lastMarker: Marker
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
        return inflater.inflate(R.layout.active_map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val okButton: Button = view.findViewById(R.id.ok_button)
        okButton.setOnClickListener { okButtonPressed() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun okButtonPressed() {
        val intent = Intent()
        intent.putExtra(LATITUDE_KEY, lastMarker.position.latitude.toString())
        intent.putExtra(LONGITUDE_KEY, lastMarker.position.longitude.toString())
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
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
        lastMarker = map.addMarker(markerOptions)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarker.position, ZOOMING))
        map.uiSettings.isZoomControlsEnabled = true

        hideBottomMenu()

        map.setOnMapLongClickListener(OnMapLongClickListener())
    }

    private fun hideBottomMenu() {
        val bottomNavigationView: BottomNavigationView? =
            activity?.findViewById(R.id.bottom_navigation)
        bottomNavigationView?.visibility = GONE
    }

    inner class OnMapLongClickListener : GoogleMap.OnMapLongClickListener {
        override fun onMapLongClick(latLng: LatLng?) {
            lastMarker.remove()
            lastMarker = map.addMarker(latLng?.let {
                MarkerOptions().position(it)
            })

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarker.position, ZOOMING))
        }
    }
}