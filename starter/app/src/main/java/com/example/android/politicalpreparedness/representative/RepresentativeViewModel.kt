package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(application: Application) : AndroidViewModel(application) {

    private val representativeRepository: RepresentativeRepository = RepresentativeRepository()

    private val _myRepresentatives = MutableLiveData<List<Representative>>()
    val myRepresentatives: LiveData<List<Representative>>
        get() = _myRepresentatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    fun setAddress(address: Address) {
        _address.value = address

        fetchRepresentatives(address)
    }

    fun onFindMyRepresentativesButtonClicked() {
        _address.value?.let { fetchRepresentatives(it) }
    }

    private fun fetchRepresentatives(address: Address) {
        viewModelScope.launch {
            val representativeResponse = representativeRepository.fetchRepresentatives(address)
            _myRepresentatives.value = representativeResponse.offices.flatMap { office ->
                office.getRepresentatives(representativeResponse.officials)
            }
        }
    }

    //
    // Factory
    //
    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
                return RepresentativeViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
