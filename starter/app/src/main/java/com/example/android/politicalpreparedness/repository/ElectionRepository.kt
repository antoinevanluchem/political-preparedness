package com.example.android.politicalpreparedness.repository

import android.content.Context
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(application: Context) {
    private val electionDao = ElectionDatabase.getInstance(application).electionDao

    //
    // Election
    // - abstract away the follow/unfollow logic, currently an election is followed if its saved in the db,
    // this can change in the future
    suspend fun isElectionFollowed(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            electionDao.getElection(id)?.id == id
        }
    }

    suspend fun followElection(election: Election) {
        return withContext(Dispatchers.IO) {
            electionDao.insert(election)
        }
    }

    suspend fun unfollowElection(id: Int) {
        return withContext(Dispatchers.IO) {
            electionDao.deleteElection(id)
        }
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