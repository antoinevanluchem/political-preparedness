package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ElectionsFragment: Fragment() {

    // TODO: Declare ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View? {
        val binding = ElectionsFragment.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        binding.asteroidRecycler.adapter = AsteroidListAdapter(AsteroidListAdapter.OnClickListener {
            viewModel.displayDetails(it)
        })

        viewModel.navigateToDetails.observe(this, Observer {
            if ( null != it ) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayDetailsComplete()
            }
        })

        return binding.root
    }

    // TODO: Refresh adapters when fragment loads
}