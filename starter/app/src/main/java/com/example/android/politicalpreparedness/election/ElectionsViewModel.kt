package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class ElectionsViewModel(application: Application) : AndroidViewModel(application) {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _followedElections = MediatorLiveData<List<Election>>()
    val followedElections: LiveData<List<Election>>
        get() = _followedElections

    private val electionRepository = ElectionRepository(application)

    init {
        refreshElections()
        setUpFollowedElections()
    }

    private fun setUpFollowedElections() {
        viewModelScope.launch {
            _followedElections.addSource(electionRepository.fetchFollowedElections()) {
                _followedElections.value = it
            }
        }
    }

    fun refreshElections() {
        fetchUpcomingElections()
    }

    private fun fetchUpcomingElections() {
        viewModelScope.launch {
            try {
                _upcomingElections.value = electionRepository.fetchUpcomingElections()
            } catch (e: Exception) {
                Timber.e("Something went wrong while fetching the upcoming elections: $e")
            }
        }
    }

    //
    // Navigate to details
    //
    private val _navigateToDetails = MutableLiveData<Election>()
    val navigateToDetails: LiveData<Election>
        get() = _navigateToDetails

    fun displayDetails(election: Election) {
        _navigateToDetails.value = election
    }

    fun displayDetailsComplete() {
        _navigateToDetails.value = null
    }

    //
    // Factory
    //
    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
                return ElectionsViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}