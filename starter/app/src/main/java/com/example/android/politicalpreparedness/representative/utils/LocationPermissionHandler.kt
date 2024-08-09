package com.example.android.politicalpreparedness.representative.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.representative.DetailFragment
import com.google.android.material.snackbar.Snackbar

class LocationPermissionHandler(private val fragment: DetailFragment) {
    fun onPermissionGranted(f: () -> Unit) {
        onPermissionGrantedCallback = f
        if (isLocationPermissionGranted()) {
            onPermissionGrantedCallback?.invoke()
        } else {
            requestLocationPermission()
        }
    }

    private var onPermissionGrantedCallback: (() -> Unit)? = null

    private fun isLocationPermissionGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            fragment.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestLocationPermission() {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

    }

    private val locationPermissionLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGrantedCallback?.invoke()
        } else {
            showPermissionDeniedSnackbar()
        }
    }

    private fun showPermissionDeniedSnackbar() {
        Snackbar.make(
            fragment.binding.fragmentRepresentativeMotionLayout,
            R.string.location_permission_denied_explanation,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.settings) {
            // Displays App settings screen.
            fragment.startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }.show()
    }
}