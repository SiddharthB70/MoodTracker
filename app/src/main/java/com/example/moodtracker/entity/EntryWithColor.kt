package com.example.moodtracker.entity

import androidx.room.Embedded
import androidx.room.Relation

data class EntryWithColor(
    @Embedded val emotionEntry: EmotionEntry,
    @Relation(
        parentColumn = "emotion",
        entityColumn = "emotion"
    )
    val emotionColor: EmotionColor
)