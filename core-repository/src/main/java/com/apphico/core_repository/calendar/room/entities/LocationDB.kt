package com.apphico.core_repository.calendar.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationDB(
    @PrimaryKey(autoGenerate = true) val locationId: Long = 0,
    val locationTaskId: Long,
    val latitude: Double,
    val longitude: Double,
    val address: String?
)