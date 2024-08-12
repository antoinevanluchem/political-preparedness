package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import timber.log.Timber

class VoterInfoFragment : Fragment() {

    private val viewModel: VoterInfoViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }

        val electionId = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId
        val electionName = VoterInfoFragmentArgs.fromBundle(requireArguments()).argEllectionName

        ViewModelProvider(
            this, VoterInfoViewModel.Factory(activity.application, electionId, electionName)
        )[VoterInfoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View {
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.followElectionButton.setOnClickListener {
            viewModel.onFollowElectionClicked()
        }

        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        // TODO: Handle loading of URLs

        // TODO: Handle save button UI state
        // TODO: cont'd Handle save button clicks
        return binding.root
    }

    // TODO: Create method to load URL intents
}