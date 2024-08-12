package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter


class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }

        ViewModelProvider(
            this, ElectionsViewModel.Factory(activity.application)
        )[ElectionsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.upcomingElectionsRecyclerView.adapter =
            ElectionListAdapter(ElectionListAdapter.OnClickListener {
                viewModel.displayDetails(it)
            })

        binding.savedElectionsRecyclerView.adapter =
            ElectionListAdapter(ElectionListAdapter.OnClickListener {
                viewModel.displayDetails(it)
            })

        viewModel.navigateToDetails.observe(viewLifecycleOwner) {
            if (null != it) {
                this.findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        it.id, it.name
                    )
                )
                viewModel.displayDetailsComplete()
            }
        }

        return binding.root
    }

    // TODO: Refresh adapters when fragment loads
}