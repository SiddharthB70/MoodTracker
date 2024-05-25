package com.example.moodtracker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "label")
data class Label(
    val labelName: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
