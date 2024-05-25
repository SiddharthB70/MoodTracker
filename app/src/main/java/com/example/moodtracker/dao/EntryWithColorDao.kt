package com.example.moodtracker.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.moodtracker.entity.EmotionCount
import com.example.moodtracker.entity.EntryWithColor

@Dao
interface EntryWithColorDao {
    @Transaction
    @Query("Select * from emotion_entry where date=:date order by time")
    fun getEntryWithColor(date: String): MutableList<EntryWithColor>

    @Query("Select ee.emotion as emotion, Count(ee.emotion)  as count ,ec.color as color from emotion_entry ee join emotion_color ec on ee.emotion= ec.emotion where Date(ee.date) between :weekBegin and :weekEnd group by ee.emotion")
    fun getEmotionCount(weekBegin: String,weekEnd: String ):MutableList<EmotionCount>

}