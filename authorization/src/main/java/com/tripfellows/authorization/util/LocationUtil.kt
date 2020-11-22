package com.tripfellows.authorization.util

import com.google.maps.GeoApiContext

object LocationUtil {
    private var API_KEY = "AIzaSyCJVA5RXMl1LVQsOGs6hef7ca6vZIMFWC8"

    fun getGeoApiContext() : GeoApiContext {
        return GeoApiContext.Builder().apiKey(API_KEY).build()
    }
}