package com.example.android.politicalpreparedness.repository

import android.content.Context
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class ElectionRepository(application: Context) {
    private val electionDao = ElectionDatabase.getInstance(application).electionDao

    //
    // Election
    // - abstract away the follow/unfollow logic, currently an election is followed if its saved in the db,
    // this can change in the future
    fun isElectionFollowed(id: Int): Boolean {
        return electionDao.getElection(id)?.id == id
    }

    suspend fun followElection(election: Election) {
        electionDao.insert(election)
    }

    suspend fun unfollowElection(id: Int) {
        return electionDao.deleteElection(id)
    }


    //
    // Fetching
    //
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