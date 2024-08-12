package com.example.android.politicalpreparedness.repository

import android.content.Context
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class ElectionRepository(application: Context) {
    private val electionDatabase = ElectionDatabase.getInstance(application)

    suspend fun fetchUpcomingElections(): List<Election> {
        return CivicsApi.retrofitService.getElections().elections
    }

    suspend fun fetchVoterInfo(electionId: Int, electionName: String): VoterInfoResponse {
        return CivicsApi.retrofitService.getVoterInfo(
            mapOf(
                "electionId" to electionId.toString(), "address" to electionName
            )
        )
    }
}