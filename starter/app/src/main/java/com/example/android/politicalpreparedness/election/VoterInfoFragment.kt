package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.voterInfo.observe(viewLifecycleOwner) {
            if (it != null) {
                if (viewModel.voterInfo.value == null) {
                    Timber.e("Not possible to enable the clickable links, because the voterInfo==null!")
                    return@observe
                }

                val voterInfo = viewModel.voterInfo.value!!
                if (voterInfo.state == null) {
                    showSnackbar(R.string.err_no_state)
                    return@observe
                }

                if (voterInfo.state.isEmpty()) {
                    showSnackbar(R.string.something_went_wrong)
                    return@observe
                }

                enableClickableLinks()
                setCorrespondenceAddress()
            }
        }
    }

    //
    // Clickable Links
    //
    private fun enableClickableLinks() {
        val electionAdministrationBody =
            viewModel.voterInfo.value!!.state!!.first().electionAdministrationBody

        if (electionAdministrationBody.electionInfoUrl == null) {
            Timber.e("Not showing stateLocations, because electionAdministrationBody.electionInfoUrl == null")
        } else {
            binding.stateLocations.visibility = VISIBLE
            binding.stateLocations.setOnClickListener { setIntent(electionAdministrationBody.electionInfoUrl) }
        }


        if (electionAdministrationBody.ballotInfoUrl == null) {
            Timber.e("Not showing stateBallot, because electionAdministrationBody.electionInfoUrl == null")
        } else {
            binding.stateBallot.visibility = VISIBLE
            binding.stateBallot.setOnClickListener { setIntent(electionAdministrationBody.ballotInfoUrl) }
        }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        requireContext().startActivity(intent)
    }

    //
    // Correspondence Address
    //
    private fun setCorrespondenceAddress() {
        val correspondenceAddress =
            viewModel.voterInfo.value!!.state!!.first().electionAdministrationBody.correspondenceAddress
        if (correspondenceAddress != null) {
            binding.addressGroup.visibility = VISIBLE
            binding.address.text = correspondenceAddress.toFormattedString()
        }
    }

    //
    // Utils
    //
    private fun showSnackbar(resId: Int) {
        Snackbar.make(
            binding.voterInfoLayout, resId, Snackbar.LENGTH_LONG
        ).show()
    }
}