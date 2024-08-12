package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class VoterInfoViewModel(
    application: Application,
    private val electionId: Int,
    private val electionName: String
) : AndroidViewModel(application) {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()

    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val electionRepository = ElectionRepository(application)

    init {
        fetchVoterInfo()
    }

    private fun fetchVoterInfo() {
        viewModelScope.launch {
            try {
                _voterInfo.value = electionRepository.fetchVoterInfo(electionId, electionName)
            } catch (e: Exception) {
                Timber.e("Something went wrong while fetching the voter info: $e")
            }
        }
    }

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    //
    // Factory
    //
    class Factory(
        private val application: Application,
        private val electionId: Int,
        private val electionName: String
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
                return VoterInfoViewModel(application, electionId, electionName) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}