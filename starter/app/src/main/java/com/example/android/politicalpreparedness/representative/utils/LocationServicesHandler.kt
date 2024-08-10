package com.example.android.politicalpreparedness.representative.utils

import android.app.Activity.RESULT_OK
import android.content.IntentSender
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.representative.DetailFragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class LocationServicesHandler(private val fragment: DetailFragment) {

    fun onLocationServicesEnabled(f: () -> Unit) {
        onLocationServicesEnabledCallback = f

        handle()
    }

    private fun handle() {
        val locationSettingsResponseTask = getLocationSettingsResponseTask()

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    locationSettingsLauncher.launch(
                        IntentSenderRequest.Builder(exception.resolution).build()
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Timber.d("Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    fragment.binding.fragmentRepresentativeMotionLayout,
                    R.string.location_permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    handle()
                }.show()
            }
        }

        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                onLocationServicesEnabledCallback?.invoke()
            }
        }
    }

    private var onLocationServicesEnabledCallback: (() -> Unit)? = null


    private fun getLocationSettingsResponseTask(): Task<LocationSettingsResponse> {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, 10000).build()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(fragment.requireActivity())
        return settingsClient.checkLocationSettings(builder.build())
    }

    private val locationSettingsLauncher: ActivityResultLauncher<IntentSenderRequest> =
        fragment.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                onLocationServicesEnabledCallback?.invoke()
            } else {
                Snackbar.make(
                    fragment.binding.fragmentRepresentativeMotionLayout,
                    R.string.location_services_disabled_explanation,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
}