package com.example.prog7314poepart2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripName: String,
    val country: String,
    val startDate: String,
    val endDate: String,
    val notes: String,
    val tripTypes: String, // stored as CSV text
    val weatherCondition: String,
)
