package com.example.android.politicalpreparedness.representative

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.utils.LocationPermissionHandler
import com.example.android.politicalpreparedness.representative.utils.LocationServicesHandler
import com.example.android.politicalpreparedness.representative.utils.getAddress
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class DetailFragment : Fragment() {
    private val viewModel: RepresentativeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }

        ViewModelProvider(
            this, RepresentativeViewModel.Factory(activity.application)
        )[RepresentativeViewModel::class.java]
    }

    lateinit var binding: FragmentRepresentativeBinding

    private val locationPermissionHandler = LocationPermissionHandler(this)
    private val locationServicesHandler = LocationServicesHandler(this)

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.myRepresentativesRecyclerView.adapter = RepresentativeListAdapter()

        binding.buttonLocation.setOnClickListener {
            hideKeyboard()
            locationPermissionHandler.onPermissionGranted {
                locationServicesHandler.onLocationServicesEnabled {
                    useMyLocation()
                }
            }
        }

        binding.buttonSearch.setOnClickListener {
            viewModel.onFindMyRepresentativesButtonClicked()
        }

        return binding.root
    }

    //
    // Location
    //
    @SuppressLint("MissingPermission") // This is handled by the locationPermissionHandler
    private fun useMyLocation() {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token
        ).addOnSuccessListener { lastLocation ->
            val geocoder = Geocoder(requireContext(), Locale.getDefault())

            geocoder.getAddress(lastLocation.latitude, lastLocation.longitude) { address ->
                if (address == null) {
                    showSnackbar(R.string.err_fetching_address)
                    return@getAddress
                }

                viewModel.setAddress(address)
            }
        }.addOnFailureListener {
            showSnackbar(R.string.err_fetching_location)
        }
    }

    //
    // Utils
    //
    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun showSnackbar(resId: Int) {
        Snackbar.make(
            binding.fragmentRepresentativeMotionLayout, resId, Snackbar.LENGTH_LONG
        ).show()
    }
}