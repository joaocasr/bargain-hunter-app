package com.example.salesapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel: ViewModel() {
    val currentLocationData: MutableLiveData<LocationData> by lazy {
        MutableLiveData<LocationData>()
    }

}