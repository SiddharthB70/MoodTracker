package com.example.moodtracker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emotion_color")
data class EmotionColor(
    val emotion: String,
    val color: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
