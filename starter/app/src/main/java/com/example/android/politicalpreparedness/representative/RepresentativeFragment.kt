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
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.utils.LocationPermissionHandler
import com.example.android.politicalpreparedness.representative.utils.getAddress
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
    }

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
                useMyLocation()
            }
        }

        return binding.root
    }

    @SuppressLint("MissingPermission") // This is handled by the locationPermissionHandler
    private fun useMyLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
            if (lastLocation == null) {
                return@addOnSuccessListener
            }

            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            geocoder.getAddress(lastLocation.latitude, lastLocation.longitude) { address ->
                if (address == null) {
                    return@getAddress
                }

                viewModel.setAddress(address)
            }

        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}