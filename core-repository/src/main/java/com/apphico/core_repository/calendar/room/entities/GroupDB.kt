package com.apphico.core_repository.calendar.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupDB(
    @PrimaryKey(autoGenerate = true) val groupId: Long = 0,
    @ColumnInfo("group_name") val name: String,
    val color: Int
)