package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {
// TODO: fix bug

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election): Long

    @Query("SELECT * FROM election_table WHERE ID=:id")
    fun getElection(id: Int): Election?

    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<Election>?>

    @Query("DELETE FROM election_table WHERE ID=:id")
    suspend fun deleteElection(id: Int)
}