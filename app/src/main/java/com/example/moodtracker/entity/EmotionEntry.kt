package com.example.moodtracker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emotion_entry")
data class EmotionEntry(
    val emotion: String,
    val label: String,
    val note: String,
    val date: String,
    val time: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
