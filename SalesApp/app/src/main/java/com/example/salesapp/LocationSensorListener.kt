package com.example.salesapp

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.example.salesapp.model.LocationData
import com.example.salesapp.model.LocationViewModel


class LocationSensorListener: LocationListener {
    private lateinit var sensorManager: LocationManager
    private lateinit var locationvm: LocationViewModel


    fun setSensorManager(sensorMan: LocationManager,loc: LocationViewModel) {
        sensorManager = sensorMan
        locationvm = loc
    }

    override fun onLocationChanged(location: Location) {
        LocationData.latitude = location.latitude
        LocationData.longitude = location.longitude
        locationvm.currentLocationData.value= LocationData
        sensorManager.removeUpdates(this)

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

}