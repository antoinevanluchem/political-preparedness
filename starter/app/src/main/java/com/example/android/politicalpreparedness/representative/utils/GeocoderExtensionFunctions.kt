package com.example.android.politicalpreparedness.representative.utils

import android.location.Geocoder
import android.os.Build
import android.location.Address as AndroidLocationAddress
import com.example.android.politicalpreparedness.network.models.Address as PoliticalPreparednessAddress

@Suppress("DEPRECATION")
fun Geocoder.getAddress(
    latitude: Double,
    longitude: Double,
    onAddressFetchedCallback: (PoliticalPreparednessAddress?) -> Unit
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
            override fun onGeocode(androidLocationAddresses: MutableList<AndroidLocationAddress>) {
                val politicalPreparednessAddresses = androidLocationAddresses.toPoliticalPreparednessAddress()
                onAddressFetchedCallback(politicalPreparednessAddresses.firstOrNull())
            }

            override fun onError(errorMessage: String?) {
                super.onError(errorMessage)
                onAddressFetchedCallback(null)
            }
        })
    } else {
        try {
            val androidLocationAddresses = getFromLocation(latitude, longitude, 1)
            val politicalPreparednessAddresses = androidLocationAddresses?.toPoliticalPreparednessAddress()
            onAddressFetchedCallback(politicalPreparednessAddresses?.first())
        } catch (e: Exception) {
            onAddressFetchedCallback(null)
        }
    }

}

private fun MutableList<AndroidLocationAddress>.toPoliticalPreparednessAddress(): List<PoliticalPreparednessAddress> {
    return this.map {
        PoliticalPreparednessAddress(
            it.thoroughfare,
            it.subThoroughfare,
            it.locality,
            it.adminArea,
            it.postalCode
        )
    }
}