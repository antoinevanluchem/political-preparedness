package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse

class RepresentativeRepository {
    //
    // Fetching
    //
    suspend fun fetchRepresentatives(address: Address): RepresentativeResponse {
        return CivicsApi.retrofitService.getRepresentatives(address.toFormattedString())
    }
}