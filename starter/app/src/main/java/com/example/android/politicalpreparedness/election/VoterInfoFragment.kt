package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.models.State
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    lateinit var binding: FragmentVoterInfoBinding

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.followElectionButton.setOnClickListener {
            viewModel.onFollowElectionClicked()
        }

        enableClickableLinks()

        return binding.root
    }

    private fun enableClickableLinks() {
        if (viewModel.voterInfo.value == null) {
            showSnackbar(R.string.err_no_voter_info)
            return
        }

        val voterInfo = viewModel.voterInfo.value!!
        if (voterInfo.state == null) {
            showSnackbar(R.string.err_no_state)
            return
        }

        if (voterInfo.state.isEmpty()) {
            showSnackbar(R.string.something_went_wrong)
            return
        }

        val electionAdministrationBody = voterInfo.state.first().electionAdministrationBody

        if (electionAdministrationBody.electionInfoUrl == null) {
            showSnackbar(R.string.cannot_get_election_info_url)
            return
        }
        binding.stateLocations.setOnClickListener { setIntent(electionAdministrationBody.electionInfoUrl) }



        if (electionAdministrationBody.ballotInfoUrl == null) {
            showSnackbar(R.string.cannot_get_ballot_info_url)
            return
        }
        binding.stateBallot.setOnClickListener { setIntent(electionAdministrationBody.ballotInfoUrl) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        requireContext().startActivity(intent)
    }

    private fun showSnackbar(resId: Int) {
        Snackbar.make(
            binding.voterInfoLayout, resId, Snackbar.LENGTH_LONG
        ).show()
    }

    // TODO: Create method to load URL intents
}