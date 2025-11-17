package com.example.prog7314poepart2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TripDao {
    @Insert
    suspend fun insertTrip(trip: TripEntity)

    @Query("SELECT * FROM trips")
    suspend fun getAllTrips(): List<TripEntity>
}
