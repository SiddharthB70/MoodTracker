package com.example.moodtracker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entry_solution")
data class EntrySolution(
    val solution: String,
    val entryId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
