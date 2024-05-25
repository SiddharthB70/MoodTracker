package com.example.moodtracker.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moodtracker.entity.EmotionColor

@Dao
interface EmotionColorDao {

    @Upsert
    fun insertEmotionColor(emotionColor: EmotionColor)

    @Query("SELECT * FROM emotion_color")
    fun getEmotionColor(): List<EmotionColor>

    @Query("SELECT * FROM emotion_color WHERE emotion=:emotion")
    fun getEmotionColor(emotion: String): EmotionColor
}